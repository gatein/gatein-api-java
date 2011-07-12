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

import org.gatein.api.GateIn;
import org.gatein.api.id.Id;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public abstract class NavigationTestCase
{
   protected GateIn gateIn;

   @BeforeTest
   public abstract void setUp();

   @Test
   public void creatingASimplePage()
   {
      Portal portal = gateIn.getDefaultPortal();

      String name = "name", title = "title";
      Page page = portal.getPageRegistry().createAndAdd(name);
      assert name.equals(page.getId().toString());
      assert name.equals(page.getTitle()) : "By default, a Page's title should be the same as its name";
      assert portal.equals(page.getSite());

      page.setTitle(title);
      assert title.equals(page.getTitle());
   }

   @Test
   public void creatingANavigationShouldLinkNavigationAndNode()
   {
      Id<Portal> classic = gateIn.portalId(Site.Type.PORTAL, "classic");
      Portal portal = gateIn.get(classic);
      assert portal.equals(gateIn.getPortal(classic));

      Page page = portal.getPageRegistry().get("page");
      Id<Page> pageId = page.getId();
      assert page.equals(gateIn.get(pageId));

      Navigation nav = portal.getNavigation().get("page");
      assert page.equals(nav.getTargetPage());

      Page sub = portal.getPageRegistry().get("sub");
      assert sub.equals(gateIn.get(Id.getIdForChild(pageId, "sub")));

      Navigation navigation = portal.createNavigationTo(sub, portal.getNavigation());
      assert sub.equals(navigation.getTargetPage());
      assert sub.getInboundNavigations().contains(navigation.getId());
      assert portal.getNavigation().contains(navigation.getName());

      Navigation inboundNavigation = sub.createInboundNavigationIn(portal, portal.getNavigation());
      assert sub.equals(inboundNavigation.getTargetPage());
      assert sub.getInboundNavigations().contains(inboundNavigation.getId());
      assert portal.getNavigation().contains(inboundNavigation.getName());
   }
}
