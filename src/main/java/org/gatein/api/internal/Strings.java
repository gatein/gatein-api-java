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

package org.gatein.api.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Strings
{
   public static boolean isEmptyOrNull(String string)
   {
      return (string == null) || string.length() == 0;
   }

   public static String trim(String string)
   {
      return (string == null) ? null : string.trim();
   }

   public static String trimToNull(String string)
   {
      String trimmed = trim(string);

      return isEmptyOrNull(trimmed) ? null : trimmed;
   }

   public static Splitter splitter(String regex)
   {
      return new Splitter(regex);
   }

   public static Joiner joiner(String separator)
   {
      return new Joiner(separator);
   }

   private static String[] EMPTY_STRING_ARRAY = new String[0];

   public static String[] emptyArray()
   {
      return EMPTY_STRING_ARRAY;
   }

   public static String[] add(String string, String[] array)
   {
      String[] newArray = new String[array.length+1];
      System.arraycopy(array, 0, newArray, 0, array.length);
      newArray[array.length] = string;

      return newArray;
   }

   public static class Splitter
   {
      private final String regex;
      private final boolean trim;
      private final boolean ignoreEmptyStrings;
      private final int limit;

      private Splitter(String regex)
      {
         this(regex, false, false, 0);
      }

      private Splitter(String regex, boolean trim, boolean ignoreEmptyStrings, int limit)
      {
         this.regex = regex;
         this.trim = trim;
         this.ignoreEmptyStrings = ignoreEmptyStrings;
         this.limit = limit;
      }

      public Splitter trim()
      {
         return new Splitter(regex, true, ignoreEmptyStrings, limit);
      }

      public Splitter ignoreEmptyStrings()
      {
         return new Splitter(regex, trim, true, limit);
      }

      public Splitter limit(int limit)
      {
         return new Splitter(regex, trim, ignoreEmptyStrings, limit);
      }

      public String[] split(String string)
      {
         if (string == null) return null;

         String[] split = string.split(regex, limit);
         List<String> list = new ArrayList<String>(split.length);
         for (String s : split)
         {
            if (trim) s = Strings.trim(s);
            boolean add = !ignoreEmptyStrings || !Strings.isEmptyOrNull(s);
            if (add)
            {
               list.add(s);
            }
         }

         return list.toArray(new String[list.size()]);
      }
   }

   public static class Joiner
   {
      private final String separator;
      private final int trimFlag;
      private final boolean ignoreNulls;
      private final boolean leading;
      private final boolean trailing;

      private Joiner(String separator)
      {
         this(separator, -1, false, false, false);
      }

      private Joiner(String separator, int trimFlag, boolean ignoreNulls, boolean leading, boolean trailing)
      {
         this.separator = separator;
         this.trimFlag = trimFlag;
         this.ignoreNulls = ignoreNulls;
         this.leading = leading;
         this.trailing = trailing;
      }

      public Joiner ignoreNulls()
      {
         return new Joiner(separator, trimFlag, true, leading, trailing);
      }

      public Joiner leading()
      {
         return new Joiner(separator, trimFlag, ignoreNulls, true, trailing);
      }

      public Joiner trailing()
      {
         return new Joiner(separator, trimFlag, ignoreNulls, leading, true);
      }

      public Joiner trim()
      {
         return new Joiner(separator, 0, ignoreNulls, leading, trailing);
      }

      public Joiner trimToNull()
      {
         return new Joiner(separator, 1, ignoreNulls, leading, trailing);
      }

      public String join(List<String> parts)
      {
         if (parts == null) return null;

         StringBuilder sb = new StringBuilder(parts.size());

         for (Iterator<String> iter = parts.iterator(); iter.hasNext();)
         {
            String part = iter.next();
            if (trimFlag > 0)
            {
               part = (trimFlag == 0) ? Strings.trim(part) : Strings.trimToNull(part);
            }

            if (ignoreNulls && part == null) continue;

            sb.append(part);
            if (!iter.hasNext())
            {
               if (trailing) sb.append(separator);
            }
            else
            {
               sb.append(separator);
            }
         }

         if (sb.length() == 0 && trimFlag == 1) return null;

         if (leading) sb.insert(0, separator);

         return sb.toString();
      }

      public String join(String...parts)
      {
         return join(ArraysExt.asList(parts));
      }
   }
}
