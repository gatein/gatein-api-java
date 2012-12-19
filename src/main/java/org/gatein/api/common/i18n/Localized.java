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

package org.gatein.api.common.i18n;

import org.gatein.api.internal.ObjectToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Base class for localizing values of type T.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public abstract class Localized<T extends Serializable> implements Iterable<T>, Serializable
{
   private final Map<Locale, Value<T>> values;

   protected Localized(Localized<T> localized)
   {
      this.values = new HashMap<Locale, Value<T>>(localized.values);
   }

   protected Localized(Locale locale, T value)
   {
      this(Collections.singletonMap(locale, value));
   }

   protected Localized(Map<Locale, T> valueMap)
   {
      if (valueMap == null) throw new IllegalArgumentException("valueMap cannot be null.");
      Map<Locale, Value<T>> map = new HashMap<Locale, Value<T>>(valueMap.size());

      for (Map.Entry<Locale, T> entry : valueMap.entrySet())
      {
         Locale locale = entry.getKey();
         T value = entry.getValue();
         if (value == null) throw new IllegalArgumentException("valueMap contains a null value for key locale " + locale);

         map.put(locale, new Value<T>(locale, value));
      }
      values = map;
   }

   /**
    * Gets the value mapped to the specific locale. Can return null if no mapping exists.
    *
    * @param locale the locale of the value
    * @return the value object representing the mapping between locale and value.
    */
   public Value<T> getLocalizedValue(Locale locale)
   {
      return values.get(locale);
   }

   /**
    * Gets the T value mapped to the specific locale. Can return null if no mapping exists. Convenience method for
    * <code>getLocalizedValue(Locale).getValue()</code>
    *
    * @param locale the locale of the value
    * @return the value of type T
    */
   public T getValue(Locale locale)
   {
      Value<T> value = getLocalizedValue(locale);
      return (value == null) ? null : value.getValue();
   }

   /**
    * Sets the value for the specified locale. If a locale already exists, will override the previous value mapped to that
    * locale.
    *
    * @param locale the locale
    * @param value the value
    * @return this
    */
   public Localized<T> setLocalizedValue(Locale locale, T value)
   {
      values.put(locale, new Value<T>(locale, value));

      return this;
   }

   /**
    * Will remove the value mapped to the locale.
    *
    * @param locale the locale
    */
   public void removeLocalizedValue(Locale locale)
   {
      values.remove(locale);
   }

   /**
    * Returns a collection of values representing the mapping of Locale to type T
    *
    * @return the collection of values
    */
   public Collection<Value<T>> getLocalizedValues()
   {
      return Collections.unmodifiableCollection(values.values());
   }

   @Override
   public Iterator<T> iterator()
   {
      List<T> list = new ArrayList<T>(values.size());
      for (Value<T> value : values.values())
      {
         list.add(value.getValue());
      }

      return list.iterator();
   }

   @Override
   public String toString()
   {
      return ObjectToStringBuilder.toStringBuilder(getClass())
         .add("values", values.values()).toString();
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof Localized)) return false;

      Localized<?> localized = (Localized<?>) o;

      return values.equals(localized.values);
   }

   @Override
   public int hashCode()
   {
      return values.hashCode();
   }

   /**
    * Value object representing the mapping of a Locale to a value of type T.
    *
    * @param <T> the value type
    */
   public static final class Value<T extends Serializable> implements Serializable
   {
      private final Locale locale;
      private final T value;

      private Value(Locale locale, T value)
      {
         this.locale = locale;
         this.value = value;
      }

      /**
       * Returns the value object of type T.
       *
       * @return the value of type T
       */
      public T getValue()
      {
         return value;
      }

      /**
       * Returns the locale associated with this value.
       *
       * @return the locale
       */
      public Locale getLocale()
      {
         return locale;
      }

      @Override
      public boolean equals(Object o)
      {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;

         Value<?> v = (Value<?>) o;

         return (locale == null) ? v.locale == null : locale.equals(v.locale) &&
            (value == null) ? v.value == null : value.equals(v.value);
      }

      @Override
      public int hashCode()
      {
         int result = locale != null ? locale.hashCode() : 0;
         result = 31 * result + (value != null ? value.hashCode() : 0);
         return result;
      }

      @Override
      public String toString()
      {
         return ObjectToStringBuilder.toStringBuilder(getClass())
            .add("locale", locale)
            .add("value", value).toString();
      }
   }
}
