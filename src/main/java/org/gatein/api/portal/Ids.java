/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.gatein.api.portal;

import org.gatein.api.internal.Strings;
import org.gatein.api.portal.page.Page;
import org.gatein.api.portal.site.Site;

import java.util.Formatter;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Ids
{
   //----------------------------------- Id creation utility methods -------------------------------------------------//

   public static Site.Id siteId(String siteName)
   {
      return new Site.Id(Site.Type.SITE, siteName);
   }

   public static Site.Id siteId(Group group)
   {
      return new Site.Id(Site.Type.SPACE, group.getId());
   }

   public static Site.Id siteId(User user)
   {
      return new Site.Id(Site.Type.DASHBOARD, user.getId());
   }

   public static Page.Id pageId(String siteName, String pageName)
   {
      return new Page.Id(siteId(siteName), pageName);
   }

   public static Page.Id pageId(Group group, String pageName)
   {
      return new Page.Id(siteId(group), pageName);
   }

   public static Page.Id pageId(User user, String pageName)
   {
      return new Page.Id(siteId(user), pageName);
   }

   //--------------------------------------- Id format utility methods -----------------------------------------------//

   /**
    * Formats a site id ensuring group names are URL safe.
    *
    * @param siteId the site id
    * @return the formatted site id.
    */
   public static String format(Site.Id siteId)
   {
      return format(siteId, "%s.%s", GROUP_ADAPTER);
   }

   /**
    * Formats a page id ensuring group names are URL safe.
    *
    * @param pageId the page id
    * @return the formatted page id.
    */
   public static String format(Page.Id pageId)
   {
      return format(pageId, "%s.%s.%s", GROUP_ADAPTER);
   }

   /**
    * Utility method which will format a {@link Formatted} object, for example {@link org.gatein.api.portal.site.Site.Id} using the format
    * defined by {@link Formatter#format(String, Object...)}.
    *
    * @param formatted the formatted object
    * @param format    the string format as defined by {@link Formatter#format(String, Object...)}.
    * @return the formatted string
    */
   public static String format(Formatted formatted, String format)
   {
      return doFormat(formatted, format, null);
   }

   /**
    * Utility method which will format a {@link Formatted} object, for example {@link org.gatein.api.portal.site.Site.Id} using the format
    * used by {@link Formatter#format(String, Object...)}.
    *
    * @param formatted the formatted object
    * @param adapter   a custom adapter which will be passed to the {@link Formatted#getFormatArguments(org.gatein.api.portal.Formatted.Adapter)}
    *                  method.
    * @param format    the string format as defined by {@link Formatter#format(String, Object...)}.
    * @return the formatted string
    */
   public static String format(Formatted formatted, String format, Formatted.Adapter adapter)
   {
      if (adapter == null) throw new IllegalArgumentException("adapter cannot be null");

      return doFormat(formatted, format, adapter);
   }

   private static String doFormat(Formatted formatted, String format, Formatted.Adapter adapter)
   {
      if (formatted == null) throw new IllegalArgumentException("formatted cannot be null");
      if (format == null) throw new IllegalArgumentException("format cannot be null.");

      Object[] args = (adapter == null) ? formatted.getFormatArguments() : formatted.getFormatArguments(adapter);
      return new Formatter().format(format, args).toString();
   }

   /**
    * Creates an id from a string that was produced by the <code>format(id)</code> method. <i>Note</i>: This cannot
    * be used to create an id that was generated from the {@link Ids#format(Formatted, String, org.gatein.api.portal.Formatted.Adapter)}
    * method.
    *
    * @param type       the class which represents the Id you are creating, i.e. Site.Id.class or Page.Id.class
    * @param idAsString the string that was produced by the format method.
    * @return the id
    */
   public static <T extends Formatted> T fromString(Class<T> type, String idAsString)
   {
      if (type == Site.Id.class)
      {
         return type.cast(siteIdFromString(idAsString));
      }
      else if (type == Page.Id.class)
      {
         return type.cast(pageIdFromString(idAsString));
      }
      else
      {
         throw new IllegalArgumentException("Unsupported type " + type);
      }
   }

   private static Site.Id siteIdFromString(String idAsString)
   {
      if (idAsString == null) throw new IllegalArgumentException("idAsString cannot be null.");

      String[] parts = idAsString.split(Pattern.quote("."));
      if (parts.length != 2) throw new IllegalArgumentException("Invalid id format '" + idAsString + "'");

      String typeName = parts[0];
      String siteName = parts[1];
      siteName = siteName.replaceAll("~", "/");

      Site.Type type = Site.Type.forName(typeName);
      if (type == null)
         throw new IllegalArgumentException("Invalid site type '" + typeName + "'" + " for id format '" + idAsString + "'");

      return new Site.Id(type, siteName);
   }

   private static Page.Id pageIdFromString(String idAsString)
   {
      if (idAsString == null) throw new IllegalArgumentException("idAsString cannot be null.");
      String[] parts = Strings.splitter(Pattern.quote(".")).split(idAsString);
      if (parts.length != 3) throw new IllegalArgumentException("Invalid id format '" + idAsString + "'");

      Site.Id siteId = siteIdFromString(parts[0] + "." + parts[1]);
      String pageName = parts[2];

      return new Page.Id(siteId, pageName);
   }

   private static final ReplaceAllAdapter GROUP_ADAPTER = new ReplaceAllAdapter("/", "~", 1);

   private static final class ReplaceAllAdapter implements Formatted.Adapter
   {
      private final int[] indexes;
      private final String regex;
      private final String replacement;

      private ReplaceAllAdapter(String regex, String replacement, int... indexes)
      {
         this.regex = regex;
         this.replacement = replacement;
         this.indexes = indexes;
      }

      @Override
      public Object adapt(int index, Object argument)
      {
         if (argument instanceof String && contains(index))
         {
            return ((String) argument).replaceAll(regex, replacement);
         }

         return argument;
      }

      private boolean contains(int index)
      {
         for (int i : indexes)
         {
            if (i == index) return true;
         }

         return false;
      }
   }
}