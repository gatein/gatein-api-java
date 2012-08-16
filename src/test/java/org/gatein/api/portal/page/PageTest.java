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

import org.junit.Test;

import static org.gatein.api.portal.Ids.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class PageTest
{
   @Test
   public void testPageId_Equals()
   {
      Page.Id pageId1 = pageId("foo", "bar");
      Page.Id pageId2 = pageId("foo", "bar");

      assertTrue(pageId1.equals(pageId2));
      assertTrue(pageId2.equals(pageId1));
      assertNotSame(pageId1, pageId2);

      pageId1 = new Page.Id(siteId("bar"), "bar");
      assertFalse(pageId1.equals(pageId2));
      pageId2 = new Page.Id(siteId("bar"), "baz");
      assertFalse(pageId1.equals(pageId2));
   }
}
