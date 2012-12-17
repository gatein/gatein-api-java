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

package org.gatein.api.portal.page;

import org.gatein.api.portal.site.SiteId;
import org.gatein.api.portal.site.SiteType;
import org.gatein.api.common.Filter;
import org.gatein.api.common.Pagination;
import org.gatein.api.util.Query;
import org.gatein.api.util.QueryBuilder;
import org.gatein.api.common.Sorting;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class PageQuery extends Query<Page>
{
   private final SiteType siteType;
   private final String siteName;
   private final String displayName;

   private PageQuery(SiteType siteType, String siteName, String displayName, Pagination pagination, Filter<Page> filter, Sorting<Page> sorting)
   {
      super(pagination, filter, sorting);
      this.siteType = siteType;
      this.siteName = siteName;
      this.displayName = displayName;
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

   public static class Builder extends QueryBuilder<Page, PageQuery, Builder>
   {
      private SiteType siteType;
      private String siteName;
      private String pageTitle;

      @Override
      public PageQuery build()
      {
         return new PageQuery(siteType, siteName, pageTitle, pagination, filter, sorting);
      }

      public Builder from(PageQuery query)
      {
         return super.from(query).withSiteType(query.getSiteType()).withSiteName(query.getSiteName())
            .withDisplayName(query.getDisplayName());
      }

      /**
       * Convenience method for setting both the site type and site name.
       *
       * @param siteId the site id corresponding to the site this query will be scoped to
       * @return the builder
       */
      public Builder withSiteId(SiteId siteId)
      {
         this.siteType = siteId.getType();
         this.siteName = siteId.getName();
         return this;
      }

      public Builder withSiteType(SiteType siteType)
      {
         this.siteType = siteType;
         return this;
      }

      public Builder withSiteName(String siteName)
      {
         this.siteName = siteName;
         return this;
      }

      public Builder withDisplayName(String displayName)
      {
         this.pageTitle = displayName;
         return this;
      }
   }
}
