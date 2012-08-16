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

package org.gatein.api.portal.site;

import org.gatein.api.portal.Group;
import org.gatein.api.portal.User;
import org.junit.Test;

import static org.gatein.api.portal.Ids.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class SiteTest
{
   @Test
   public void testSiteId_Equals()
   {
      //site
      Site.Id id1 = siteId("foo");
      Site.Id id2 = siteId("foo");
      assertTrue(id1.equals(id2));
      assertTrue(id2.equals(id1));
      assertNotSame(id1, id2);
      id2 = siteId("bar");
      assertFalse(id1.equals(id2));

      //space
      id1 = siteId(new Group("foo", "bar"));
      id2 = siteId(new Group("foo", "bar"));
      assertTrue(id1.equals(id2));
      assertTrue(id2.equals(id1));
      assertNotSame(id1, id2);
      id2 = siteId(new Group("foo", "baz"));
      assertFalse(id1.equals(id2));

      // dashboard
      id1 = siteId(new User("foo"));
      id2 = siteId(new User("foo"));
      assertTrue(id1.equals(id2));
      assertTrue(id2.equals(id1));
      assertNotSame(id1, id2);
      id2 = siteId(new User("bar"));
      assertFalse(id1.equals(id2));

      // mix site types
      id1 = siteId("foo");
      id2 = siteId(new Group("foo"));
      assertFalse(id1.equals(id2));
      id2 = siteId(new User("foo"));
      assertFalse(id1.equals(id2));
      id1 = siteId("foo");
      assertFalse(id1.equals(id2));
   }
}
