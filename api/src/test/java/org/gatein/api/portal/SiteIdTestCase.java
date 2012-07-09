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
public class SiteIdTestCase
{
   @Test
   public void siteId_Equals()
   {
      //site
      Site.Id id1 = Site.Id.site("foo");
      Site.Id id2 = Site.Id.site("foo");
      assertTrue(id1.equals(id2));
      assertTrue(id2.equals(id1));
      assertNotSame(id1, id2);
      id2 = Site.Id.site("bar");
      assertFalse(id1.equals(id2));

      //space
      id1 = Site.Id.space("foo", "bar");
      id2 = Site.Id.space("foo", "bar");
      assertTrue(id1.equals(id2));
      assertTrue(id2.equals(id1));
      assertNotSame(id1, id2);
      id2 = Site.Id.space("foo", "baz");
      assertFalse(id1.equals(id2));

      // dashboard
      id1 = Site.Id.dashboard("foo");
      id2 = Site.Id.dashboard("foo");
      assertTrue(id1.equals(id2));
      assertTrue(id2.equals(id1));
      assertNotSame(id1, id2);
      id2 = Site.Id.dashboard("bar");
      assertFalse(id1.equals(id2));

      // mix site types
      id1 = Site.Id.site("foo");
      id2 = Site.Id.space("foo");
      assertFalse(id1.equals(id2));
      id2 = Site.Id.dashboard("foo");
      assertFalse(id1.equals(id2));
      id1 = Site.Id.space("foo");
      assertFalse(id1.equals(id2));
   }

   @Test
   public void siteId_Base64()
   {
      Site.Id id1 = Site.Id.site("foo");
      String base64 = id1.toBase64String();
      Site.Id id2 = Site.Id.fromBase64String(base64);
      assertTrue(id1.equals(id2));
      assertTrue(id2.equals(id1));
      assertNotSame(id1, id2);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void siteId_InvalidBase64()
   {
      Site.Id.fromBase64String(Node.Id.create(Site.Id.site("foo"), "some", "path").toBase64String());
   }
}
