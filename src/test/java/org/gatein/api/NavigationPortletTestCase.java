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
import org.gatein.api.navigation.Navigation;
import org.gatein.api.navigation.Page;
import org.gatein.api.navigation.Site;
import org.testng.annotations.Test;

import java.util.Iterator;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class NavigationPortletTestCase
{
   private GateIn gateIn = null;

   @Test(enabled = false)
   public void shouldListSpecificGroupPages()
   {
      Id groupId = Ids.groupId("platform", "administrators");

      Site<Page> adminSite = gateIn.getGroupSite(groupId);
      IterableResult<Navigation> adminNodes = adminSite.getNavigations();
      assert 2 == adminNodes.size();

      Iterator<Navigation> iterator = adminNodes.iterator();

      Navigation administrationNode = iterator.next();
      assert "Administration".equals(administrationNode.getDisplayName());
      assert 2 == administrationNode.getChildrenNumber();
      IterableResult<Navigation> children = administrationNode.getChildren();
      for (Navigation child : children)
      {
         assert child.equals(administrationNode.getChild(child.getName(), child.getClass()));
         Node<?> target = child.getTarget();
         assert target.equals(gateIn.get(target.getId()));
         assert target.getInboundNavigations().contains(child);
      }

      Navigation wsrp = iterator.next();
      assert "WSRP".equals(wsrp.getDisplayName());
      Node<?> target = wsrp.getTarget();
      assert target instanceof Page;
      assert 1 == wsrp.getChildrenNumber();

      assert !iterator.hasNext();
   }

   @Test(enabled = false)
   public void shouldListGroupPages()
   {
      final Id id = Ids.userId("root");

      IterableResult<Site<Page>> rootSites = gateIn.getGroupSites(id);
      assert 3 == rootSites.size();

      for (Site<Page> site : rootSites)
      {
         Page page = site.getTarget();
         assert page.getDisplayName().equals(site.getDisplayName());
         assert Site.Type.GROUP.equals(site.getType());
         assert site.getNavigations().size() >= page.getChildrenNumber(); // we should have at least as many navigations as the target page has children pages
      }
   }

   @Test(enabled = false)
   public void shouldListSitePages()
   {
      final Id id = Ids.userId("root");

      IterableResult<Site<Portal>> portals = gateIn.getPortalSites(id);
      assert 1 == portals.size();

      Iterator<Site<Portal>> sites = portals.iterator();

      Site<Portal> site = sites.next();
      assert Site.Type.PORTAL.equals(site.getType());
      assert "classic".equals(site.getName());
      Portal target = site.getTarget();
      assert gateIn.getPortal(Ids.portalId("classic")).equals(target);
      IterableResult<Navigation> navigations = site.getNavigations();
      assert 2 == navigations.size();
   }

   @Test(enabled = false)
   public void shouldListDashboardPages()
   {
      final Id id = Ids.userId("root");

      Site dashboard = gateIn.getDashboard(id);
      assert Site.Type.DASHBOARD.equals(dashboard.getType());
      IterableResult<Navigation> nodes = dashboard.getNavigations();
      assert 1 == nodes.size();
      assert "Dashboard".equals(nodes.iterator().next().getDisplayName());
   }
}
