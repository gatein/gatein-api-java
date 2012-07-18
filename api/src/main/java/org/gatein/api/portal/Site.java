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


import org.gatein.api.commons.Filter;
import org.gatein.api.commons.PropertyType;
import org.gatein.api.commons.Range;
import org.gatein.api.exception.EntityAlreadyExistsException;
import org.gatein.api.exception.EntityNotFoundException;
import org.gatein.api.security.SecurityRestriction;
import org.gatein.api.util.ExternalizedBase64;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @author <a href="mailto:bdawidow@redhat.com">Boleslaw Dawidowicz</a>
 */
public interface Site
{
   /** @return Id of the site */
   Id getId();

   /** @return Name of the site */
   String getName();

   /** @return Type of the site */
   Type getType();

   /** @return Label of the site */
   Label getLabel();

   /** @return Description of the site */
   String getDescription();

   /**
    * Set description of the site
    *
    * @param description Description of the site
    */
   void setDescription(String description);

   /** @return the locale of the site */
   Locale getLocale();

   /**
    * Sets the locale of the site.
    *
    * @param locale the locale of the site
    */
   void setLocale(Locale locale);

   /** @return the currently set skin */
   String getSkin();

   /**
    * Sets the skin for this site
    *
    * @param skin the skin for this site
    */
   void setSkin(String skin);

   /** @return List of pages related to this site */
   List<Page> getPages();

   /**
    * @param range Range of pages to return
    * @return List of pages related to this site
    */
   List<Page> getPages(Range range);

   /**
    * @param filter the filter to filter pages from returned list
    * @return List of pages related to this site
    */
   List<Page> getPages(Filter<Page> filter);

   /**
    * @param filter the filter to filter pages from returned list
    * @param range  Range of pages to return
    * @return List of pages related to this site
    */
   List<Page> getPages(Filter<Page> filter, Range range);

   /**
    * @param pageName Name of the page
    * @return The page
    */
   Page getPage(String pageName);

   /**
    * Create new page
    *
    * @param pageName Name of the page
    * @return New page
    * @throws EntityAlreadyExistsException
    */
   Page createPage(String pageName) throws EntityAlreadyExistsException;

   /**
    * Remove page
    *
    * @param pageName Name of the page
    * @throws EntityNotFoundException
    */
   void removePage(String pageName) throws EntityNotFoundException;

   /**
    * Retrieves the navigation for the given site, and optionally creates it.
    *
    * @param create if navigation doesn't exist, create it.
    * @return the navigation associated with the site. Can return null if navigation doesn't exist and the parameter
    *         create is false.
    */
   Navigation getNavigation(boolean create);

   /**
    * @param type Type of SecurityRestriction object to obtain
    * @return The SecurityRestriction object. Can be null if site is public
    */
   SecurityRestriction getSecurityRestriction(SecurityRestriction.Type type);

   /**
    * Updates security restrictions for the site.
    *
    * @param securirtyRestriction SecurityRestriction object to update. Cannot be null
    */
   void updateSecurityRestriction(SecurityRestriction securirtyRestriction);

   /**
    * @param user Name of the user
    * @return true if given user can access the site
    */
   boolean hasAccess(String user);

   //TODO: Attributes

   <T> T getProperty(PropertyType<T> property);

   <T> void setProperty(PropertyType<T> property, T value);

   class Id extends ExternalizedBase64
   {
      private Type type;

      private String name;

      private Id(Type type, String name)
      {
         super(new byte[]{(byte)'s'});
         this.type = type;
         this.name = name;
      }

      Id()
      {
         this(null, null);
      }

      public static Id create(Type type, String name)
      {
         if (type == null)
         {
            throw new IllegalArgumentException("type cannot be null");
         }
         if (name == null)
         {
            throw new IllegalArgumentException("name cannot be null");
         }

         return new Id(type, name);
      }

      public static Id site(String siteName)
      {
         return create(Type.SITE, siteName);
      }

      public static Id space(String groupId)
      {
         return create(Type.SPACE, groupId);
      }

      public static Id space(String... groupName)
      {
         StringBuilder groupId = new StringBuilder();
         for (String s : groupName)
         {
            groupId.append("/").append(s);
         }

         return create(Type.SPACE, groupId.toString());
      }

      public static Id dashboard(String userName)
      {
         return create(Type.DASHBOARD, userName);
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

      public Type getType()
      {
         return type;
      }

      public String getName()
      {
         return name;
      }

      @Override
      public String toString()
      {
         return "Site.Id=[type=" + type + ", name=" + name + "]";
      }

      @Override
      protected void writeExternal(DataOutput out) throws IOException
      {
         out.writeUTF(type.name);
         out.writeUTF(name);
      }

      @Override
      protected void readExternal(DataInput in) throws IOException
      {
         type = Type.fromString(in.readUTF());
         name = in.readUTF();
      }

      @Override
      public boolean equals(Object o)
      {
         if (this == o)
         {
            return true;
         }
         if (o == null || getClass() != o.getClass())
         {
            return false;
         }

         Id id = (Id)o;

         if (!name.equals(id.name))
         {
            return false;
         }
         if (type != id.type)
         {
            return false;
         }

         return true;
      }

      @Override
      public int hashCode()
      {
         int result = type.hashCode();
         result = 31 * result + name.hashCode();
         return result;
      }
   }

   public static enum Type
   {
      SITE("site"), SPACE("space"), DASHBOARD("dashboard");

      private final String name;

      private Type(String name)
      {
         this.name = name;
      }

      static Type fromString(String typeAsString)
      {
         if (typeAsString != null)
         {
            for (Type type : Type.values())
            {
               if (type.name.equalsIgnoreCase(typeAsString))
               {
                  return type;
               }
            }
         }
         return null;
      }
   }
}
