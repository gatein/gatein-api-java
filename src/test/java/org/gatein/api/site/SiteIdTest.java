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

package org.gatein.api.site;

import org.gatein.api.security.Group;
import org.gatein.api.security.User;
import org.gatein.api.site.SiteId;
import org.junit.Test;

import java.util.Formatter;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class SiteIdTest
{
   @Test
   public void testEquals()
   {
      //site
      SiteId id1 = new SiteId("foo");
      SiteId id2 = new SiteId("foo");
      assertTrue(id1.equals(id2));
      assertTrue(id2.equals(id1));
      assertNotSame(id1, id2);
      id2 = new SiteId("bar");
      assertFalse(id1.equals(id2));

      //space
      id1 = new SiteId(new Group("foo", "bar"));
      id2 = new SiteId(new Group("foo", "bar"));
      assertTrue(id1.equals(id2));
      assertTrue(id2.equals(id1));
      assertNotSame(id1, id2);
      id2 = new SiteId(new Group("foo", "baz"));
      assertFalse(id1.equals(id2));

      // dashboard
      id1 = new SiteId(new User("foo"));
      id2 = new SiteId(new User("foo"));
      assertTrue(id1.equals(id2));
      assertTrue(id2.equals(id1));
      assertNotSame(id1, id2);
      id2 = new SiteId(new User("bar"));
      assertFalse(id1.equals(id2));

      // mix site types
      id1 = new SiteId("foo");
      id2 = new SiteId(new Group("foo"));
      assertFalse(id1.equals(id2));
      id2 = new SiteId(new User("foo"));
      assertFalse(id1.equals(id2));
      id1 = new SiteId("foo");
      assertFalse(id1.equals(id2));
   }

   @Test
   public void testFormat()
   {
      SiteId id = new SiteId("foo");
      assertEquals("type=site, name=foo", new Formatter().format("%s", id).toString());

      id = new SiteId(new Group("foo", "bar"));
      assertEquals("type=space, name=/foo/bar", new Formatter().format("%s", id).toString());

      id = new SiteId(new User("foo"));
      assertEquals("type=dashboard, name=foo", new Formatter().format("%s", id).toString());
   }

   @Test
   public void testFormat_UrlSafe()
   {
      // unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
      Pattern urlUnreserved = Pattern.compile("[0-9A-Za-z-\\._~]*");
      SiteId siteId = new SiteId(new Group("foo", "bar"));

      assertTrue(urlUnreserved.matcher(new Formatter().format("%#s", siteId).toString()).matches());
   }

   @Test
   public void testFromString()
   {
      SiteId id = new SiteId("foo-_site0");
      assertEquals(id, SiteId.fromString(id.toString()));
      assertEquals(id, SiteId.fromString(new Formatter().format("%s", id).toString()));
      assertEquals(id, SiteId.fromString(new Formatter().format("%#s", id).toString()));

      id = new SiteId(new Group("foo", "bar"));
      assertEquals(id, SiteId.fromString(id.toString()));
      assertEquals(id, SiteId.fromString(new Formatter().format("%s", id).toString()));
      assertEquals(id, SiteId.fromString(new Formatter().format("%#s", id).toString()));

      id = new SiteId(new User("foo"));
      assertEquals(id, SiteId.fromString(id.toString()));
      assertEquals(id, SiteId.fromString(new Formatter().format("%s", id).toString()));
      assertEquals(id, SiteId.fromString(new Formatter().format("%#s", id).toString()));
   }
}
