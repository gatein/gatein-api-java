/*
 * JBoss, a division of Red Hat
 * Copyright 2011, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
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

package org.gatein.api;

import java.util.Comparator;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class Query<T>
{

   public static <T> QueryBuilder<T> builder()
   {
      return new QueryBuilder<T>();
   }

   public static class QueryBuilder<T>
   {
      public QueryBuilder<T> where(Filter<T> filter)
      {
         return this;
      }

      public QueryBuilder<T> orderBy(Comparator<T> comparator)
      {
         return this;
      }

      public QueryBuilder<T> limit(int pageSize)
      {
         return this;
      }

      public QueryBuilder<T> startAt(int firstItem)
      {
         return this;
      }

      public QueryBuilder<T> scope(GateInObject root)
      {
         return this;
      }

      public QueryBuilder<T> refine(Query<T> query)
      {
         return this;
      }

      public Query<T> build()
      {
         return null;
      }
   }
}
