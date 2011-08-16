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

import org.gatein.api.content.Category;
import org.gatein.api.content.Content;
import org.gatein.api.content.Gadget;
import org.gatein.api.content.ManagedContent;
import org.gatein.api.content.Portlet;
import org.gatein.api.content.WSRP;
import org.gatein.api.id.Id;
import org.gatein.api.id.Identifiable;
import org.gatein.api.portal.Page;
import org.gatein.api.portal.Portal;
import org.gatein.api.portal.Site;
import org.gatein.api.util.IterableIdentifiableCollection;
import org.gatein.api.util.Type;

import java.net.URI;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public interface GateIn
{
   String GATEIN_API = "org.gatein.api.instance";

   IterableIdentifiableCollection<Site> getPortals();

   Portal getPortal(Site.Id portalId);

   Portal getDefaultPortal();

   IterableIdentifiableCollection<Site> getSites();

   IterableIdentifiableCollection<Site> getGroupSites();

   Site getGroupSite(String... groupName);

   IterableIdentifiableCollection<Site> getGroupSites(String userId);

   IterableIdentifiableCollection<Site> getPortalSites(String userId);

   Site getDashboard(String userId);

   <T extends Identifiable<T>> T get(Id<T> id);

   Site getSite(Site.Id siteId);

   Portlet.Id portletId(String application, String portlet);

   WSRP.Id wsrpPortletId(String invoker, String portlet);

   Gadget.Id gadgetId(String gadgetName);

   Gadget.Id gadgetId(URI uri);

   Category.Id categoryId(String name);

   Page.Id pageId(Site.Id ownerSite, String pageName);

   ManagedContent.Id managedContentId(Category.Id categoryId, String name, Content.Id contentId);

   <T> T getProperty(Type<T> property);

   <T> void setProperty(Type<T> property, T value);

   String LIFECYCLEMANAGER_TYPE_NAME = "org.gatein.api.lifecyclemanager";
   Type<LifecycleManager> LIFECYCLE_MANAGER = new Type<LifecycleManager>(LIFECYCLEMANAGER_TYPE_NAME)
   {
   };

   LifecycleManager NO_OP_MANAGER = new LifecycleManager()
   {
      public void begin()
      {
         // do nothing
      }

      public void end()
      {
         // do nothing
      }
   };

   public interface LifecycleManager
   {
      void begin();

      void end();
   }

}
