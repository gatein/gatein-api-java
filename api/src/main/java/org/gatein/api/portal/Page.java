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


import org.gatein.api.security.SecurityRestriction;

import java.util.Formatter;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @author <a href="mailto:bdawidow@redhat.com">Boleslaw Dawidowicz</a>
 */
public interface Page
{
   /**
    * @return Page Id
    */
   Id getId();

   /**
    * @return Site to which this page belongs
    */
   Site getSite();

   /**
    * @return Name of the page
    */
   String getName();

   /**
    * @return Title of the page
    */
   String getTitle();

   /**
    * @param title Title of the page
    */
   void setTitle(String title);

   //TODO: set/get showMaxWindow?


   /**
    * @param type Type of SecurityRestriction object to obtain
    * @return The SecurityRestriction object. Can be null if page is public
    */
   SecurityRestriction getSecurityRestriction(SecurityRestriction.Type type);

   /**
    * Updates security restrictions for the page.
    * @param securityRestriction SecurityRestriction object to update. Cannot be null
    */
   void updateSecurityRestriction(SecurityRestriction securityRestriction);

   /**
    * @param user Name of the user
    * @return true if given user can access the page
    */
   boolean hasAccess(String user);

   /**
    * Page Id
    */
   class Id
   {
      private final Site.Id siteId;
      private final String pageName;

      private Id(Site.Id siteId, String pageName)
      {
         this.siteId = siteId;
         this.pageName = pageName;
      }

      /**
       * Create a <code>Page.Id</code> from the <code>Site.Id</code> and page name
       *
       * @param siteId Id of the site
       * @param pageName Name of the page
       * @return The page id
       */
      public static Id create(Site.Id siteId, String pageName)
      {
         if (siteId == null) throw new IllegalArgumentException("siteId cannot be null");
         if (pageName == null) throw new IllegalArgumentException("pageName cannot be null");

         return new Id(siteId, pageName);
      }

      /**
       * Create a <code>Page.Id</code> from the <code>Site.Type</code> site name, and page name
       *
       * @param type Type of the site
       * @param siteName Name of the site
       * @param pageName Name of the page
       * @return The page id
       */
      public static Id create(Site.Type type, String siteName, String pageName)
      {
         return create(Site.Id.create(type, siteName), pageName);
      }

      /**
       * @return Id of the site
       */
      public Site.Id getSiteId()
      {
         return siteId;
      }

      /**
       * @return Name of the page
       */
      public String getPageName()
      {
         return pageName;
      }

      /**
       * @return String representation of the Id
       */
      @Override
      public String toString()
      {
         return format("Page.Id[siteType=%s, siteName=%s, pageName=%s]", false);
      }

      public String format(String format, boolean urlSafe)
      {
         if (format == null) throw new IllegalArgumentException("format cannot be null.");

         String typeName = siteId.getType().name;
         String siteName = siteId.getName();
         if (urlSafe) siteName = Site.Id.fixSiteName(siteName);

         return new Formatter().format(format, typeName, siteName, pageName).toString();
      }

      public String format()
      {
         return format(FORMAT, true);
      }

      public static Id fromString(String idAsString)
      {
         if (idAsString == null) throw new IllegalArgumentException("idAsString cannot be null.");
         String[] parts = idAsString.split(Pattern.quote("" + SEP));
         if (parts.length != 3) throw new IllegalArgumentException("Invalid id format '" + idAsString + "'");

         Site.Id siteId = Site.Id.fromString(parts[0] + SEP + parts[1]);

         // Parse node path and fix
         String pageName = parts[2];

         return create(siteId, pageName);
      }

      @Override
      public boolean equals(Object o)
      {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;

         Id id = (Id) o;

         if (!pageName.equals(id.pageName)) return false;
         if (!siteId.equals(id.siteId)) return false;

         return true;
      }

      @Override
      public int hashCode()
      {
         int result = siteId.hashCode();
         result = 31 * result + pageName.hashCode();
         return result;
      }

      private static final char SEP = Site.Id.SEP;
      private static final String FORMAT = Site.Id.FORMAT + SEP + "%s";
   }
}
