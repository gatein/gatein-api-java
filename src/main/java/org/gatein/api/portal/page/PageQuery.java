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

import org.gatein.api.portal.site.Site;
import org.gatein.api.util.Filter;
import org.gatein.api.util.Pagination;
import org.gatein.api.util.Query;
import org.gatein.api.util.QueryBuilder;
import org.gatein.api.util.Sorting;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class PageQuery extends Query<Page>
{
   public PageQuery(Pagination pagination, Filter<Page> filter, Sorting sorting)
   {
      super(pagination, filter, sorting);
   }

   public static class Builder extends QueryBuilder<Page, PageQuery, Builder>
   {
      private Site.Id siteId;

      @Override
      public PageQuery build()
      {
         return new PageQuery(pagination, filter, sorting);
      }

      public Builder withSiteId(Site.Id siteId)
      {
         this.siteId = siteId;
         return this;
      }
   }
}
