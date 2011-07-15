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

import org.gatein.api.Container;
import org.gatein.api.id.Identifiable;

import java.lang.reflect.ParameterizedType;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public interface Site extends Identifiable
{
   Container<String, Page> getPageRegistry();

   Navigation getNavigation();

   Type getType();

   Navigation createNavigationTo(Page node, Navigation parent);

   int getPriority();

   public static abstract class Type<U extends Site>
   {
      private final String name;
      private final Class<U> valueType;

      private Type(String name)
      {
         this.name = name;
         valueType = (Class<U>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
      }

      public String getName()
      {
         return name;
      }

      public static Type<? extends Site> forName(String name)
      {
         if (PORTAL_NAME.equals(name))
         {
            return PORTAL;
         }
         else if (GROUP_NAME.equals(name))
         {
            return GROUP;
         }
         else if (DASHBOARD_NAME.equals(name))
         {
            return DASHBOARD;
         }
         else
         {
            throw new IllegalArgumentException("Unknown Type: " + name);
         }
      }

      public Class<U> getValueType()
      {
         return valueType;
      }

      public static final String PORTAL_NAME = "portal";
      public static final String DASHBOARD_NAME = "user";
      public static final String GROUP_NAME = "group";
      public static final Type<Portal> PORTAL = new Type<Portal>(Type.PORTAL_NAME)
      {
      };
      public static final Type<Site> DASHBOARD = new Type<Site>(DASHBOARD_NAME)
      {
      };
      public static final Type<Site> GROUP = new Type<Site>(GROUP_NAME)
      {
      };
   }
}
