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
import org.gatein.api.GateIn;
import org.gatein.api.IterableResult;
import org.gatein.api.Query;
import org.gatein.api.id.Id;
import org.gatein.api.portal.Portal;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public abstract class ContentTestCase
{
   protected Portal portal;
   protected GateIn gateIn;

   @BeforeTest
   public abstract void setUp();

   @Test
   public void getInexistentCategoryShouldReturnNull()
   {
      assert portal.getContentRegistry().getCategory("inexistent") == null;
   }

   @Test
   public void getOrCreateInexistentCategoryShouldCreateANewCategory()
   {
      assert portal.getContentRegistry().getOrCreateCategory("inexistent") != null;
   }

   @Test
   public void assigningContentToACategory()
   {
      ContentRegistry registry = portal.getContentRegistry();
      final Category category = registry.getOrCreateCategory("category");

      Id<Application> id = gateIn.applicationId("application", "portlet");
      List<Id<? extends ManagedContent>> knownContentIds = category.getKnownManagedContentIds();
      assert !knownContentIds.contains(id) : "A category doesn't contain content directly.";
      ManagedContent<Application> managed = category.addContent(id);
      assert managed != null;
      assert category.contains(managed.getId());
      assert knownContentIds.contains(managed.getId());

      Id<Content> wsrp = gateIn.wsrpPortletId("invoker", "portlet");
      ManagedContent<? extends Content> managedWSRP = category.addContent(wsrp);
      assert managedWSRP != null;
      assert category.contains(managedWSRP.getId());
      knownContentIds = category.getKnownManagedContentIds();
      assert !knownContentIds.contains(wsrp) : "A category doesn't contain content directly.";
   }

   @Test
   public void assigningAnApplicationToACategory()
   {
      ContentRegistry registry = portal.getContentRegistry();
      final Category category = registry.getOrCreateCategory("category");

      Application application = registry.getContent(gateIn.applicationId("application", "portlet"));
      assert application.getName().equals(application.getDisplayName());

      Id<Application> id = application.getId();
      ManagedContent<Application> managed = category.addContent(id);
      assert managed != null;
      assert application.equals(managed.getContent());
      assert managed.equals(category.getContent(managed.getId()));

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

   @Test
   public void assigningAGadgetToACategory() throws URISyntaxException, MalformedURLException
   {
      ContentRegistry registry = portal.getContentRegistry();
      final Category category = registry.getOrCreateCategory("category");

      // from name
      Id<Gadget> gadgetId = gateIn.gadgetId("gadgetName");
      Gadget gadget = registry.getContent(gadgetId);
      assert "gadgetName".equals(gadget.getName());

      ManagedContent<Gadget> managedContent = category.addContent(gadgetId);
      assert managedContent != null;
      assert gadget.equals(managedContent.getContent());
      assert managedContent.equals(category.getContent(managedContent.getId()));

      // from URL
      gadgetId = gateIn.gadgetId(new URI("http://foo.bar.com/gadget.xml"));
      gadget = registry.getContent(gadgetId);

      managedContent = category.addContent(gadgetId);
      assert managedContent != null;
      assert gadget.equals(managedContent.getContent());
      assert managedContent.equals(category.getContent(managedContent.getId()));
   }

   @Test
   public void creatingAGadgetFromSource()
   {
      ContentRegistry registry = portal.getContentRegistry();

      Gadget gadget = registry.createGadget("gadget", "source");
      assert gateIn.gadgetId("gadget").equals(gadget.getId());
      assert "source".equals(gadget.getSource());

      assert gadget.getEditURL() != null;
      assert gadget.getViewURL() != null;
      assert gadget.getReferenceURL() != null;

      assert gadget.equals(registry.getContent(gadget.getId()));
   }

   @Test
   public void removeContentFromCategory()
   {
      ContentRegistry registry = portal.getContentRegistry();
      Category category = registry.getOrCreateCategory("category");

      Id<Application> id = gateIn.applicationId("application", "portlet");
      Application application = registry.getContent(id);
      ManagedContent<Application> managed = category.addContent(id);
      assert category.contains(managed.getId());

      category.removeContent(managed.getId());
      assert !category.contains(managed.getId());
   }
}
