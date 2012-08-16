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

import org.gatein.api.portal.page.Page;
import org.gatein.api.portal.site.Site;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.gatein.api.portal.Ids.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class IdsTest
{
   @Test
   public void testSiteId_Format()
   {
      Site.Id id = siteId("foo");
      assertEquals("site || foo", Ids.format(id, "%s || %s", false));

      id = siteId(new Group("foo", "bar"));
      assertEquals("space || /foo/bar", Ids.format(id, "%s || %s", false));

      id = siteId(new User("foo"));
      assertEquals("dashboard || foo", Ids.format(id, "%s || %s", false));
   }

   @Test
   public void testSiteId_Form_UrlSafe()
   {
      // unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
      Pattern urlUnreserved = Pattern.compile("[0-9A-Za-z-\\._~]*");
      String format = Ids.format(siteId(new Group("foo", "bar")));

      assertTrue(urlUnreserved.matcher(format).matches());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSiteId_Format_Null()
   {
      Ids.format(siteId("foo"), null, true);
   }

   @Test
   public void testSiteId_FromString()
   {
      Site.Id id = siteId("foo");
      assertEquals(id, Ids.fromString(Site.Id.class, Ids.format(id)));

      id = siteId(new Group("foo", "bar"));
      assertEquals(id, Ids.fromString(Site.Id.class, Ids.format(id)));

      id = siteId(new User("foo"));
      assertEquals(id, Ids.fromString(Site.Id.class, Ids.format(id)));
   }

   @Test
   public void testPageId_Format()
   {
      Page.Id id = pageId("foo", "bar");
      assertEquals("site || foo || bar", format(id, "%s || %s || %s", false));

      id = pageId(new Group("foo", "bar"), "bar");
      assertEquals("space || /foo/bar || bar", format(id, "%s || %s || %s", false));

      id = pageId(new User("foo"), "bar");
      assertEquals(format(id, "%s || %s || %s", false), "dashboard || foo || bar");
   }

   @Test
   public void testPageId_Format_UrlSafe()
   {
      // unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
      Pattern urlUnreserved = Pattern.compile("[0-9A-Za-z-\\._~]*");
      String format = format(pageId(new Group("platform", "administrators"), "pageManagement"));

      assertTrue(urlUnreserved.matcher(format).matches());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testPageId_Format_Null()
   {
      format(new Page.Id(siteId("foo"), "bar"), null, true);
   }

   @Test
   public void testPageId_FromString()
   {
      Site.Id siteId = siteId("foo");
      Page.Id id = new Page.Id(siteId, "bar");
      assertEquals(id, Ids.fromString(Page.Id.class, Ids.format(id)));

      id = new Page.Id(siteId, "bar_baz");
      assertEquals(id, Ids.fromString(Page.Id.class, Ids.format(id)));

      id = new Page.Id(siteId, "bar-baz");
      assertEquals(id, Ids.fromString(Page.Id.class, Ids.format(id)));
   }
}
