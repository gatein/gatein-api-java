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

package org.gatein.api.support;

import org.gatein.api.GateIn;
import org.gatein.api.content.Content;
import org.gatein.api.portal.Site;
import org.gatein.api.util.Type;

/** @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a> */
public class TypeResolver
{
   public static Type forName(String name, Class originatingClass)
   {
      if (originatingClass == null)
      {
         throw new IllegalArgumentException("Must pass a valid class for which a Type needs to be resolved");
      }

      if (name == null || name.isEmpty())
      {
         throw new IllegalArgumentException("Must pass a valid name for the Type to be resolved");
      }

      if (Site.class.equals(originatingClass))
      {
         if (Site.PORTAL_TYPE_NAME.equalsIgnoreCase(name))
         {
            return Site.PORTAL;
         }
         else if (Site.GROUP_TYPE_NAME.equalsIgnoreCase(name))
         {
            return Site.GROUP;
         }
         else if (Site.DASHBOARD_TYPE_NAME.equalsIgnoreCase(name))
         {
            return Site.DASHBOARD;
         }
      }
      else if (Content.class.equals(originatingClass))
      {
         if (Content.PORTLET_TYPE_NAME.equalsIgnoreCase(name))
         {
            return Content.PORTLET;
         }
         else if (Content.GADGET_TYPE_NAME.equalsIgnoreCase(name))
         {
            return Content.GADGET;
         }
         else if (Content.WSRP_TYPE_NAME.equalsIgnoreCase(name))
         {
            return Content.WSRP;
         }
      }
      else if (GateIn.class.equals(originatingClass))
      {
         if (GateIn.LIFECYCLEMANAGER_TYPE_NAME.equalsIgnoreCase(name))
         {
            return GateIn.LIFECYCLE_MANAGER;
         }
      }

      throw new IllegalArgumentException("Unknown Type '" + name + "' for class " + originatingClass.getName());
   }
}
