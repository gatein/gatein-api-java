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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.gatein.api.ApiException;
import org.gatein.api.internal.Parameters;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
@SuppressWarnings("unchecked")
public class Attributes extends HashMap<String, String> {
    public Attributes() {
    }

    public Attributes(Map<String, String> values) {
        super(values);
    }

    public <T> T get(Key<T> key) {
        Parameters.requireNonNull(key, "key");

        String name = key.getName();
        String value = get(name);
        return value != null ? fromString(key.getType(), value) : null;
    }

    public <T> T put(Key<T> key, T value) {
        Parameters.requireNonNull(key, "key");

        T oldValue = get(key);
        put(key.getName(), value != null ? toString(key.getType(), value) : null);
        return oldValue;
    }

    public <T> T remove(Key<T> key) {
        Parameters.requireNonNull(key, "key");

        T oldValue = get(key);
        remove(key.getName());
        return oldValue;
    }

    public static <T> Key<T> key(String name, Class<T> type) {
        return new Key<T>(name, type) {
        };
    }

    private <T> T fromString(Class<T> type, String value) {
        if (type.equals(String.class)) {
            return (T) value;
        }

        Method m;
        try {
            m = type.getDeclaredMethod("valueOf", new Class[] { String.class });
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "Unsupported key type. Key type has to either be String or implement 'valueOf(String.class)'", e);
        }

        try {
            return (T) m.invoke(null, value);
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                if (((InvocationTargetException) e).getTargetException() instanceof NumberFormatException) {
                    throw new IllegalArgumentException("Unsupported key type", e);
                }
            }
            throw new ApiException("Failed to convert value", e);
        }
    }

    private <T> String toString(Class<T> type, T value) {
        if (value instanceof String) {
            return (String) value;
        }

        try {
            type.getDeclaredMethod("valueOf", new Class[] { String.class });
            return value.toString();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "Unsupported key type. Key type has to either be String or implement 'valueOf(String.class)'");
        }
    }

    public static abstract class Key<T> {
        private final String name;
        private Class<T> type;

        public Key(String name, Class<T> type) {
            this.name = Parameters.requireNonNull(name, "name");
            this.type = Parameters.requireNonNull(type, "type");
        }

        public String getName() {
            return name;
        }

        public Class<T> getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Key))
                return false;

            Key<?> key = (Key<?>) o;

            return name.equals(key.name) && type.equals(type);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}