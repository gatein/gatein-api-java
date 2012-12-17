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

import org.gatein.api.common.Filter;
import org.gatein.api.common.Pagination;
import org.gatein.api.common.Sorting;
import org.gatein.api.internal.Objects;

/**
 * Base class for query objects. If a base query class does not support pagination, sorting, or filter they should set
 * them to null.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public abstract class Query<T>
{
   private final Filter<T> filter;
   private final Pagination pagination;
   private final Sorting<T> sorting;

   /**
    * Constructor to be used by base query classes.
    *
    * @param filter     custom filter to "filter" the results. This can be null.
    * @param pagination pagination object controlling the pagination of the results. This can be null.
    * @param sorting    the sorting object controlling the sorting of the results. This can be null.
    */
   protected Query(Pagination pagination, Filter<T> filter, Sorting<T> sorting)
   {
      this.filter = filter;
      this.pagination = pagination;
      this.sorting = sorting;
   }

   /**
    * The filter used for this query.
    *
    * @return the filter, which can be null.
    * @see Filter
    */
   public Filter<T> getFilter()
   {
      return filter;
   }

   /**
    * The pagination object used for this query.
    *
    * @return the pagination object, which can be null.
    * @see Pagination
    */
   public Pagination getPagination()
   {
      return pagination;
   }

   /**
    * The sorting object used for this query.
    *
    * @return the sorting object, which can be null.
    */
   public Sorting<T> getSorting()
   {
      return sorting;
   }

   @Override
   public String toString()
   {
      return Objects.toStringBuilder(getClass())
         .add("pagination", pagination)
         .add("filter", filter)
         .add("sorting", sorting)
         .toString();
   }
}
