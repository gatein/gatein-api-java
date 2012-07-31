/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class NodeIdTestCase
{
   @Test
   public void testEquals()
   {
      Site.Id siteId = Site.Id.site("foo");
      Node.Id nodeId1 = Node.Id.create(siteId, "foo", "bar", "baz");
      Node.Id nodeId2 = Node.Id.create(siteId, "foo", "bar", "baz");

      assertTrue(nodeId1.equals(nodeId2));
      assertTrue(nodeId2.equals(nodeId1));
      assertNotSame(nodeId1, nodeId2);

      nodeId1 = Node.Id.create(Site.Id.site("bar"), "bar");
      assertFalse(nodeId1.equals(nodeId2));
      nodeId2 = Node.Id.create(Site.Id.site("bar"), "baz");
      assertFalse(nodeId1.equals(nodeId2));
   }

   @Test
   public void testFormat()
   {
      Site.Id siteId = Site.Id.site("foo");
      Node.Id id = Node.Id.create(siteId, "bar", "baz");
      assertEquals("site || foo || /bar/baz", id.format("%s || %s || %s", false));

      siteId = Site.Id.space("foo", "bar");
      id = Node.Id.create(siteId, "bar", "baz");
      assertEquals("space || /foo/bar || /bar/baz", id.format("%s || %s || %s", false));

      siteId = Site.Id.dashboard("foo");
      id = Node.Id.create(siteId);
      assertEquals(id.format("%s || %s || %s", false), "dashboard || foo || /");
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testFormat_Null()
   {
      Node.Id.create(Site.Id.site("foo")).format(null, true);
   }

   @Test
   public void testFromString()
   {
      Site.Id siteId = Site.Id.site("foo");
      Node.Id id = Node.Id.create(siteId, "bar");
      assertEquals(id, Node.Id.fromString(id.format()));

      siteId = Site.Id.space("foo", "bar");
      id = Node.Id.create(siteId, "bar", "baz");
      assertEquals(id, Node.Id.fromString(id.format()));

      siteId = Site.Id.dashboard("foo");
      id = Node.Id.create(siteId);
      assertEquals(id, Node.Id.fromString(id.format()));
   }
}
