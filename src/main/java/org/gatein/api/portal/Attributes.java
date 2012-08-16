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

package org.gatein.api.portal;


import org.gatein.api.annotation.NotNull;
import org.gatein.api.internal.Objects;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
@SuppressWarnings("unchecked")
public class Attributes
{
   private Map<Attributes.Key<?>, Object> values;

   public Attributes()
   {
      this(new HashMap<Attributes.Key<?>, Object>());
   }

   public Attributes(Map<Attributes.Key<?>, Object> values)
   {
      this.values = values;
   }

   public <T> T get(@NotNull Key<T> key)
   {
      if (key == null) throw new IllegalArgumentException("key cannot be null");
      return (T) values.get(key);
   }

   public <T> T put(@NotNull Key<T> key, T value)
   {
      if (key == null) throw new IllegalArgumentException("key cannot be null");
      return (T) values.put(key, value);
   }

   public <T> T remove(@NotNull Key<T> key)
   {
      if (key == null) throw new IllegalArgumentException("key cannot be null");
      return (T) values.remove(key);
   }

   public Collection<Object> values()
   {
      return values.values();
   }

   public Set<Key<?>> keySet()
   {
      return values.keySet();
   }

   public Set<Map.Entry<Key<?>, Object>> entrySet()
   {
      return values.entrySet();
   }

   public int size()
   {
      return values.size();
   }

   public boolean isEmpty()
   {
      return values.isEmpty();
   }

   public boolean containsKey(Key<?> key)
   {
      return values.containsKey(key);
   }

   public boolean containsValue(Object value)
   {
      return values.containsValue(value);
   }

   public void putAll(Map<? extends Key<?>, ?> m)
   {
      values.putAll(m);
   }

   public void clear()
   {
      values.clear();
   }

   @Override
   public String toString()
   {
      return Objects.toStringBuilder().add(values).toString();
   }

   public static abstract class Key<T>
   {
      private final String name;
      private Class<T> type;

      public Key(@NotNull String name)
      {
         if (name == null) throw new IllegalArgumentException("name cannot be null");

         this.name = name;
      }

      public String getName()
      {
         return name;
      }

      public Class<T> getType()
      {
         if (type == null)
         {
            Class<?> cls = getClass();
            type = (Class<T>) resolveType(resolveGenericType(cls, Key.class));
         }

         return type;
      }

      private static Type resolveGenericType(Type genericType, Class<?> targetType)
      {
         if (genericType == null || genericType == Object.class) return null;

         Class<?> cls;
         if (genericType instanceof ParameterizedType)
         {
            cls = (Class<?>) ((ParameterizedType) genericType).getRawType();
         }
         else
         {
            cls = (Class<?>) genericType;
         }

         if (cls.equals(targetType)) return genericType;

         return resolveGenericType(cls.getGenericSuperclass(), targetType);
      }

      private static Class<?> resolveType(Type type)
      {
         if (type instanceof Class)
         {
            return (Class<?>) type;
         }
         else if (type instanceof ParameterizedType)
         {
            return resolveType(((ParameterizedType) type).getActualTypeArguments()[0]);
         }
         else if (type instanceof GenericArrayType)
         {
            GenericArrayType arrayType = (GenericArrayType) type;
            Class<?> ct = resolveType(arrayType.getGenericComponentType());

            // ct can be null here since we don't support TypeVariable's atm.
            return (ct == null) ? null : Array.newInstance(ct, 0).getClass();
         }
         else if (type instanceof TypeVariable)
         {
            // Not supporting this atm
         }

         return null;
      }

      @Override
      public boolean equals(Object o)
      {
         if (this == o) return true;
         if (!(o instanceof Key)) return false;

         Key key = (Key) o;

         return name.equals(key.name);
      }

      @Override
      public int hashCode()
      {
         return name.hashCode();
      }

      @Override
      public String toString()
      {
         return getName();
      }
   }
}