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


import org.gatein.api.commons.Query;
import org.gatein.api.commons.Range;
import org.gatein.api.portal.site.Site;

/**
 * Enables performing more complex queries for sites.
 *
 * @author <a href="mailto:bdawidow@redhat.com">Boleslaw Dawidowicz</a>
 */
public interface SiteQuery<T extends Site> extends Query<Site>
{

   //TODO: sortByProperty? Property value conditions?

   // Conditions

   /**
    *
    * @param id
    * @return
    */
   SiteQuery<T> setId(Site.Id id);

   /**
    *
    * @return
    */
   Site.Id getId();

   /**
    *
    * @param siteType
    * @return
    */
   SiteQuery<T> setType(Site.Type siteType);

   /**
    *
    * @return
    */
   Site.Type getType();

   SiteQuery<T> setUserId(String userId);

   String getUserId();

   SiteQuery<T> setGroupId(String groupId);

   SiteQuery<T> setGroupId(String... groupId);

   String getGroupId();

   SiteQuery<T> containNavigation(boolean option);

   boolean isContainNavigation();


   // General

   /**
    * Reset query to clean state and revert any modifications
    * @return Modified query
    */
   SiteQuery<T> reset();

   /**
    *
    * @return Immutable version of a query
    */
   SiteQuery<T> immutable();

   /**
    *
    * @return Clone - new instance - of a query
    */
   SiteQuery<T> clone();

   /**
    * Set if query should be sorted
    *
    * @param ascending - if true query will be sorted in ascending order
    * @return Modified query
    */
   SiteQuery<T> sort(boolean ascending);

   /**
    * @param range Range object defining starting point and number of returned results - a page.
    *              If null range will be removed.
    * @return Modified query
    */
   SiteQuery<T> setRange(Range range);

   /**
    * Does nothing if getRange() returns null
    *
    * @param number Current page index
    * @return Modified query.
    */
   SiteQuery<T> setPage(int number);

   /**
    * Move to next page. Equivalent to getRange().next(). Does nothing if getRange() returns null
    * @return Modified query.
    */
   SiteQuery<T> nextPage();

   /**
    * Move to previous page. Equivalent to getRange().previous(). Does nothing if getRange() returns null
    * @return Modified query.
    */
   SiteQuery<T> previousPage();

   /**
    * Move to the first page. Equivalent to getRange().first(). Does nothing if getRange returns null
    * @return Modified query.
    */
   SiteQuery<T> firstPage();

   /**
    * Move to the last page. Does nothing if getRange returns null
    * @return Modified query.
    */
   SiteQuery<T> lastPage();

}
