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

import org.gatein.api.annotation.NotNull;
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

   public static Site.Id siteId(@NotNull String siteName)
   {
      return new Site.Id(Site.Type.SITE, siteName);
   }

   public static Site.Id siteId(@NotNull Group group)
   {
      return new Site.Id(Site.Type.SPACE, group.getId());
   }

   public static Site.Id siteId(@NotNull User user)
   {
      return new Site.Id(Site.Type.DASHBOARD, user.getId());
   }

   public static Page.Id pageId(@NotNull String siteName, @NotNull String pageName)
   {
      return new Page.Id(siteId(siteName), pageName);
   }

   public static Page.Id pageId(@NotNull Group group, @NotNull String pageName)
   {
      return new Page.Id(siteId(group), pageName);
   }

   public static Page.Id pageId(@NotNull User user, @NotNull String pageName)
   {
      return new Page.Id(siteId(user), pageName);
   }

   //--------------------------------------- Id format utility methods -----------------------------------------------//

   public static String format(Site.Id siteId)
   {
      return format(siteId, "%s.%s", true);
   }

   public static String format(Page.Id pageId)
   {
      return format(pageId, "%s.%s.%s", true);
   }

   public static String format(Formatting formatting, String format, boolean urlSafe)
   {
      if (format == null) throw new IllegalArgumentException("format cannot be null.");

      return new Formatter().format(format, formatting.getFormattedParts(urlSafe)).toString();
   }

   public static <T> T fromString(Class<T> type, String idAsString)
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

   private static Site.Id siteIdFromString(@NotNull String idAsString)
   {
      if (idAsString == null) throw new IllegalArgumentException("idAsString cannot be null.");

      String[] parts = idAsString.split(Pattern.quote("."));
      if (parts.length != 2) throw new IllegalArgumentException("Invalid id format '" + idAsString + "'");

      String typeName = parts[0];
      String siteName = parts[1];
      siteName = siteName.replaceAll("~", "/");

      Site.Type type = Site.Type.forName(typeName);
      if (type == null) throw new IllegalArgumentException("Invalid site type '" + typeName + "'" + " for id format '" + idAsString + "'");

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
}
