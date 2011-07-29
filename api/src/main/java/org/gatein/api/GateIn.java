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

   IterableIdentifiableCollection<Portal> getPortals();

   Portal getPortal(Id<Portal> portalId);

   Portal getDefaultPortal();

   IterableIdentifiableCollection<? extends Site> getSites();

   IterableIdentifiableCollection<Site> getGroupSites();

   Site getGroupSite(Id groupId);

   IterableIdentifiableCollection<Site> getGroupSites(Id userId);

   IterableIdentifiableCollection<Portal> getPortalSites(Id userId);

   Site getDashboard(Id userId);

   <T extends Identifiable> T get(Id<T> id);

   <T extends Site> T getSite(Id<T> siteId, Type<T, Site> type);

   Id userId(String user);

   Id groupId(String root, String... children);

   Id<Portlet> portletId(String application, String portlet);

   Id<WSRP> wsrpPortletId(String invoker, String portlet);

   Id<Gadget> gadgetId(String gadgetName);

   Id<Gadget> gadgetId(URI uri);

   Id<Category> categoryId(String name);

   <T extends Site> Id<Page> pageId(Id<T> ownerSite, String pageName);

   <T extends Content> Id<ManagedContent> managedContentId(Id<T> contentId);

   <T extends Site> Id<T> siteId(Type<T, Site> siteType, String portalName);

   <T> T getProperty(Type<T, GateIn> property);

   <T> void setProperty(Type<T, GateIn> property, T value);

   Type<LifecycleManager, GateIn> LIFECYCLE_MANAGER = new Type<LifecycleManager, GateIn>("org.gatein.api.lifecyclemanager")
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
