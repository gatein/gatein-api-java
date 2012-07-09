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

package org.gatein.api.commons;

/**
 * Represent range in paginated query
 *
 * @author <a href="mailto:bdawidow@redhat.com">Boleslaw Dawidowicz</a>
 */
public class Range
{
   //TODO: Just a quick impl

   private int offset = 0;
   
   private int limit = 0;

   private Range() {}

   private Range(int offset, int limit)
   {
      this.offset = offset;
      this.limit = limit;
   }

   public int getPage()
   {
      if (offset == 0 || offset < limit || limit == 0)
      {
         return 0;
      }

      return offset / limit;
   }

   public int getOffset()
   {
      return offset;
   }

   public int getLimit()
   {
      return limit;
   }

   public static Range of(int offset, int limit)
   {
      return new Range(offset, limit);
   }
   
   public Range next()
   {
      offset += limit;
      return this;
   }

   public Range previous()
   {
      offset -= limit;
      if(offset < 0)
      {
         offset = 0;
      }
      return this;
   }

   public Range first()
   {
      offset = 0;
      return this;
   }

}
