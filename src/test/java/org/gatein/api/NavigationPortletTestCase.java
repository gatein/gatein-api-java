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
import org.gatein.api.navigation.Node;
import org.gatein.api.navigation.Nodes;
import org.gatein.api.navigation.Page;
import org.gatein.api.navigation.Site;
import org.testng.annotations.BeforeTest;
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

      Site adminSite = gateIn.getGroupSite(groupId);
      IterableResult<Navigation> adminNodes = adminSite.getNavigationNodes();
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
         assert target.equals(Nodes.get(target.getId()));
      }

      Navigation wsrp = iterator.next();
      assert "WSRP".equals(wsrp.getDisplayName());
      assert wsrp.getTarget() instanceof Page;
      assert 1 == wsrp.getChildrenNumber();

      assert !iterator.hasNext();
   }

   @Test(enabled = false)
   public void shouldListGroupPages()
   {
      final Id id = Ids.userId("root");

      IterableResult<Site> rootSites = gateIn.getGroupSites(id);
      assert 3 == rootSites.size();

      Iterator<Site> sites = rootSites.iterator();

      Site site = sites.next();
      assert "Administrators's pages".equals(site.getDisplayName());
      assert Site.Type.GROUP.equals(site.getType());
      assert 2 == site.getNavigationNodes().size();

      site = sites.next();
      assert "Executive Board's pages".equals(site.getDisplayName());
      assert Site.Type.GROUP.equals(site.getType());
      assert 1 == site.getNavigationNodes().size();

      site = sites.next();
      assert "Users's pages".equals(site.getDisplayName());
      assert Site.Type.GROUP.equals(site.getType());
      assert 1 == site.getNavigationNodes().size();

      assert !sites.hasNext();
   }

   @Test(enabled = false)
   public void shouldListSitePages()
   {
      final Id id = Ids.userId("root");

      IterableResult<Site> portals = gateIn.getPortalSites(id);
      assert 1 == portals.size();

      Iterator<Site> sites = portals.iterator();

      Site site = sites.next();
      assert "classic".equals(site.getName());
      IterableResult<Navigation> nodes = site.getNavigationNodes();
      assert 2 == nodes.size();
   }

   @Test(enabled = false)
   public void shouldListDashboardPages()
   {
      final Id id = Ids.userId("root");

      Site dashboard = gateIn.getDashboard(id);
      IterableResult<Navigation> nodes = dashboard.getNavigationNodes();
      assert 1 == nodes.size();
      assert "Dashboard".equals(nodes.iterator().next().getDisplayName());
   }
}
