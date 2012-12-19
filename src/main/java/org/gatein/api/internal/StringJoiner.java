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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StringJoiner
{
   private final String separator;
   private final int trimFlag;
   private final boolean ignoreNulls;
   private final boolean leading;
   private final boolean trailing;

   private StringJoiner(String separator)
   {
      this(separator, -1, false, false, false);
   }

   private StringJoiner(String separator, int trimFlag, boolean ignoreNulls, boolean leading, boolean trailing)
   {
      this.separator = separator;
      this.trimFlag = trimFlag;
      this.ignoreNulls = ignoreNulls;
      this.leading = leading;
      this.trailing = trailing;
   }

   public StringJoiner ignoreNulls()
   {
      return new StringJoiner(separator, trimFlag, true, leading, trailing);
   }

   public StringJoiner leading()
   {
      return new StringJoiner(separator, trimFlag, ignoreNulls, true, trailing);
   }

   public StringJoiner trailing()
   {
      return new StringJoiner(separator, trimFlag, ignoreNulls, leading, true);
   }

   public StringJoiner trim()
   {
      return new StringJoiner(separator, 0, ignoreNulls, leading, trailing);
   }

   public StringJoiner trimToNull()
   {
      return new StringJoiner(separator, 1, ignoreNulls, leading, trailing);
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
            part = (trimFlag == 0) ? trim(part) : trimToNull(part);
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
      return join(Arrays.asList(parts));
   }

   private String trim(String string)
   {
      return (string == null) ? null : string.trim();
   }

   private String trimToNull(String string)
   {
      if (string == null) return null;
      string = string.trim();
      return (string.length() == 0) ? null : string;
   }

   public static StringJoiner joiner(String separator)
   {
      return new StringJoiner(separator);
   }
}