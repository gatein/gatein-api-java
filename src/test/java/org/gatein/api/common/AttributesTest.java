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
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {

    private Attributes attributes;

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
    public void get_BooleanWrongType() {
        attributes.put("my.prop", "string");
        assertEquals(false, attributes.get(Attributes.key("my.prop", Boolean.class)));
    }

    @Test
    public void get_BooleanNull() {
        assertNull(attributes.get(Attributes.key("my.prop", Boolean.class)));
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
    public void put_Boolean() {
        assertNull(attributes.put(Attributes.key("my.prop", Boolean.class), new Boolean(true)));
        assertEquals("true", attributes.get("my.prop"));
    }

    @Test
    public void put_Integer() {
        assertNull(attributes.put(Attributes.key("my.prop", Integer.class), new Integer(10)));
        assertEquals("10", attributes.get("my.prop"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void put_InvalidType() {
        assertNull(attributes.put(Attributes.key("my.prop", Object.class), new Object()));
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
        attributes.put("my.prop", "10");
        assertEquals(new Integer(10), attributes.remove(Attributes.key("my.prop", Integer.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void remove_InvalidType() {
        attributes.put("my.prop", "string");
        attributes.remove(Attributes.key("my.prop", Integer.class));
    }
}
