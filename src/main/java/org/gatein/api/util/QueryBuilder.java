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

package org.gatein.api.util;

import java.util.Comparator;

/**
 * Abstract query builder to handle common query properties like {@link Pagination}, {@link Filter}, and {@link Sorting}
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
//
public abstract class QueryBuilder<T, Q extends Query<T>, B extends QueryBuilder<T, Q, B>>
{
   protected Filter<T> filter;
   protected Pagination pagination;
   protected Sorting<T> sorting;

   protected QueryBuilder()
   {
      pagination = new Pagination(0, 20); // Default all queries to use pagination
   }

   public B from(Query<T> query)
   {
      return withPagination(query.getPagination()).withFilter(query.getFilter()).withSorting(query.getSorting());
   }

   @SuppressWarnings("unchecked")
   public B withFilter(Filter<T> filter)
   {
      this.filter = filter;
      return (B) this;
   }

   public B withPagination(int offset, int limit)
   {
      return withPagination(new Pagination(offset, limit));
   }

   @SuppressWarnings("unchecked")
   public B withPagination(Pagination pagination)
   {
      this.pagination = pagination;
      return (B) this;
   }

   @SuppressWarnings("unchecked")
   public B withNextPage()
   {
      if (pagination != null)
      {
         pagination = pagination.getNext();
      }

      return (B) this;
   }

   @SuppressWarnings("unchecked")
   public B withPreviousPage()
   {
      if (pagination != null)
      {
         pagination = pagination.getPrevious();
      }

      return (B) this;
   }

   @SuppressWarnings("unchecked")
   public B withSorting(Sorting<T> sorting)
   {
      this.sorting = sorting;
      return (B) this;
   }

   public SortingBuilder<T, Q, B> withSorting()
   {
      return new SortingBuilder<T,Q,B>(this);
   }

   public abstract Q build();

   public static class SortingBuilder<T, Q extends Query<T>, B extends QueryBuilder<T, Q, B>>
   {
      private final QueryBuilder<T, Q, B> queryBuilder;

      private SortingBuilder(QueryBuilder<T, Q, B> queryBuilder)
      {
         this.queryBuilder = queryBuilder;
      }

      public B withComparator(Comparator<T> comparator)
      {
         return queryBuilder.withSorting(new Sorting<T>(comparator));
      }

      public B ascending()
      {
         return queryBuilder.withSorting(new Sorting<T>(Sorting.Order.ascending));
      }

      public B descending()
      {
         return queryBuilder.withSorting(new Sorting<T>(Sorting.Order.descending));
      }
   }
}
