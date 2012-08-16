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


import org.gatein.api.portal.site.Site;
import org.gatein.api.security.SecurityRestriction;
import org.gatein.api.util.ExternalizedBase64;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;

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
   class Id extends ExternalizedBase64
   {
      private Site.Id siteId;
      private String pageName;

      private Id(Site.Id siteId, String pageName)
      {
         super(new byte[]{(byte) 'p'});
         this.siteId = siteId;
         this.pageName = pageName;
      }

      private Id()
      {
         this(null, null);
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

      public static Id fromBase64(InputStream stream) throws IOException
      {
         Id id = new Id();
         id.readExternalBase64(stream);
         return id;
      }

      public static Id fromBase64String(String base64)
      {
         try
         {
            return fromBase64(new ByteArrayInputStream(base64.getBytes("UTF-8")));
         }
         catch (IOException e)
         {
            final IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
            iae.setStackTrace(e.getStackTrace());
            throw iae;
         }
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
         return "Page.Id[pageName="+pageName+", " + siteId+"]";
      }

      @Override
      protected void writeExternal(DataOutput out) throws IOException
      {
         siteId.writeExternal(out);
         out.writeUTF(pageName);
      }

      @Override
      protected void readExternal(DataInput in) throws IOException
      {
         Site.Id si = new Site.Id();
         si.readExternal(in);
         this.siteId = si;
         this.pageName = in.readUTF();
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
   }
}
