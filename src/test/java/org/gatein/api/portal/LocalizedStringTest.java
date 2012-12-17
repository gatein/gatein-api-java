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


import org.gatein.api.common.i18n.Localized;
import org.gatein.api.common.i18n.LocalizedString;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class LocalizedStringTest
{
   @Test
   public void testSimple()
   {
      LocalizedString simple = new LocalizedString("simple");
      assertFalse(simple.isLocalized());
      assertEquals("simple", simple.getValue());
      assertEquals(1, simple.getLocalizedValues().size());
      for (String value : simple)
      {
         assertEquals("simple", value);
      }

      simple.setValue("another value");
      assertEquals("another value", simple.getValue());
   }

   @Test
   public void testLocalized()
   {
      List<String> values = Arrays.asList("Hello", "Ciao", "Bonjour");

      LocalizedString hello = new LocalizedString(Locale.ENGLISH, values.get(0));
      hello.setLocalizedValue(Locale.ITALIAN, values.get(1)).setLocalizedValue(Locale.FRENCH, values.get(2));

      assertTrue(hello.isLocalized());
      assertNull(hello.getValue());
      assertEquals(3, hello.getLocalizedValues().size());

      for (String value : hello)
      {
         assertTrue(values.contains(value));
      }

      for (Localized.Value<String> value : hello.getLocalizedValues())
      {
         if (value.getLocale().equals(Locale.ENGLISH))
         {
            assertEquals(values.get(0), value.getValue());
         }
         else if (value.getLocale().equals(Locale.ITALIAN))
         {
            assertEquals(values.get(1), value.getValue());
         }
         else if (value.getLocale().equals(Locale.FRENCH))
         {
            assertEquals(values.get(2), value.getValue());
         }
         else
         {
            fail("Unknown locale found " + value.getLocale());
         }
      }
   }

   @Test
   public void testEquals()
   {
      List<String> values = Arrays.asList("Hello", "Ciao", "Bonjour");

      LocalizedString one = new LocalizedString(Locale.ENGLISH, values.get(0));
      one.setLocalizedValue(Locale.ITALIAN, values.get(1)).setLocalizedValue(Locale.FRENCH, values.get(2));

      Map<Locale, String> valueMap = new HashMap<Locale, String>(3);
      valueMap.put(Locale.ENGLISH, values.get(0));
      valueMap.put(Locale.ITALIAN, values.get(1));
      valueMap.put(Locale.FRENCH, values.get(2));
      LocalizedString two = new LocalizedString(valueMap);

      assertEquals(one, two);
   }

   @Test
   public void testLocalized_Unlocalized()
   {
      LocalizedString localizedString = new LocalizedString("simple");
      assertFalse(localizedString.isLocalized());
      localizedString.setLocalizedValue(Locale.ENGLISH, "Hello");
      assertTrue(localizedString.isLocalized());
      assertNull(localizedString.getValue());
      assertEquals("Hello", localizedString.getLocalizedValue(Locale.ENGLISH).getValue());

      localizedString.setValue("non localized");
      assertFalse(localizedString.isLocalized());
      assertEquals("non localized", localizedString.getValue());
   }
}
