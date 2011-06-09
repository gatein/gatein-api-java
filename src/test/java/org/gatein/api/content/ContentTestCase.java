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

package org.gatein.api.content;

import org.gatein.api.Filter;
import org.gatein.api.Ids;
import org.gatein.api.IterableResult;
import org.gatein.api.Portal;
import org.gatein.api.Query;
import org.gatein.api.id.Id;
import org.gatein.api.navigation.Page;
import org.gatein.api.navigation.Window;
import org.gatein.api.organization.Permissions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Comparator;
import java.util.List;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class ContentTestCase
{
   private Portal portal;

   @BeforeTest
   public void setUp()
   {
      // get portal from CDI?
   }

   @Test(enabled = false)
   public void getInexistentCategoryShouldReturnNull()
   {
      assert portal.getContentRegistry().getCategory("inexistent") == null;
   }

   @Test(enabled = false)
   public void getOrCreateInexistentCategoryShouldCreateANewCategory()
   {
      assert portal.getContentRegistry().getOrCreateCategory("inexistent") != null;
   }

   @Test(enabled = false)
   public void assigningContentToAWindowOnAPage()
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

      Page page;
      String pageName = "page";
      if (!portal.hasChild(pageName))
      {
         page = portal.createChild(pageName, Page.class);
         assert page != null;
      }
      else
      {
         page = portal.getChild(pageName, Page.class);
      }

      // an exception should be thrown if child already exists
      // todo: decide if the exception should be more specific or not
      try
      {
         page = portal.createChild(pageName, Page.class);
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }

      page.addPermission(Permissions.ADMINISTRATOR_ACCESS);
      page.addPermission(Permissions.ADMINISTRATOR_EDIT);

      Window window;
      String windowName = "window";
      if (!page.hasChild(windowName))
      {
         window = page.createChild(windowName, Window.class);
         assert window != null;
      }
      else
      {
         window = page.getChild(windowName, Window.class);
      }
      assert window != null;

      window.addPermission(Permissions.ADMINISTRATOR_ACCESS);

      ContentRegistry registry = portal.getContentRegistry();

      Id<Application> applicationId = Ids.applicationId("application", "portlet");
      window.setContent(applicationId);

      Application application = registry.getContent(applicationId);
      assert application != null;
      assert application.equals(window.getContent());

      String title = "title";
      window.setTitle(title);
      assert title.equals(window.getTitle());

      window.setProperty(Window.SHOW_INFO_BAR, false);
      assert !window.getProperty(Window.SHOW_INFO_BAR);
   }

   @Test(enabled = false)
   public void assigningContentToACategory()
   {
      ContentRegistry registry = portal.getContentRegistry();
      final Category category = registry.getOrCreateCategory("category");

      Id<Application> id = Ids.applicationId("application", "portlet");
      List<Id<? extends ManagedContent>> knownContentIds = category.getKnownManagedContentIds();
      assert !knownContentIds.contains(id) : "A category doesn't contain content directly.";
      ManagedContent<Application> managed = category.addContent(id);
      assert managed != null;
      assert category.contains(managed.getId());
      assert knownContentIds.contains(managed.getId());

      Id<Content> wsrp = Ids.wsrpPortletId("invoker", "portlet");
      ManagedContent<? extends Content> managedWSRP = category.addContent(wsrp);
      assert managedWSRP != null;
      assert category.contains(managedWSRP.getId());
      knownContentIds = category.getKnownManagedContentIds();
      assert !knownContentIds.contains(wsrp) : "A category doesn't contain content directly.";
   }

   @Test(enabled = false)
   public void assigningAnApplicationToACategory()
   {
      ContentRegistry registry = portal.getContentRegistry();
      final Category category = registry.getOrCreateCategory("category");

      Application application = registry.getDeployedApplication(Ids.applicationId("application", "portlet"));
      assert application.getName().equals(application.getDisplayName());

      Id<Application> id = application.getId();
      ManagedContent<Application> managed = category.addContent(id);
      assert managed != null;
      assert application.equals(managed.getContent());
      assert managed.equals(category.getContent(id));

      IterableResult<ManagedContent> managedContents = registry.getManagedContents(Query.<ManagedContent>builder().where(new Filter<ManagedContent>()
      {
         @Override
         public boolean accept(ManagedContent item)
         {
            return item.getContent() instanceof Application;
         }
      }).orderBy(new Comparator<ManagedContent>()
      {
         public int compare(ManagedContent o1, ManagedContent o2)
         {
            return o1.getId().compareTo(o2.getId());
         }
      }).build());

      assert 1 == managedContents.size();
      for (ManagedContent managedContent : managedContents)
      {
         assert managed.equals(managedContent);
      }

      managed.setDisplayName("displayName");
      assert "displayName".equals(managed.getDisplayName());
   }
}
