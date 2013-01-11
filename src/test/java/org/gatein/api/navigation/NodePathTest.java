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
package org.gatein.api.navigation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodePathTest {

    @Test
    public void fromString() {
        assertArrayEquals(new String[] {}, NodePath.fromString("/").asArray());
        assertArrayEquals(new String[] { "one" }, NodePath.fromString("/one").asArray());
        assertArrayEquals(new String[] { "one", "two" }, NodePath.fromString("/one/two").asArray());
    }

    @Test
    public void isParent() {
        assertTrue(NodePath.root().isParent(NodePath.path("one", "two")));
        assertTrue(NodePath.path("one").isParent(NodePath.path("one", "two")));
        assertTrue(NodePath.path("one", "two").isParent(NodePath.path("one", "two", "three")));

        assertFalse(NodePath.path("what").isParent(NodePath.path("one", "two")));
        assertFalse(NodePath.path("one", "two").isParent(NodePath.path("one")));
        assertFalse(NodePath.path("what", "two").isParent(NodePath.path("one", "two", "three")));
    }

    @Test
    public void parent() {
        assertArrayEquals(new String[] { "one" }, NodePath.path("one", "two").parent().asArray());
        assertNull(NodePath.root().parent());
    }

    @Test
    public void subPath() {
        assertArrayEquals(new String[] { "two", "three" }, NodePath.path("one", "two", "three").subPath(1).asArray());
        assertArrayEquals(new String[] { "two" }, NodePath.path("one", "two", "three").subPath(1, 2).asArray());
    }

    @Test
    public void toString_() {
        assertEquals("/", NodePath.root().toString());
        assertEquals("/one", NodePath.path("one").toString());
        assertEquals("/one/two", NodePath.path("one", "two").toString());
    }

}
