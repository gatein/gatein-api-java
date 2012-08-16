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

import java.util.List;

/**
 * Builder
 *
 * @author <a href="mailto:bdawidow@redhat.com">Boleslaw Dawidowicz</a>
 */
public interface Query<T>
{

   /**
    * Reset query to clean state and revert any modifications
    * @return Modified query
    */
   Query<T> reset();

   /**
    *
    * @return Immutable version of a query
    */
   Query<T> immutable();

   /**
    *
    * @return Clone - new instance - of a query
    */
   Query<T> clone();

   /**
    * Execute query
    *
    * @return List of query results
    */
   List<T> execute();

   /**
    *
    * @return Number of query results
    */
   int getResultsCount();

   /**
    * Calculates number of pages based on result from getResultsCount() and getRange().
    *
    * @return Number of pages
    */
   int getPageCount();

   /**
    * Set if query should be sorted
    *
    * @param ascending - if true query will be sorted in ascending order
    * @return Modified query
    */
   Query<T> sort(boolean ascending);

   /**
    * @param range Range object defining starting point and number of returned results - a page.
    *              If null range will be removed.
    * @return Modified query
    */
   Query<T> setRange(Range range);

   /**
    * @return Currently set range object. Can be null
    */
   Range getRange();

   /**
    * Returns current page calculated from currently set Range object
    *
    * @return Current page number. 0 if getRange() returns null
    */
   int getCurrentPage();

   /**
    * Does nothing if getRange() returns null
    *
    * @param number Current page index
    * @return Modified query.
    */
   Query<T> setPage(int number);

   /**
    * Move to next page. Equivalent to getRange().next(). Does nothing if getRange() returns null
    * @return Modified query.
    */
   Query<T> nextPage();

   /**
    * Move to previous page. Equivalent to getRange().previous(). Does nothing if getRange() returns null
    * @return Modified query.
    */
   Query<T> previousPage();

   /**
    * Move to the first page. Equivalent to getRange().first(). Does nothing if getRange returns null
    * @return Modified query.
    */
   Query<T> firstPage();


   /**
    * Move to the last page. Does nothing if getRange returns null
    * @return Modified query.
    */
   Query<T> lastPage();
}
