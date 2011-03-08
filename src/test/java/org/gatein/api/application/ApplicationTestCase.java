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

package org.gatein.api.application;

import org.gatein.api.Filter;
import org.gatein.api.GateIn;
import org.gatein.api.NotFoundException;
import org.gatein.api.Portal;
import org.gatein.api.navigation.Page;
import org.gatein.api.navigation.Window;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class ApplicationTestCase
{
   private Portal portal;

   @BeforeTest
   public void setUp()
   {
      portal = GateIn.getPortal("container", "portal", true);
   }

   @Test(enabled = false, expectedExceptions = NotFoundException.class)
   public void getCategoryShouldFailOnInexistentCategoryIfNotAskedToCreateOnInexistent()
   {
      portal.getApplicationRegistry().getCategory("inexistent", false);
   }

   @Test(enabled = false)
   public void assigningAnApplicationToAWindowOnAPage()
   {
      /**
       * <name>wsrpConfiguration</name>
       <title>WSRP Admin</title>
       <access-permissions>manager:/platform/administrators</access-permissions>
       <edit-permission>manager:/platform/administrators</edit-permission>
       <portlet-application>
       <portlet>
       <application-ref>wsrp-admin-gui</application-ref>
       <portlet-ref>WSRPConfigurationPortlet</portlet-ref>
       </portlet>
       <title>WSRP Admin</title>
       <access-permissions>manager:/platform/administrators</access-permissions>
       <show-info-bar>false</show-info-bar>
       </portlet-application>
       */
      Page page = portal.getPage("page", true);
      assert page != null;

      Window window = page.getWindow("window", true);
      assert window != null;

      Category category = portal.getApplicationRegistry().getCategory("category", true);
      assert category != null;

      Application application = category.getApplication("application");
      assert application != null;

      window.setApplication(application);
      assert application.equals(window.getApplication());

      String title = "title";
      window.setTitle(title);
      assert title.equals(window.getTitle());

      window.setProperty(Window.SHOW_INFO_BAR, false);
      assert !window.getProperty(Window.SHOW_INFO_BAR);
   }

   @Test(enabled = false)
   public void assigningAnApplicationToACategory()
   {
      ApplicationRegistry registry = portal.getApplicationRegistry();
      final Category category = registry.getCategory("category", true);

      Application application = registry.getDeployedApplication("application");
      assert application.getName().equals(application.getDisplayName());
      assert application.equals(application.getDeployedParent()) : "A deployed app should be its own deployed parent";
      assert !application.isManaged();

      final String name = application.getName();
      assert !category.contains(name);

      Application addedApp = category.add(application);
      assert category.contains(name);
      assert addedApp.equals(category.getApplication(name));
      assert addedApp.isManaged();
      assert !application.equals(category.getApplication(name)) : "The added application should be wrapped so that it can be modified";
      assert application.equals(addedApp.getDeployedParent());

      List<Application> applications = registry.getManagedApplications(Filter.ALL, Filter.ALL, Application.SORT_BY_NAME);
      assert applications.size() == 1 : "There should only be one application matching";
      assert applications.contains(addedApp) : "Application should have become managed";
      assert !applications.contains(application) : "Original application should have been left untouched";

      try
      {
         application.setDisplayName("displayName");
         Assert.fail("Shouldn't be possible to modify the original application. Use the result of Category.add instead.");
      }
      catch (ApplicationException e)
      {
         // expected
      }

      addedApp.setDisplayName("displayName");
      assert "displayName".equals(addedApp.getDisplayName());
   }

   @Test(enabled = false, expectedExceptions = ApplicationException.class)
   public void modifyingADeployedApplicationShouldFail()
   {
      Application application = portal.getApplicationRegistry().getDeployedApplication("application");
      application.setDisplayName("foo");
   }
}
