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

package org.gatein.api;

import org.gatein.api.id.Id;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Entry point to the API in absence of CDI injection.
 *
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class GateIn
{
   private static SortedMap<Id<PortalContainer>, PortalContainer> portalContainers = new TreeMap<Id<PortalContainer>, PortalContainer>();

   public static PortalContainer getPortalContainer(String containerName)
   {
      return portalContainers.get(Ids.containerId(containerName));
   }

   public static Portal getPortal(String containerName, String portalName)
   {
      PortalContainer container = getPortalContainer(containerName);
      if (container == null)
      {
         return null;
      }
      return container.getPortal(portalName);
   }

   public static Collection<PortalContainer> getPortalContainers()
   {
      return portalContainers.values();
   }
}
