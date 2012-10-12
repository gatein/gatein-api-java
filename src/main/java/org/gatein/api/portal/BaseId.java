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

import java.io.Serializable;
import java.util.Formatter;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public abstract class BaseId implements Formatted, Serializable
{
   public String format(String format)
   {
      return doFormat(format, null);
   }

   public String format(String format, Formatted.Adapter adapter)
   {
      if (adapter == null) throw new IllegalArgumentException("adapter cannot be null");

      return doFormat(format, adapter);
   }

   private String doFormat(String format, Formatted.Adapter adapter)
   {
      if (format == null) throw new IllegalArgumentException("format cannot be null.");

      Object[] args = (adapter == null) ? getFormatArguments() : getFormatArguments(adapter);
      return new Formatter().format(format, args).toString();
   }

   protected static Adapter groupAdapter()
   {
      return GROUP_ADAPTER;
   }

   private static final ReplaceAllAdapter GROUP_ADAPTER = new ReplaceAllAdapter("/", "~", 1);

   private static final class ReplaceAllAdapter implements Formatted.Adapter
   {
      private final int[] indexes;
      private final String regex;
      private final String replacement;

      private ReplaceAllAdapter(String regex, String replacement, int... indexes)
      {
         this.regex = regex;
         this.replacement = replacement;
         this.indexes = indexes;
      }

      @Override
      public Object adapt(int index, Object argument)
      {
         if (argument instanceof String && contains(index))
         {
            return ((String) argument).replaceAll(regex, replacement);
         }

         return argument;
      }

      private boolean contains(int index)
      {
         for (int i : indexes)
         {
            if (i == index) return true;
         }

         return false;
      }
   }
}
