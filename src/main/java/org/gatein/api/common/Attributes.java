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

import org.gatein.api.Parameters;
import org.gatein.api.internal.ObjectToStringBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
@SuppressWarnings("unchecked")
public class Attributes {
    private Map<Attributes.Key<?>, Object> values;

    public Attributes() {
        this(new HashMap<Attributes.Key<?>, Object>());
    }

    public Attributes(Map<Attributes.Key<?>, Object> values) {
        this.values = values;
    }

    public <T> T get(Key<T> key) {
        return (T) values.get(Parameters.requireNonNull(key, "key"));
    }

    public <T> T put(Key<T> key, T value) {
        return (T) values.put(Parameters.requireNonNull(key, "key"), value);
    }

    public <T> T remove(Key<T> key) {
        return (T) values.remove(Parameters.requireNonNull(key, "key"));
    }

    public Collection<Object> values() {
        return values.values();
    }

    public Set<Key<?>> keySet() {
        return values.keySet();
    }

    public Set<Map.Entry<Key<?>, Object>> entrySet() {
        return values.entrySet();
    }

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public boolean containsKey(Key<?> key) {
        return values.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return values.containsValue(value);
    }

    public void putAll(Map<? extends Key<?>, ?> m) {
        values.putAll(m);
    }

    public void clear() {
        values.clear();
    }

    @Override
    public String toString() {
        return ObjectToStringBuilder.toStringBuilder().add(values).toString();
    }

    public static <T> Key<T> key(String name, Class<T> type) {
        return new Key<T>(name, type) {
        };
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