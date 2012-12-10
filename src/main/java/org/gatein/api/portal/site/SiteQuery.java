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

package org.gatein.api.portal.site;

import org.gatein.api.util.Filter;
import org.gatein.api.util.Pagination;
import org.gatein.api.util.Query;
import org.gatein.api.util.QueryBuilder;
import org.gatein.api.util.Sorting;

import java.util.EnumSet;

/**
 * A site query object used to query sites.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class SiteQuery extends Query<Site>
{
   private final EnumSet<SiteType> siteTypes;
   private final boolean includeEmptySites;

   /**
    * Creates a site query object for all parameters which make up the query. The {@link SiteQuery.Builder} object
    * should be used instead to create this object.
    *
    * @param siteTypes the site types to return for this query
    * @param includeEmptySites flag if true will include sites that are empty (i.e. no navigation associated with it).
    * @see Query
    * @see SiteQuery.Builder
    */
   private SiteQuery(EnumSet<SiteType> siteTypes, boolean includeEmptySites,
                    Filter<Site> filter, Pagination pagination, Sorting<Site> sorting)
   {
      super(pagination, filter, sorting);
      this.siteTypes = siteTypes;
      this.includeEmptySites = includeEmptySites;
   }

   /**
    * The site types used for this query.
    *
    * @return the site types associated with this query.
    */
   public EnumSet<SiteType> getSiteTypes()
   {
      return siteTypes;
   }

   /**
    * The flag indicates if the query should include/exclude sites that are empty. Empty sites generally have no
    * navigation associated with them (hence cannot be viewed) which typically happens for spaces of a parent group.
    * For example the space <code>/platform</code> is the parent group of <code>/platform/administrators</code>, however
    * the /platform space should never be returned as a site in most cases.
    *
    * @return true if the query should return sites that are empty, false otherwise (default)
    */
   public boolean isIncludeEmptySites()
   {
      return includeEmptySites;
   }

   /**
    * Convenience method for creating a new SiteQuery with pagination set to the next page represented by
    * by {@link org.gatein.api.util.Pagination#getNext()}
    *
    * @return a new SiteQuery with pagination set to the next page.
    */
   public SiteQuery nextPage()
   {
      return new Builder().from(this).withNextPage().build();
   }

   /**
    * Convenience method for creating a new SiteQuery with pagination set to the previous page represented by
    * by {@link org.gatein.api.util.Pagination#getPrevious()}
    *
    * @return a new SiteQuery with pagination set to the previous page.
    */
   public SiteQuery previousPage()
   {
      return new Builder().from(this).withPreviousPage().build();
   }

   /**
    * The builder class responsible for building SiteQuery objects.
    *
    * @see SiteQuery
    */
   public static class Builder extends QueryBuilder<Site, SiteQuery, Builder>
   {
      private EnumSet<SiteType> siteTypes = EnumSet.of(SiteType.SITE);
      private boolean emptySites = false;

      /**
       *
       * @param query the query object
       * @return this builder
       */
      public Builder from(SiteQuery query)
      {
         return super.from(query).withSiteTypes(query.getSiteTypes()).includeEmptySites(query.isIncludeEmptySites());
      }

      /**
       * @see SiteQuery#getSiteTypes
       * @return this builder
       */
      public Builder withSiteTypes(SiteType first, SiteType...rest)
      {
         return withSiteTypes(EnumSet.of(first, rest));
      }

      public Builder withSiteTypes(EnumSet<SiteType> siteTypes)
      {
         this.siteTypes = EnumSet.copyOf(siteTypes);
         return this;
      }

      public Builder withAllSiteTypes()
      {
         this.siteTypes = EnumSet.allOf(SiteType.class);
         return this;
      }

      /**
       * @see SiteQuery#isIncludeEmptySites
       * @return this builder
       */
      public Builder includeEmptySites(boolean emptySites)
      {
         this.emptySites = emptySites;
         return this;
      }

      /**
       * Builds a new SiteQuery object based on the builder methods called.
       *
       * @return a new SiteQuery
       */
      @Override
      public SiteQuery build()
      {
         if (siteTypes == null || siteTypes.isEmpty()) siteTypes = EnumSet.of(SiteType.SITE);

         return new SiteQuery(siteTypes, emptySites, filter, pagination, sorting);
      }
   }
}
