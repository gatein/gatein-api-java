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
package org.gatein.api.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MembershipTest {

    @Test
    public void fromString() {
        assertMembership("user", null, Membership.fromString("user"));
        assertMembership("member", "/platform/administrators", Membership.fromString("member:/platform/administrators"));
        assertMembership("*", "/platform/administrators", Membership.fromString("*:/platform/administrators"));
    }

    @Test
    public void toString_() {
        assertEquals("user", new Membership(new User("user")).toString());
        assertEquals("member:/platform/administrators",
                new Membership("member", new Group("platform", "administrators")).toString());
        assertEquals("*:/platform/administrators", Membership.any("platform", "administrators").toString());
    }

    @Test
    public void equalsUserMembership() {
        /* do some string ops to make sure there two distinct string instances in game */
        String id = "_id".substring(1);
        User u = new User(id);
        Membership m = new Membership(u);

        assertEquals(new Membership(new User(id)), m);
        assertEquals("id", m.getMembershipType());

    }

    @Test
    public void equalsGroupMembership() {
        Group g = new Group("platform", "administrators");
        Membership m = new Membership("member", g);

        assertEquals(Membership.fromString("member:/platform/administrators"), m);
        assertEquals("member", m.getMembershipType());

    }

    public static void assertMembership(String expectedType, String expectedGroup, Membership actual) {
        if (expectedGroup == null) {
            assertNull(actual.getGroup());
        } else {
            assertEquals(expectedGroup, actual.getGroup().getId());
        }
        assertEquals(expectedType, actual.getMembershipType());
    }

}
