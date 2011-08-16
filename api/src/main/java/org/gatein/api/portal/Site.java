/*
 * JBoss, a division of Red Hat
 * Copyright 2011, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
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

import org.gatein.api.id.BaseId;
import org.gatein.api.id.Identifiable;
import org.gatein.api.util.Type;

import java.util.Map;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public interface Site extends Identifiable<Site>
{

   Id getId();

   Page getPage(String pageName);

   Navigation getNavigation();

   Type<Site> getType();

   int getPriority();

   public static final String PORTAL_TYPE_NAME = "portal";
   public static final String DASHBOARD_TYPE_NAME = "user";
   public static final String GROUP_TYPE_NAME = "group";
   public static final Type<Site> PORTAL = new Type<Site>(PORTAL_TYPE_NAME)
   {
   };
   public static final Type<Site> DASHBOARD = new Type<Site>(DASHBOARD_TYPE_NAME)
   {
   };
   public static final Type<Site> GROUP = new Type<Site>(GROUP_TYPE_NAME)
   {
   };

   final class Id extends BaseId<Site>
   {

      /** . */
      private final Type<Site> type;

      /** . */
      private final String name;

      public Id(Type<Site> type, String name)
      {
         if (type == null)
         {
            throw new NullPointerException();
         }
         if (name == null)
         {
            throw new NullPointerException();
         }

         //
         this.type = type;
         this.name = name;
      }

      public Type<Site> getType()
      {
         return type;
      }

      public String getName()
      {
         return name;
      }

      @Override
      public boolean equals(Object obj)
      {
         if (obj == this)
         {
            return true;
         }
         if (obj instanceof Id)
         {
            Id that = (Id)obj;
            return type.equals(that.type) && name.equals(that.name);
         }
         return false;
      }

      @Override
      public int hashCode()
      {
         return type.hashCode() & name.hashCode();
      }

      public Class<Site> getIdentifiableType()
      {
         return Site.class;
      }
   }
}
