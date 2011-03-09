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

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class IdTestCase
{
   private static final String CONTAINER = "container";
   private static final String PORTAL = "portal";
   private static final String INVOKER = "invoker";
   private static final String PORTLET = "portlet";
   private static final String INSTANCE = "instance";

   @Test
   public void testRoundtripParsing()
   {
      Id key = new Id(CONTAINER, PORTAL, INVOKER, PORTLET, INSTANCE);
      Id parsed = Id.parse(Id.asString(key));
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getContainerId());
      assert PORTAL.equals(key.getPortalId());
      assert INVOKER.equals(key.getInvokerId());
      assert PORTLET.equals(key.getPortletId());
      assert INSTANCE.equals(key.getInstanceId());

      key = new Id(CONTAINER, PORTAL, INVOKER);
      parsed = Id.parse(Id.asString(key));
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getContainerId());
      assert PORTAL.equals(key.getPortalId());
      assert INVOKER.equals(parsed.getInvokerId());
      assert parsed.getPortletId() == null;
      assert parsed.getInstanceId() == null;

      key = new Id(CONTAINER, PORTAL);
      parsed = Id.parse(Id.asString(key));
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getContainerId());
      assert PORTAL.equals(key.getPortalId());
      assert parsed.getInvokerId() == null;
      assert parsed.getPortletId() == null;
      assert parsed.getInstanceId() == null;

      key = new Id(CONTAINER, PORTAL, null, PORTLET);
      parsed = Id.parse(Id.asString(key));
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getContainerId());
      assert PORTAL.equals(key.getPortalId());
      assert parsed.getInvokerId() == null;
      assert parsed.getPortletId() == null;
      assert parsed.getInstanceId() == null;

      key = new Id(CONTAINER, PORTAL, INVOKER, null, INSTANCE);
      parsed = Id.parse(Id.asString(key));
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getContainerId());
      assert PORTAL.equals(key.getPortalId());
      assert INVOKER.equals(parsed.getInvokerId());
      assert parsed.getPortletId() == null;
      assert parsed.getInstanceId() == null;
   }

   @Test
   public void testPortletNameWithSlash()
   {
      Id key = new Id(CONTAINER, PORTAL, INVOKER, "category/portlet");
      Id parsed = Id.parse(Id.asString(key));
      assert key.equals(parsed);
      assert CONTAINER.equals(key.getContainerId());
      assert PORTAL.equals(key.getPortalId());
      assert INVOKER.equals(parsed.getInvokerId());
      assert "category/portlet".equals(parsed.getPortletId());
   }

   @Test
   public void testGetChildFor()
   {
      Id key = new Id(CONTAINER, PORTAL);
      Id child = Id.getIdForChild(key, INVOKER);
      assert new Id(CONTAINER, PORTAL, INVOKER, null).equals(child);

      child = Id.getIdForChild(child, PORTLET);
      assert new Id(CONTAINER, PORTAL, INVOKER, PORTLET).equals(child);

      child = Id.getIdForChild(child, INSTANCE);
      assert new Id(CONTAINER, PORTAL, INVOKER, PORTLET, INSTANCE).equals(child);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testAIdShouldAlwaysHaveAPortalKey()
   {
      new Id(null, null);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testGetChildForShouldFailOnNullParent()
   {
      Id.getIdForChild(null, null);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testGetChildForShouldFailOnNullChildId()
   {
      Id.getIdForChild(new Id(CONTAINER, PORTAL), null);
   }
}
