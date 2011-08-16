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
      Page page = portal.getPage(name); // Should be create but we don't support it at the moment
      assert name.equals(page.getId().toString());
      assert name.equals(page.getTitle()) : "By default, a Page's title should be the same as its name";
      assert portal.equals(page.getSite());

      page.setTitle(title);
      assert title.equals(page.getTitle());
   }

   @Test
   public void creatingANavigationShouldLinkNavigationAndNode()
   {
      Site.Id classic = Site.Id.createPortal("classic");
      Portal portal = (Portal)gateIn.get(classic);
      assert portal.equals(gateIn.getPortal(classic));

      Page page = portal.getPage("page");
      Page.Id pageId = page.getId();
      assert page.equals(gateIn.get(pageId));

      Navigation nav = portal.getNavigation().getChild("page");
      assert page.equals(nav.getTargetPage());

      Page sub = portal.getPage("sub");
      assert sub.equals(gateIn.get(pageId.getIdForChild("sub")));

//      Navigation navigation = portal.createNavigationTo(sub, portal.getNavigation());
//      assert sub.equals(navigation.getTargetPage());
//      assert portal.getNavigation().getChild(navigation.getName()) != null;
   }
}
