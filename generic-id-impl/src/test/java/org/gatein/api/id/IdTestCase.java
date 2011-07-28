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

import org.gatein.api.GateIn;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class IdTestCase
{
   private static final String CONTAINER_COMPONENT_NAME = "containerComponent";
   private static final String PORTAL_COMPONENT_NAME = "portalComponent";
   private static final String INVOKER_COMPONENT_NAME = "invokerComponent";
   private static final String PORTLET_COMPONENT_NAME = "portletComponent";
   private static final String INSTANCE_COMPONENT_NAME = "instanceComponent";

   private Context context = GenericContext.builder().withDefaultSeparator("=")
      .requiredComponent(CONTAINER_COMPONENT_NAME, Identifiable.class, Pattern.compile("container"))
      .requiredComponent(PORTAL_COMPONENT_NAME, Identifiable.class, Pattern.compile("portal"))
      .optionalComponent(INVOKER_COMPONENT_NAME, Identifiable.class, Pattern.compile(".*"))
      .optionalComponent(PORTLET_COMPONENT_NAME, Identifiable.class, Pattern.compile(".*"))
      .optionalComponent(INSTANCE_COMPONENT_NAME, Identifiable.class, Pattern.compile(".*Instance$"))
      .ignoreRemainingAfterFirstMissingOptional().build();

   private static final String CONTAINER = "container";
   private static final String PORTAL = "portal";
   private static final String INVOKER = "invoker";
   private static final String PORTLET = "portlet";
   private static final String INSTANCE = "fooInstance";

   @Test
   public void testRoundtripParsing()
   {
      Id key = context.create(CONTAINER, PORTAL, INVOKER, PORTLET, INSTANCE);
      Id parsed = context.parse(key.toString());
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getComponent(CONTAINER_COMPONENT_NAME));
      assert PORTAL.equals(key.getComponent(PORTAL_COMPONENT_NAME));
      assert INVOKER.equals(key.getComponent(INVOKER_COMPONENT_NAME));
      assert PORTLET.equals(key.getComponent(PORTLET_COMPONENT_NAME));
      assert INSTANCE.equals(key.getComponent(INSTANCE_COMPONENT_NAME));

      key = context.create(CONTAINER, PORTAL, INVOKER);
      parsed = context.parse(key.toString());
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getComponent(CONTAINER_COMPONENT_NAME));
      assert PORTAL.equals(key.getComponent(PORTAL_COMPONENT_NAME));
      assert INVOKER.equals(parsed.getComponent(INVOKER_COMPONENT_NAME));
      assert parsed.getComponent(PORTLET_COMPONENT_NAME) == null;
      assert parsed.getComponent(INSTANCE_COMPONENT_NAME) == null;

      key = context.create(CONTAINER, PORTAL);
      parsed = context.parse(key.toString());
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getComponent(CONTAINER_COMPONENT_NAME));
      assert PORTAL.equals(key.getComponent(PORTAL_COMPONENT_NAME));
      assert parsed.getComponent(INVOKER_COMPONENT_NAME) == null;
      assert parsed.getComponent(PORTLET_COMPONENT_NAME) == null;
      assert parsed.getComponent(INSTANCE_COMPONENT_NAME) == null;

      key = context.create(CONTAINER, PORTAL, null, PORTLET);
      parsed = context.parse(key.toString());
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getComponent(CONTAINER_COMPONENT_NAME));
      assert PORTAL.equals(key.getComponent(PORTAL_COMPONENT_NAME));
      assert parsed.getComponent(INVOKER_COMPONENT_NAME) == null;
      assert parsed.getComponent(PORTLET_COMPONENT_NAME) == null;
      assert parsed.getComponent(INSTANCE_COMPONENT_NAME) == null;

      key = context.create(CONTAINER, PORTAL, INVOKER, null, INSTANCE);
      parsed = context.parse(key.toString());
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getComponent(CONTAINER_COMPONENT_NAME));
      assert PORTAL.equals(key.getComponent(PORTAL_COMPONENT_NAME));
      assert INVOKER.equals(parsed.getComponent(INVOKER_COMPONENT_NAME));
      assert parsed.getComponent(PORTLET_COMPONENT_NAME) == null;
      assert parsed.getComponent(INSTANCE_COMPONENT_NAME) == null;
   }

   @Test
   public void testRoundtripParsingWithRequiredFirstSeparator()
   {
      final Context context = GenericContext.builder().withDefaultSeparator("/")
         .requiredUnboundedHierarchicalComponent("foo", Identifiable.class, Pattern.compile("\\w+"))
         .requireSeparatorInFirstPosition()
         .build();

      Id key = context.create("foo", "bar", "baz");
      Id parsed = context.parse(key.toString());
      assert key.equals(parsed);
   }

   @Test
   public void getParentShouldWork()
   {
      Id portal = context.create(CONTAINER, PORTAL);

      Id invoker = context.create(CONTAINER, PORTAL, INVOKER);
      assert portal.equals(invoker.getParent());

      Id portlet = context.create(CONTAINER, PORTAL, INVOKER, PORTLET);
      assert invoker.equals(portlet.getParent());

      Id instance = context.create(CONTAINER, PORTAL, INVOKER, PORTLET, INSTANCE);
      assert portlet.equals(instance.getParent());
   }

   @Test
   public void testRoundtripParsingWithHierarchicalComponents()
   {
      Context groupContext = GenericContext.builder().withDefaultSeparator("/")
         .requiredUnboundedHierarchicalComponent("root", Identifiable.class, Pattern.compile("\\w*")).build();
      final Id id = groupContext.create("root", "1", "2", "3", "4");
      assert id.equals(groupContext.parse(id.toString()));

      Id parent = groupContext.create("root", "1", "2", "3");
      assert parent.equals(id.getParent());
   }

   @Test
   public void testRootComponent()
   {
      Id key = GenericContext.builder().withDefaultSeparator("-").requiredComponent(CONTAINER_COMPONENT_NAME, Identifiable.class, Pattern.compile("container")).build().create(CONTAINER);
      assert CONTAINER.equals(key.getRootComponent());
      assert key.getParent() == null;

      key = context.create(CONTAINER, PORTAL, INVOKER, PORTLET, INSTANCE);
      assert CONTAINER.equals(key.getRootComponent());
   }

   @Test
   public void testPortletNameWithSlash()
   {
      Id key = context.create(CONTAINER, PORTAL, INVOKER, "category/portlet");
      Id parsed = context.parse(key.toString());
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getComponent(CONTAINER_COMPONENT_NAME));
      assert PORTAL.equals(key.getComponent(PORTAL_COMPONENT_NAME));
      assert INVOKER.equals(parsed.getComponent(INVOKER_COMPONENT_NAME));
      assert "category/portlet".equals(parsed.getComponent(PORTLET_COMPONENT_NAME));
   }

   @Test
   public void shouldNotSetOptionalComponents()
   {
      assert context.create(CONTAINER, PORTAL, INVOKER, null).equals(context.create(CONTAINER, PORTAL, INVOKER));
   }

   @Test
   public void testGetChildFor()
   {
      Id key = context.create(CONTAINER, PORTAL);
      Id child = key.getIdForChild(INVOKER);
      assert context.create(CONTAINER, PORTAL, INVOKER, null).equals(child);

      child = child.getIdForChild(PORTLET);
      assert context.create(CONTAINER, PORTAL, INVOKER, PORTLET).equals(child);

      child = child.getIdForChild(INSTANCE);
      assert context.create(CONTAINER, PORTAL, INVOKER, PORTLET, INSTANCE).equals(child);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testAnIdShouldAlwaysHaveAPortalKey()
   {
      context.create(null);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testGetChildForShouldFailOnNullChildId()
   {
      context.create(CONTAINER, PORTAL).getIdForChild(null);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void anIdShouldAlwaysHaveARoot()
   {
      GenericContext.builder().withDefaultSeparator("-").requiredComponent("foo", Identifiable.class, Pattern.compile(".*")).build().create(null);
   }

   @Test
   public void testPortletIdsScenarios()
   {
      Id id = context.create("container", "portal");
      assert "container".equals(id.getComponent(CONTAINER_COMPONENT_NAME));
      assert "portal".equals(id.getComponent(PORTAL_COMPONENT_NAME));
      Assert.assertEquals(id.toString(context), "container=portal");

      try
      {
         context.create(null);
         Assert.fail("Should have failed as " + CONTAINER_COMPONENT_NAME + " is required");
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }

      try
      {
         context.create("foo");
         Assert.fail("Should have failed as only 'container' is allowed as value for " + CONTAINER_COMPONENT_NAME);
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }

      try
      {
         context.create("container");
         Assert.fail("Should have failed as portalComponent is required");
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }

      try
      {
         context.create("container", "goo");
         Assert.fail("Should have failed as only 'portal' is allowed as value for portalComponent");
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }
   }

   @Test
   public void getIdentifiableTypeShouldReturnTheReifiedType()
   {
      Id<A> foo = A.context.create(A.class, "foo");
      assert A.class.equals(foo.getIdentifiableType());
   }

   private static class A implements Identifiable<A>
   {
      static final Context context = GenericContext.builder().requiredComponent("foo", A.class, Pattern.compile(".*")).build();

      public Id<A> getId()
      {
         return context.create(A.class, "foo");
      }

      public String getName()
      {
         return null;  //To change body of implemented methods use File | Settings | File Templates.
      }

      public String getDisplayName()
      {
         return null;  //To change body of implemented methods use File | Settings | File Templates.
      }

      public GateIn getGateIn()
      {
         return null;  //To change body of implemented methods use File | Settings | File Templates.
      }
   }
}
