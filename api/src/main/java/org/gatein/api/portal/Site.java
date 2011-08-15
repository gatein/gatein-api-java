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

import org.gatein.api.id.Identifiable;
import org.gatein.api.util.HierarchicalContainer;
import org.gatein.api.util.Type;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public interface Site extends Identifiable<Site>
{
   HierarchicalContainer<String, Page> getPageRegistry();

   Navigation getNavigation();

   Type<Site> getType();

   Navigation createNavigationTo(Page node, Navigation parent);

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
}
