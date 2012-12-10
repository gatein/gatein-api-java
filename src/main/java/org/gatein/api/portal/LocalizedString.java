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

import java.util.Locale;
import java.util.Map;

/**
 * A <code>LocalizedString</code> can represent a single value (non localized) or many values each mapping to a specific {@link Locale}
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class LocalizedString extends Localized<String>
{
   public LocalizedString(LocalizedString localizedString)
   {
      super(localizedString);
   }

   /**
    * Constructor to create a non localized string. The result will include a <code>Value</code> with a null <code>Locale</code>.
    *
    * @param value the value to represent the non localized string
    */
   public LocalizedString(String value)
   {
      this(null, value);
   }

   /**
    * Constructor to create a localized string.
    *
    * @param locale the locale of the value
    * @param value the value
    */
   public LocalizedString(Locale locale, String value)
   {
      super(locale, value);
   }

   /**
    * Constructor to create a localized string with multiple values.
    *
    * @param valueMap the map containing the Locale->String mapping.
    */
   public LocalizedString(Map<Locale, String> valueMap)
   {
      super(valueMap);
   }

   /**
    * This method returns the value for a non localized string. If the string is localized this will return
    * null. This is equivalent to calling {@link #getValue(java.util.Locale)} and passing in null for the <code>Locale</code>
    *
    * @return the value or null if the string is localized.
    */
   public String getValue()
   {
      return getValue(null);
   }

   /**
    * This will clear any localized values, and convert the string to a non localized string.
    *
    * @param value the value
    * @return the localized string
    */
   public LocalizedString setValue(String value)
   {
      for (Value<String> v : getLocalizedValues())
      {
         removeLocalizedValue(v.getLocale());
      }
      super.setLocalizedValue(null, value);
      return this;
   }

   /**
    * This method will first remove the non localized value if one exists then sets the value for the specified locale.
    * If a locale already exists this will override the previous value mapped to that locale.
    *
    * @param locale the locale
    * @param value the value
    * @return the localized string
    */
   @Override
   public LocalizedString setLocalizedValue(Locale locale, String value)
   {
      if (!isLocalized()) removeLocalizedValue(null);

      super.setLocalizedValue(locale, value);
      return this;
   }

   /**
    * Indicates if the string is localized or not.
    *
    * @return true if the string is localized, false otherwise.
    */
   public boolean isLocalized()
   {
      return getLocalizedValue(null) == null;
   }
}
