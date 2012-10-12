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

package org.gatein.api.portal.page;

import org.gatein.api.portal.Group;
import org.gatein.api.portal.User;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class PageIdTest
{
   @Test
   public void testEquals()
   {
      PageId pageId1 = new PageId("foo", "bar");
      PageId pageId2 = new PageId("foo", "bar");

      assertTrue(pageId1.equals(pageId2));
      assertTrue(pageId2.equals(pageId1));
      assertNotSame(pageId1, pageId2);

      pageId1 = new PageId(new Group("foo", "bar"), "baz");
      assertFalse(pageId1.equals(pageId2));
      pageId2 = new PageId(new Group("foo"), "baz");
      assertFalse(pageId1.equals(pageId2));
      pageId2 = new PageId(new Group("foo", "bar"), "baz");
      assertTrue(pageId1.equals(pageId2));

      pageId1 = new PageId(new User("foo"), "baz");
      assertFalse(pageId1.equals(pageId2));
      pageId2 = new PageId(new User("bar"), "baz");
      assertFalse(pageId1.equals(pageId2));
      pageId2 = new PageId(new User("foo"), "baz");
      assertTrue(pageId1.equals(pageId2));
   }
   @Test
   public void testFormat()
   {
      PageId id = new PageId("foo", "bar");
      assertEquals("site || foo || bar", id.format("%s || %s || %s"));

      id = new PageId(new Group("foo", "bar"), "bar");
      assertEquals("space || /foo/bar || bar", id.format("%s || %s || %s"));

      id = new PageId(new User("foo"), "bar");
      assertEquals(id.format("%s || %s || %s"), "dashboard || foo || bar");
   }

   @Test
   public void testFormat_UrlSafe()
   {
      // unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
      Pattern urlUnreserved = Pattern.compile("[0-9A-Za-z-\\._~]*");
      String format = new PageId(new Group("platform", "administrators"), "pageManagement").format();

      assertTrue(urlUnreserved.matcher(format).matches());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testFormat_Null()
   {
      new PageId("foo", "bar").format(null);
   }

   @Test
   public void testFromString()
   {
      PageId id = new PageId("foo", "bar");
      assertEquals(id, PageId.fromString(id.format()));

      id = new PageId(new Group("foo", "bar"), "bar_baz");
      assertEquals(id, PageId.fromString(id.format()));

      id = new PageId(new User("foo"), "bar_baz");
      assertEquals(id, PageId.fromString(id.format()));
   }
}
