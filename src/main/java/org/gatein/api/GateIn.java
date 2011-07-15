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

import org.gatein.api.content.Application;
import org.gatein.api.content.Content;
import org.gatein.api.content.Gadget;
import org.gatein.api.content.ManagedContent;
import org.gatein.api.id.Id;
import org.gatein.api.id.Identifiable;
import org.gatein.api.portal.Portal;
import org.gatein.api.portal.Site;

import java.net.URI;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public interface GateIn
{
   IterableResult<Portal> getPortals();

   Portal getPortal(Id<Portal> portalId);

   Portal getDefaultPortal();

   IterableResult<Site> getSites();

   IterableResult<Site> getGroupSites();

   Site getGroupSite(Id groupId);

   IterableResult<Site> getGroupSites(Id userId);

   IterableResult<Portal> getPortalSites(Id userId);

   Site getDashboard(Id userId);

   <T extends Identifiable> T get(Id<T> id);

   <T extends Site> T getSite(Id<T> siteId, Site.Type<T> type);

   Id userId(String user);

   Id groupId(String root, String... children);

   Id<Application> applicationId(String application, String portlet);

   Id<Content> wsrpPortletId(String invoker, String portlet);

   Id<Gadget> gadgetId(String gadgetName);

   Id<Gadget> gadgetId(URI uri);

   <T extends Content> Id<ManagedContent<T>> managedContentId(Id<Content<T>> contentId);

   <T extends Site> Id<T> siteId(Site.Type<T> siteType, String portalName);
}
