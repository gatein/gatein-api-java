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
public class PageIdTestCase
{
   @Test
   public void pageId_Equals()
   {
      Site.Id siteId = Site.Id.site("foo");
      Page.Id pageId1 = Page.Id.create(siteId, "bar");
      Page.Id pageId2 = Page.Id.create(siteId, "bar");

      assertTrue(pageId1.equals(pageId2));
      assertTrue(pageId2.equals(pageId1));
      assertNotSame(pageId1, pageId2);

      pageId1 = Page.Id.create(Site.Id.site("bar"), "bar");
      assertFalse(pageId1.equals(pageId2));
      pageId2 = Page.Id.create(Site.Id.site("bar"), "baz");
      assertFalse(pageId1.equals(pageId2));
   }

   @Test
   public void pageId_Base64()
   {
      Site.Id siteId = Site.Id.site("foo");
      Page.Id pageId = Page.Id.create(siteId, "bar");
      String base64 = pageId.toBase64String();

      assertEquals(Page.Id.fromBase64String(base64), pageId);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void pageId_InvalidBase64()
   {
      Page.Id.fromBase64String(Site.Id.site("foo").toBase64String());
   }
}
