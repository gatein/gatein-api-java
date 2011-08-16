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

import org.gatein.api.GateIn;
import org.gatein.api.id.Id;
import org.gatein.api.portal.Portal;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

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

      Portlet.Id id = gateIn.portletId("application", "portlet");
      final Portlet portlet = (Portlet)registry.get(id);

      final ManagedContent managedContent = category.addContent(id, portlet.getName());
      assert managedContent != null;
      assert portlet.equals(managedContent.getContent());

      final String name = managedContent.getName();
      assert managedContent.equals(category.getManagedContent(name));
      assert category.contains(name);
      assert category.getKnownManagedContentNames().contains(name);

      WSRP.Id wsrp = gateIn.wsrpPortletId("invoker", "portlet");
      ManagedContent managedWSRP = category.addContent(wsrp, "foo");
      assert managedWSRP != null;
      assert category.contains(managedWSRP.getName());
   }

   @Test
   public void assigningAnApplicationToACategory()
   {
      ContentRegistry registry = portal.getContentRegistry();
      final Category category = registry.getOrCreateCategory("category");

      Portlet portlet = (Portlet)registry.get(gateIn.portletId("application", "portlet"));
      assert portlet.getName().equals(portlet.getDisplayName());

      Content.Id id = portlet.getId();
      ManagedContent managed = category.addContent(id, portlet.getName());
      assert managed != null;
      assert portlet.equals(managed.getContent());
      assert managed.equals(category.getManagedContent(managed.getName()));

      /*IterableIdentifiableCollection<ManagedContent> managedContents = registry.getManagedContents(Query.<ManagedContent>builder().where(new Filter<ManagedContent>()
      {
         @Override
         public boolean accept(ManagedContent item)
         {
            return item.getContent() instanceof Portlet;
         }
      }).orderBy(new Comparator<ManagedContent>()
      {
         public int compare(ManagedContent o1, ManagedContent o2)
         {
            return o1.getName().compareTo(o2.getName());
         }
      }).build());

      assert 1 == managedContents.size();
      for (ManagedContent managedContent : managedContents)
      {
         assert managed.equals(managedContent);
      }

      managed.setDisplayName("displayName");
      assert "displayName".equals(managed.getDisplayName());*/
   }

   @Test
   public void assigningAGadgetToACategory() throws URISyntaxException, MalformedURLException
   {
      ContentRegistry registry = portal.getContentRegistry();
      final Category category = registry.getOrCreateCategory("category");

      // from name
      Content.Id gadgetId = gateIn.gadgetId("gadgetName");
      Gadget gadget = (Gadget)registry.get(gadgetId);
      assert "gadgetName".equals(gadget.getName());

      ManagedContent managedContent = category.addContent(gadgetId, gadget.getName());
      assert managedContent != null;
      assert gadget.equals(managedContent.getContent());
      assert managedContent.equals(category.getManagedContent(managedContent.getName()));

      // from URL
      URI uri = new URI("http://foo.bar.com/gadget.xml");
      gadgetId = gateIn.gadgetId(uri);
      gadget = (Gadget)registry.get(gadgetId);
      assert !gadget.isLocal();
      assert uri.equals(gadget.getURI());
      assert uri.equals(((Gadget.RemoteData)gadget.getData()).getURI());

      managedContent = category.addContent(gadgetId, gadget.getName());
      assert managedContent != null;
      assert gadget.equals(managedContent.getContent());
      assert managedContent.equals(category.getManagedContent(managedContent.getName()));
   }

   @Test
   public void creatingAGadgetFromSource()
   {
      ContentRegistry registry = portal.getContentRegistry();

      Gadget gadget = registry.createGadget("gadget", "source");
      assert gateIn.gadgetId("gadget").equals(gadget.getId());
      assert gadget.getURI() != null;
      assert gadget.isLocal();
      assert gadget.getData() instanceof Gadget.LocalData;
      assert "source".equals(((Gadget.LocalData)gadget.getData()).getSource());

      assert gadget.getReferenceURI() != null;

      assert gadget.equals(registry.get(gadget.getId()));
   }

   @Test
   public void removeContentFromCategory()
   {
      ContentRegistry registry = portal.getContentRegistry();
      Category category = registry.getOrCreateCategory("category");

      Portlet.Id id = gateIn.portletId("application", "portlet");
      Portlet portlet = (Portlet)registry.get(id);
      ManagedContent managed = category.addContent(id, portlet.getName());
      assert category.contains(managed.getName());

      category.removeContent(managed.getName());
      assert !category.contains(managed.getName());
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void addingContentToACategoryWithoutAValidNameShouldFail()
   {
      ContentRegistry registry = portal.getContentRegistry();
      Category category = registry.getOrCreateCategory("category");

      Portlet.Id id = gateIn.portletId("application", "portlet");
      category.addContent(id, null);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void addingContentToACategoryWithoutAValidIdShouldFail()
   {
      ContentRegistry registry = portal.getContentRegistry();
      Category category = registry.getOrCreateCategory("category");

      category.addContent(null, null);
   }
}
