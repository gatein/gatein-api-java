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
package org.gatein.api.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {

    protected Attributes attributes;

    @Before
    public void before() {
        attributes = new Attributes();
    }

    @Test
    public void get_Boolean() {
        attributes.put("my.prop", "true");
        assertEquals(true, attributes.get(Attributes.key("my.prop", Boolean.class)));
    }

    @Test
    public void get_BooleanNull() {
        assertNull(attributes.get(Attributes.key("my.prop", Boolean.class)));
    }

    @Test
    public void get_BooleanWrongType() {
        attributes.put("my.prop", "string");
        assertEquals(false, attributes.get(Attributes.key("my.prop", Boolean.class)));
    }

    @Test
    public void get_Integer() {
        attributes.put("my.prop", "10");
        assertEquals(new Integer(10), attributes.get(Attributes.key("my.prop", Integer.class)));
    }

    @Test
    public void get_IntegerNull() {
        assertNull(attributes.get(Attributes.key("my.prop", Integer.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_IntegerWrongType() {
        attributes.put("my.prop", "string");
        attributes.get(Attributes.key("my.prop", Integer.class));
    }

    @Test
    public void get_String() {
        attributes.put("my.prop", "true");
        assertEquals("true", attributes.get(Attributes.key("my.prop", String.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void key_InvalidType() {
        Attributes.key("my.prop", Object.class);
    }

    @Test
    public void put_Boolean() {
        assertNull(attributes.put(Attributes.key("my.prop", Boolean.class), new Boolean(true)));
        assertEquals("true", attributes.get("my.prop"));
    }

    @Test
    public void put_Integer() {
        assertNull(attributes.put(Attributes.key("my.prop", Integer.class), new Integer(10)));
        assertEquals("10", attributes.get("my.prop"));
    }

    @Test
    public void put_Override() {
        attributes.put("my.prop", "10");
        assertEquals(new Integer(10), attributes.put(Attributes.key("my.prop", Integer.class), new Integer(20)));
        assertEquals("20", attributes.get("my.prop"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void put_OverrideInvalidType() {
        attributes.put("my.prop", "string");
        attributes.put(Attributes.key("my.prop", Integer.class), new Integer(20));
    }

    @Test
    public void remove() {
        assertEquals(0, attributes.size());
        attributes.put("my.prop", "10");
        assertEquals(1, attributes.size());
        assertEquals(new Integer(10), attributes.remove(Attributes.key("my.prop", Integer.class)));
        assertEquals(0, attributes.size());
    }

    @Test
    public void remove_InvalidType() {
        assertEquals(0, attributes.size());
        attributes.put("my.prop", "string");
        assertEquals(1, attributes.size());
        try {
            attributes.remove(Attributes.key("my.prop", Integer.class));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
        }
        assertEquals(1, attributes.size());
    }

    @Test
    public void remove_ThroughSettingNull() {
        assertEquals(0, attributes.size());
        attributes.put("my.prop", "10");
        assertEquals(1, attributes.size());
        attributes.put(Attributes.key("my.prop", Integer.class), null);
        assertEquals(0, attributes.size());
    }

    @Test
    public void containsKey() {
        assertEquals(0, attributes.size());
        attributes.put("my.prop", "string");
        assertTrue(attributes.containsKey("my.prop"));
        assertTrue(attributes.containsKey(Attributes.key("my.prop", String.class)));
        /* not there with improper type */
        assertFalse(attributes.containsKey(Attributes.key("my.prop", Integer.class)));
    }


}
