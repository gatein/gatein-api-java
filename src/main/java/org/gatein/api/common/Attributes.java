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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
public class Attributes extends HashMap<String, String> implements Serializable {
    /**
     * Creates a new attributes instance with no attributes
     */
    public Attributes() {
    }

    /**
     * Creates a new attributes instance and adds all attributes from the specified map
     * 
     * @param values the map of attributes to add
     */
    public Attributes(Map<String, String> values) {
        super(Parameters.requireNonNull(values, "values"));
    }

    /**
     * Returns the value to the which the specified key name is mapped. The value is returned as the type specified by the key
     * type
     * 
     * @param key the key for the attribute
     * @return the converted value
     * @throws IllegalArgumentException if the specified key is null, or the value failed to convert into to key type
     */
    public <T> T get(Key<T> key) {
        Parameters.requireNonNull(key, "key");

        String name = key.getName();
        String value = get(name);
        return value != null ? fromString(key.getType(), value) : null;
    }

    /**
     * Converts the value into a String using the <code>toString</code> method
     * 
     * @param key the key for the attribute
     * @param value the value
     * @return the previous value, or null if it didn't exist
     * @throws IllegalArgumentException if the specified key is null, the current value is non-null and failed to convert
     *         into the key type, or the value class is not the same as the key type
     */
    public <T> T put(Key<T> key, T value) {
        Parameters.requireNonNull(key, "key");
        if (!key.getType().equals(value.getClass())) {
            throw new IllegalArgumentException("Value class is not the same as key type");
        }

        T oldValue = get(key);
        put(key.getName(), value != null ? toString(key.getType(), value) : null);
        return oldValue;
    }

    /**
     * Removes the attribute that is mapped to the specified key name
     * 
     * @param key the key for the the attribute
     * @return the previous value, or null if it didn't exist
     * @throws IllegalArgumentException if the specified key is null, or the current value is non-null and failed to to convert
     *         into the key type
     */
    public <T> T remove(Key<T> key) {
        Parameters.requireNonNull(key, "key");

        T oldValue = get(key);
        remove(key.getName());
        return oldValue;
    }

    /**
     * Creates a key with the specified name and type. The type class has to either be {@link String} or implement a static
     * method <code>valueOf(String)</code> method as specified in {@link Attributes#get(Key)}. The <code>valueOf</code> method
     * should convert the string parameter into an instance of type and return it.
     * 
     * @param name the key name
     * @param type the key type
     * @return the key
     * @throws IllegalArgumentException if name or type is null, or if type is not {@link String} or does not implement the
     *         static <code>valueOf</code> method
     */
    public static <T> Key<T> key(String name, Class<T> type) {
        Parameters.requireNonNull(name, "name");
        Parameters.requireNonNull(type, "type");

        if (!type.equals(String.class)) {
            getValueOfMethod(type);
        }

        return new Key<T>(name, type);
    }

    private <T> T fromString(Class<T> type, String value) {
        if (type.equals(String.class)) {
            return (T) value;
        }

        Method m = getValueOfMethod(type);

        try {
            return (T) m.invoke(null, value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid type" + type, e);
        }
    }

    private <T> String toString(Class<T> type, T value) {
        if (value instanceof String) {
            return (String) value;
        }

        return value.toString();
    }

    private static Method getValueOfMethod(Class<?> c) {
        try {
            return c.getDeclaredMethod("valueOf", new Class[] { String.class });
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Unsupported key type. Key type has to either be String or implement 'valueOf(String.class)'");
        }
    }

    public static class Key<T> {
        private final String name;
        private Class<T> type;

        private Key(String name, Class<T> type) {
            this.name = name;
            this.type = type;
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