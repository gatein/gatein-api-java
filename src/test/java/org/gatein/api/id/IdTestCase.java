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

package org.gatein.api.id;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class IdTestCase
{
   private static final String CONTAINER_COMPONENT = "containerComponent";
   private static final String PORTAL_COMPONENT = "portalComponent";
   private static final String INVOKER_COMPONENT = "invokerComponent";
   private static final String PORTLET_COMPONENT = "portletComponent";
   private static final String INSTANCE_COMPONENT = "instanceComponent";
   private Context context;

   private static final String CONTAINER = "container";
   private static final String PORTAL = "portal";
   private static final String INVOKER = "invoker";
   private static final String PORTLET = "portlet";
   private static final String INSTANCE = "fooInstance";

   @BeforeTest
   public void setUp()
   {
      List<Component> components = new ArrayList<Component>(5);
      components.add(new Component(CONTAINER_COMPONENT, Pattern.compile("container"), true));
      components.add(new Component(PORTAL_COMPONENT, Pattern.compile("portal"), true));
      components.add(new Component(INVOKER_COMPONENT, Pattern.compile(".*"), false));
      components.add(new Component(PORTLET_COMPONENT, Pattern.compile(".*"), false));
      components.add(new Component(INSTANCE_COMPONENT, Pattern.compile(".*Instance$"), false));
      context = new Context("=", components, true);
   }

   @Test
   public void testRoundtripParsing()
   {
      Id key = Id.create(context, CONTAINER, PORTAL, INVOKER, PORTLET, INSTANCE);
      Id parsed = Id.parse(key.getOriginalContext(), key.toString());
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getComponent(CONTAINER_COMPONENT));
      assert PORTAL.equals(key.getComponent(PORTAL_COMPONENT));
      assert INVOKER.equals(key.getComponent(INVOKER_COMPONENT));
      assert PORTLET.equals(key.getComponent(PORTLET_COMPONENT));
      assert INSTANCE.equals(key.getComponent(INSTANCE_COMPONENT));

      key = Id.create(context, CONTAINER, PORTAL, INVOKER);
      parsed = Id.parse(key.getOriginalContext(), key.toString());
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getComponent(CONTAINER_COMPONENT));
      assert PORTAL.equals(key.getComponent(PORTAL_COMPONENT));
      assert INVOKER.equals(parsed.getComponent(INVOKER_COMPONENT));
      assert parsed.getComponent(PORTLET_COMPONENT) == null;
      assert parsed.getComponent(INSTANCE_COMPONENT) == null;

      key = Id.create(context, CONTAINER, PORTAL);
      parsed = Id.parse(key.getOriginalContext(), key.toString());
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getComponent(CONTAINER_COMPONENT));
      assert PORTAL.equals(key.getComponent(PORTAL_COMPONENT));
      assert parsed.getComponent(INVOKER_COMPONENT) == null;
      assert parsed.getComponent(PORTLET_COMPONENT) == null;
      assert parsed.getComponent(INSTANCE_COMPONENT) == null;

      key = Id.create(context, CONTAINER, PORTAL, null, PORTLET);
      parsed = Id.parse(key.getOriginalContext(), key.toString());
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getComponent(CONTAINER_COMPONENT));
      assert PORTAL.equals(key.getComponent(PORTAL_COMPONENT));
      assert parsed.getComponent(INVOKER_COMPONENT) == null;
      assert parsed.getComponent(PORTLET_COMPONENT) == null;
      assert parsed.getComponent(INSTANCE_COMPONENT) == null;

      key = Id.create(context, CONTAINER, PORTAL, INVOKER, null, INSTANCE);
      parsed = Id.parse(key.getOriginalContext(), key.toString());
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getComponent(CONTAINER_COMPONENT));
      assert PORTAL.equals(key.getComponent(PORTAL_COMPONENT));
      assert INVOKER.equals(parsed.getComponent(INVOKER_COMPONENT));
      assert parsed.getComponent(PORTLET_COMPONENT) == null;
      assert parsed.getComponent(INSTANCE_COMPONENT) == null;
   }

   @Test
   public void testRootComponent()
   {
      List<Component> components = new ArrayList<Component>(1);
      components.add(new Component(CONTAINER_COMPONENT, Pattern.compile("container"), true));

      Id key = Id.create(new Context("-", components, true), CONTAINER);
      assert CONTAINER.equals(key.getRootComponent());

      key = Id.create(context, CONTAINER, PORTAL, INVOKER, PORTLET, INSTANCE);
      assert CONTAINER.equals(key.getRootComponent());
   }

   @Test
   public void testPortletNameWithSlash()
   {
      Id key = Id.create(context, CONTAINER, PORTAL, INVOKER, "category/portlet");
      Id parsed = Id.parse(key.getOriginalContext(), key.toString());
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getComponent(CONTAINER_COMPONENT));
      assert PORTAL.equals(key.getComponent(PORTAL_COMPONENT));
      assert INVOKER.equals(parsed.getComponent(INVOKER_COMPONENT));
      assert "category/portlet".equals(parsed.getComponent(PORTLET_COMPONENT));
   }

   @Test
   public void shouldNotSetOptionalComponents()
   {
      assert Id.create(context, CONTAINER, PORTAL, INVOKER, null).equals(Id.create(context, CONTAINER, PORTAL, INVOKER));
   }

   @Test
   public void testGetChildFor()
   {
      Id key = Id.create(context, CONTAINER, PORTAL);
      Id child = Id.getIdForChild(key, INVOKER);
      assert Id.create(context, CONTAINER, PORTAL, INVOKER, null).equals(child);

      child = Id.getIdForChild(child, PORTLET);
      assert Id.create(context, CONTAINER, PORTAL, INVOKER, PORTLET).equals(child);

      child = Id.getIdForChild(child, INSTANCE);
      assert Id.create(context, CONTAINER, PORTAL, INVOKER, PORTLET, INSTANCE).equals(child);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testAnIdShouldAlwaysHaveAPortalKey()
   {
      Id.create(context, null, null);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testGetChildForShouldFailOnNullParent()
   {
      Id.getIdForChild(null, null);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testGetChildForShouldFailOnNullChildId()
   {
      Id.getIdForChild(Id.create(context, CONTAINER, PORTAL), null);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void anIdShouldAlwaysHaveARoot()
   {
      Id.create(new Context("-", Collections.<Component>emptyList(), false), null);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void shouldNotBePossibleToCreateAnIdWithoutAContext()
   {
      Id.create(null, null);
   }


   @Test
   public void testPortletIdsScenarios()
   {
      Id id = Id.create(context, "container", "portal");
      assert "container".equals(id.getComponent(CONTAINER_COMPONENT));
      assert "portal".equals(id.getComponent(PORTAL_COMPONENT));
      Assert.assertEquals(id.toString(context), "container=portal");

      try
      {
         Id.create(context, null);
         Assert.fail("Should have failed as " + CONTAINER_COMPONENT + " is required");
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }

      try
      {
         Id.create(context, "foo");
         Assert.fail("Should have failed as only 'container' is allowed as value for " + CONTAINER_COMPONENT);
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }

      try
      {
         Id.create(context, "container");
         Assert.fail("Should have failed as portalComponent is required");
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }

      try
      {
         Id.create(context, "container", "goo");
         Assert.fail("Should have failed as only 'portal' is allowed as value for portalComponent");
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }
   }
}
