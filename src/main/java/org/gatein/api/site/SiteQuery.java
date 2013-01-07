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

package org.gatein.api.site;

import org.gatein.api.Parameters;
import org.gatein.api.common.Filter;
import org.gatein.api.common.Pagination;
import org.gatein.api.common.Sorting;

import java.util.EnumSet;

/**
 * An immutable SiteQuery object that can be used to query sites. This object is created by using the builder
 * {@link SiteQuery.Builder}.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class SiteQuery {
    private final EnumSet<SiteType> siteTypes;
    private final boolean includeEmptySites;
    private final Filter<Site> filter;
    private final Pagination pagination;
    private final Sorting<Site> sorting;

    /**
     * Creates a site query object for all parameters which make up the query. The {@link SiteQuery.Builder} object should be
     * used instead to create this object.
     *
     * @param siteTypes the site types to return for this query
     * @param includeEmptySites flag if true will include sites that are empty (i.e. no navigation associated with it).
     * @see SiteQuery.Builder
     */
    private SiteQuery(EnumSet<SiteType> siteTypes, boolean includeEmptySites, Filter<Site> filter, Pagination pagination,
            Sorting<Site> sorting) {
        this.siteTypes = siteTypes;
        this.includeEmptySites = includeEmptySites;
        this.filter = filter;
        this.pagination = pagination;
        this.sorting = sorting;
    }

    /**
     * The site types used for this query.
     *
     * @return the site types associated with this query.
     */
    public EnumSet<SiteType> getSiteTypes() {
        return siteTypes;
    }

    /**
     * The flag indicates if the query should include/exclude sites that are empty. Empty sites generally have no navigation
     * associated with them (hence cannot be viewed) which typically happens for spaces of a parent group. For example the space
     * <code>/platform</code> is the parent group of <code>/platform/administrators</code>, however the /platform space should
     * never be returned as a site in most cases.
     *
     * @return true if the query should return sites that are empty, false otherwise (default)
     */
    public boolean isIncludeEmptySites() {
        return includeEmptySites;
    }

    public Filter<Site> getFilter() {
        return filter;
    }

    public Sorting<Site> getSorting() {
        return sorting;
    }

    public Pagination getPagination() {
        return pagination;
    }

    /**
     * Convenience method for creating a new SiteQuery with pagination set to the next page represented by by
     * {@link org.gatein.api.common.Pagination#getNext()}
     *
     * @return a new SiteQuery with pagination set to the next page.
     */
    public SiteQuery nextPage() {
        return new Builder().from(this).withNextPage().build();
    }

    /**
     * Convenience method for creating a new SiteQuery with pagination set to the previous page represented by by
     * {@link org.gatein.api.common.Pagination#getPrevious()}
     *
     * @return a new SiteQuery with pagination set to the previous page.
     */
    public SiteQuery previousPage() {
        return new Builder().from(this).withPreviousPage().build();
    }

    /**
     * The builder class responsible for building SiteQuery objects.
     *
     * @see SiteQuery
     */
    public static class Builder {
        /**
         * The default limit used for pagination
         */
        public static final int DEFAULT_LIMIT = 15;
        /**
         * The default pagination used for <code>SiteQuery</code>'s with a limit set to {@link Builder#DEFAULT_LIMIT}
         */
        public static final Pagination DEFAULT_PAGINATION = new Pagination(0, DEFAULT_LIMIT);

        /**
         * Default site type used to build SiteQuery's if one is not defined.
         */
        public static final SiteType DEFAULT_SITE_TYPE = SiteType.SITE;

        private EnumSet<SiteType> siteTypes;
        private boolean emptySites;
        private Filter<Site> filter;
        private Pagination pagination;
        private Sorting<Site> sorting;

        public Builder() {
            pagination = DEFAULT_PAGINATION;
            siteTypes = EnumSet.of(DEFAULT_SITE_TYPE);
        }

        /**
         * Sets the site types of this builder
         *
         * @param first the site type this builder is to contain
         * @param rest the remaining site type's this builder is to contain
         * @return this builder
         */
        public Builder withSiteTypes(SiteType first, SiteType... rest) {
            Parameters.requireNonNull(first, "first");

            return withSiteTypes(EnumSet.of(first, rest));
        }

        /**
         * Sets the site types of this builder.
         *
         * @param siteTypes the set of siteTypes to set for this builder.
         * @return this builder
         */
        public Builder withSiteTypes(EnumSet<SiteType> siteTypes) {
            this.siteTypes = EnumSet.copyOf(Parameters.requireNonNull(siteTypes, "siteTypes"));
            return this;
        }

        /**
         * Sets the site type's of this builder to include all <code>SiteType</code>'s
         *
         * @return this builder
         */
        public Builder withAllSiteTypes() {
            this.siteTypes = EnumSet.allOf(SiteType.class);
            return this;
        }

        /**
         * @see SiteQuery#isIncludeEmptySites
         * @return this builder
         */
        public Builder includeEmptySites(boolean emptySites) {
            this.emptySites = emptySites;
            return this;
        }

        /**
         * Sets the pagination object of this builder to use the specified offset and limit.
         *
         * @param offset the offset of the pagination
         * @param limit the limit of the pagination
         * @return this builder
         */
        public Builder withPagination(int offset, int limit) {
            return withPagination(new Pagination(offset, limit));
        }

        /**
         * Sets the pagination object of this builder
         *
         * @param pagination the pagination object
         * @return this builder
         */
        public Builder withPagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        /**
         * Sets the pagination of this builder to <code>pagination.getNext()</code> if it's not null.
         *
         * @return this builder
         */
        public Builder withNextPage() {
            if (pagination != null) {
                this.pagination = pagination.getNext();
            }

            return this;
        }

        /**
         * Sets the pagination of this builder to <code>pagination.getPrevious()</code> if it's not null.
         *
         * @return this builder
         */
        public Builder withPreviousPage() {
            if (pagination != null) {
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
        public Builder withFilter(Filter<Site> filter) {
            this.filter = filter;
            return this;
        }

        /**
         * Sets the sorting object for this builder
         *
         * @param sorting the sorting object
         * @return this builder
         */
        public Builder withSorting(Sorting<Site> sorting) {
            this.sorting = sorting;
            return this;
        }

        /**
         * Sets the order of the sorting object of this builder to <code>Sorting.Order.ascending</code>
         *
         * @return this builder
         */
        public Builder ascending() {
            sorting = new Sorting<Site>(Sorting.Order.ascending);
            return this;
        }

        /**
         * Sets the order of the sorting object of this builder to <code>Sorting.Order.descending</code>
         *
         * @return this builder
         */
        public Builder descending() {
            sorting = new Sorting<Site>(Sorting.Order.descending);
            return this;
        }

        /**
         * Creates a new SiteQuery object represented by the state this builder
         *
         * @return a new SiteQuery object
         */
        public SiteQuery build() {
            if (siteTypes == null || siteTypes.isEmpty())
                siteTypes = EnumSet.of(SiteType.SITE);

            return new SiteQuery(siteTypes, emptySites, filter, pagination, sorting);
        }

        /**
         * Creates a new builder from an existing SiteQuery.
         *
         * @param query the query used to build the initial state of the builder
         * @return a new builder
         */
        public Builder from(SiteQuery query) {
            return new Builder().includeEmptySites(query.isIncludeEmptySites()).withSiteTypes(query.getSiteTypes())
                    .withFilter(query.getFilter()).withPagination(query.getPagination()).withSorting(query.getSorting());
        }
    }
}
