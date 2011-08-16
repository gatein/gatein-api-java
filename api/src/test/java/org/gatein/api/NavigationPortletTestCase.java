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

import org.gatein.api.portal.Navigation;
import org.gatein.api.portal.Page;
import org.gatein.api.portal.Portal;
import org.gatein.api.portal.Site;
import org.gatein.api.util.IterableIdentifiableCollection;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public abstract class NavigationPortletTestCase
{
   protected GateIn gateIn;

   @BeforeTest
   public abstract void setUp();

   @Test
   public void shouldListSpecificGroupPages()
   {
      Site adminSite = gateIn.getGroupSite("platform", "administrators");
      Navigation navigation = adminSite.getNavigation();

      Collection<? extends Navigation> adminNodes = navigation.getChildren();
      assert 2 == adminNodes.size();

      Iterator<? extends Navigation> iterator = adminNodes.iterator();

      Navigation administrationNode = iterator.next();
      assert "Administration".equals(administrationNode.getDisplayName());
      assert 2 == administrationNode.getChildren().size();
      Collection<? extends Navigation> children = administrationNode.getChildren();
      for (Navigation child : children)
      {
         assert child.equals(administrationNode.getChild(child.getName()));
         Page target = child.getTargetPage();
         assert target.equals(gateIn.get(target.getId()));
      }

      Navigation wsrp = iterator.next();
      assert "WSRP".equals(wsrp.getDisplayName());
      assert 1 == wsrp.getChildren().size();

      assert !iterator.hasNext();
   }

   @Test(enabled = false)
   public void shouldListGroupPages()
   {
      IterableIdentifiableCollection<Site> rootSites = gateIn.getGroupSites("root");
      assert 3 == rootSites.size();

      Iterator<Site> sites = rootSites.iterator();

      checkGroupSite(sites.next(), 0, 2, "Administrators");
      checkGroupSite(sites.next(), 1, 1, "Executive Board");
      checkGroupSite(sites.next(), 2, 1, "Users");
   }

   private void checkGroupSite(Site site, final int priority, final int navigationsNumber, final String groupName)
   {
      assert Site.GROUP.equals(site.getType());
      assert priority == site.getPriority();
      assert site.getDisplayName().contains(groupName);

      Collection<? extends Navigation> navigations = site.getNavigation().getChildren();
      assert navigationsNumber == navigations.size();
      for (Navigation navigation : navigations)
      {
         assert site.equals(navigation.getSite());

      }
   }

   @Test(enabled = false)
   public void shouldListSitePages()
   {
      IterableIdentifiableCollection<? extends Site> portalResult = gateIn.getPortalSites("root");
      assert 1 == portalResult.size();

      Iterator<? extends Site> portals = portalResult.iterator();

      Portal portal = (Portal)portals.next();
      assert Site.PORTAL.equals(portal.getType());
      assert "classic".equals(portal.getName());
      assert gateIn.getPortal(Site.Id.createPortal("classic")).equals(portal);
      Collection<? extends Navigation> navigations = portal.getNavigation().getChildren();
      assert 2 == navigations.size();
   }

   @Test(enabled = false)
   public void shouldListDashboardPages()
   {
      Site dashboard = gateIn.getDashboard("root");
      assert Site.DASHBOARD.equals(dashboard.getType());
      Collection<? extends Navigation> nodes = dashboard.getNavigation().getChildren();
      assert 1 == nodes.size();
      assert "Dashboard".equals(nodes.iterator().next().getDisplayName());
   }
}
