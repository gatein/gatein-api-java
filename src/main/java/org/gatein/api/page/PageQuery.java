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

package org.gatein.api.page;

import org.gatein.api.common.Filter;
import org.gatein.api.common.Pagination;
import org.gatein.api.internal.ObjectToStringBuilder;
import org.gatein.api.site.SiteId;
import org.gatein.api.site.SiteType;

/**
 * An immutable PageQuery object that can be used to query pages. This object is created by using the builder
 * {@link PageQuery.Builder}.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class PageQuery
{
   private final SiteType siteType;
   private final String siteName;
   private final String displayName;

   // Common query fields
   private final Pagination pagination;
   private final Filter<Page> filter;

   private PageQuery(SiteType siteType, String siteName, String displayName, Pagination pagination, Filter<Page> filter)
   {
      this.siteType = siteType;
      this.siteName = siteName;
      this.displayName = displayName;
      this.pagination = pagination;
      this.filter = filter;
   }

   public Pagination getPagination()
   {
      return pagination;
   }

   /**
    * Convenience method for creating a new PageQuery with pagination set to the next page represented by
    * by {@link org.gatein.api.common.Pagination#getNext()}
    *
    * @return a new PageQuery with pagination set to the next page.
    */
   public PageQuery nextPage()
   {
      return new Builder().from(this).withNextPage().build();
   }

   /**
    * Convenience method for creating a new PageQuery with pagination set to the previous page represented by
    * by {@link org.gatein.api.common.Pagination#getPrevious()}
    *
    * @return a new PageQuery with pagination set to the previous page.
    */
   public PageQuery previousPage()
   {
      return new Builder().from(this).withPreviousPage().build();
   }

   public Filter<Page> getFilter()
   {
      return filter;
   }

   public SiteType getSiteType()
   {
      return siteType;
   }

   public String getSiteName()
   {
      return siteName;
   }

   public String getDisplayName()
   {
      return displayName;
   }

   @Override
   public String toString()
   {
      return ObjectToStringBuilder.toStringBuilder(PageQuery.class)
         .add("siteType", siteType)
         .add("siteName", siteName)
         .add("displayName", displayName)
         .add("pagination", pagination)
         .add("filter", filter).toString();
   }

   /**
    * The builder responsible for creating {@link PageQuery} objects.
    */
   public static class Builder
   {
      public static final int DEFAULT_LIMIT = 15;
      /**
       * The default pagination used for <code>PageQuery</code>'s with a limit set to {@link Builder#DEFAULT_LIMIT}
       */
      public static final Pagination DEFAULT_PAGINATION = new Pagination(0, DEFAULT_LIMIT);

      private SiteType siteType;
      private String siteName;
      private String displayName;
      private Filter<Page> filter;
      private Pagination pagination;

      public Builder()
      {
         this.pagination = DEFAULT_PAGINATION;
      }

      /**
       * Convenience method for setting both the site type and site name.
       *
       * @param siteId the site id
       * @return the builder
       */
      public Builder withSiteId(SiteId siteId)
      {
         return withSiteType(siteId.getType()).withSiteName(siteId.getName());
      }

      /**
       * Sets the siteType for this builder to be used to query all sites that have the <code>SiteType</code>.
       *
       * @param siteType the site type
       * @return this builder
       */
      public Builder withSiteType(SiteType siteType)
      {
         this.siteType = siteType;
         return this;
      }

      /**
       * Sets the siteName for this builder to be used to query all sites that have the name.
       *
       *
       * @param siteName the site name
       * @return this builder
       */
      public Builder withSiteName(String siteName)
      {
         this.siteName = siteName;
         return this;
      }

      /**
       * Sets the display name for this builder to be used to query all sites that have the displayName.
       *
       * @param displayName the display name
       * @return this builder
       */
      public Builder withDisplayName(String displayName)
      {
         this.displayName = displayName;
         return this;
      }

      /**
       * Sets the pagination object of this builder to use the specified offset and limit.
       *
       * @param offset the offset of the pagination
       * @param limit the limit of the pagination
       * @return this builder
       */
      public Builder withPagination(int offset, int limit)
      {
         return withPagination(new Pagination(offset, limit));
      }

      /**
       * Sets the pagination object of this builder
       *
       * @param pagination the pagination object
       * @return this builder
       */
      public Builder withPagination(Pagination pagination)
      {
         this.pagination = pagination;
         return this;
      }

      /**
       * Sets the pagination of this builder to <code>pagination.getNext()</code> if it's not null.
       *
       * @return this builder
       */
      public Builder withNextPage()
      {
         if (pagination != null)
         {
            this.pagination = pagination.getNext();
         }

         return this;
      }

      /**
       * Sets the pagination of this builder to <code>pagination.getPrevious()</code> if it's not null.
       *
       * @return this builder
       */
      public Builder withPreviousPage()
      {
         if (pagination != null)
         {
            this.pagination = pagination.getPrevious();
         }

         return this;
      }

      /**
       * Sets the filter for this builder
       *
       * @param filter the filter
       * @return this builder
       */
      public Builder withFilter(Filter<Page> filter)
      {
         this.filter = filter;
         return this;
      }

      /**
       * Creates a new <code>PageQuery</code> object represented by the state of this builder
       *
       * @return a new <code>PageQuery</code> object
       */
      public PageQuery build()
      {
         return new PageQuery(siteType, siteName, displayName, pagination, filter);
      }

      /**
       * Creates a new builder from an existing <code>PageQuery</code>.
       *
       * @param query the query used to build the initial state of the builder
       * @return a new builder
       */
      public Builder from(PageQuery query)
      {
         return new Builder()
            .withSiteType(query.getSiteType())
            .withSiteName(query.getSiteName())
            .withDisplayName(query.getDisplayName())
            .withPagination(query.getPagination())
            .withFilter(query.getFilter());
      }
   }
}
