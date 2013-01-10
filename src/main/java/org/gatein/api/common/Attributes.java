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
 * Implements Map<String, String> and adds convinience methods to retrieve a <code>String</code> property converted into a
 * specific type. The {@link #get(Key)}, {@link #put(Key, Object)} and {@link #remove(Key)} methods converts <code>String</code>
 * values to/from the instances of the class specified by {@link Key#type}.
 * 
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
@SuppressWarnings("unchecked")
public class Attributes extends HashMap<String, String> {
    public Attributes() {
    }

    public Attributes(Map<String, String> values) {
        super(values);
    }

    /**
     * Returns the value to the which the specified key name is mapped. The value is returned as the type specified by the key
     * uses the static
     * <code>valueOf(String)<code> method in the key type. The <code>valueOf(String)<code> should convert the <code>String</code>
     * into an instance of the key type and return it.
     * 
     * @param key the key that holds the name and type of the attribute
     * @return the converted value
     * @throws IllegalArgumentException if the specified key type class is not {@link String} or implements static
     *         <code>valueOf(String)<code>
     */
    public <T> T get(Key<T> key) {
        Parameters.requireNonNull(key, "key");

        String name = key.getName();
        String value = get(name);
        return value != null ? fromString(key.getType(), value) : null;
    }

    /**
     * Converts the value into a String using the <code>toString</code> method. The value has to be an instance of the class
     * specified by the key type, and it has to either be a {@link String} or implement the <code>valueOf</code> method as
     * specified in {@link Attributes#get(Key)}.
     * 
     * @param key the key that holds the name and type of the attribute
     * @param value the value
     * @return the previous value, or null if it didn't exist
     * @throws IllegalArgumentException if the current value is non-null and can't be converted into the key type
     */
    public <T> T put(Key<T> key, T value) {
        Parameters.requireNonNull(key, "key");

        T oldValue = get(key);
        put(key.getName(), value != null ? toString(key.getType(), value) : null);
        return oldValue;
    }

    /**
     * Removes the attribute that is mapped to the specified key name
     * 
     * @param key the key that holds the name and type of the attribute
     * @return the previous value, or null if it didn't exist
     * @throws IllegalArgumentException if the current value is non-null and can't be converted into the key type
     */
    public <T> T remove(Key<T> key) {
        Parameters.requireNonNull(key, "key");

        T oldValue = get(key);
        remove(key.getName());
        return oldValue;
    }

    /**
     * Creates a key with the specified name and type
     * 
     * @param name the name
     * @param type the type
     * @return the key
     */
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
                throw new IllegalArgumentException("Unsupported key type", e);
            }
            throw new ApiException("Failed to convert attribute value", e);
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

        private Key(String name, Class<T> type) {
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