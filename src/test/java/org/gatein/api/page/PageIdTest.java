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

package org.gatein.api.page;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.gatein.api.security.Group;
import org.gatein.api.security.User;
import org.junit.Test;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class PageIdTest {
    @Test
    public void testEquals() {
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
    public void testFormat_Alternative() {
        // unreserved = ALPHA / DIGIT / "-" / "." / "_" / "~"
        Pattern urlUnreserved = Pattern.compile("[0-9A-Za-z-\\._~]*");
        PageId pageId = new PageId(new Group("platform", "administrators"), "pageManagement");

        assertTrue(urlUnreserved.matcher(String.format("%#s", pageId).toString()).matches());
    }

    @Test
    public void testFromString() {
        PageId id = new PageId("foo-_site0", "bar");
        assertEquals(id, PageId.fromString(id.toString()));
        assertEquals(id, PageId.fromString(String.format("%s", id).toString()));
        assertEquals(id, PageId.fromString(String.format("%#s", id).toString()));

        id = new PageId(new Group("foo", "bar"), "bar_baz");
        assertEquals(id, PageId.fromString(id.toString()));
        assertEquals(id, PageId.fromString(String.format("%s", id).toString()));
        assertEquals(id, PageId.fromString(String.format("%#s", id).toString()));

        id = new PageId(new User("foo"), "bar_baz");
        assertEquals(id, PageId.fromString(id.toString()));
        assertEquals(id, PageId.fromString(String.format("%s", id).toString()));
        assertEquals(id, PageId.fromString(String.format("%#s", id).toString()));
    }
}
