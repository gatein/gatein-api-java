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

package org.gatein.api;

import org.gatein.api.application.Application;
import org.gatein.api.application.ApplicationRegistry;
import org.gatein.api.navigation.Navigation;
import org.gatein.api.oauth.OAuthProvider;
import org.gatein.api.page.Page;
import org.gatein.api.composition.Container;
import org.gatein.api.composition.PageBuilder;
import org.gatein.api.page.PageId;
import org.gatein.api.page.PageQuery;
import org.gatein.api.site.Site;
import org.gatein.api.site.SiteId;
import org.gatein.api.site.SiteQuery;
import org.gatein.api.security.Permission;
import org.gatein.api.security.User;

import java.util.List;

/**
 * The main interface of the portal public API. This is available from the <code>PortalRequest</code> object which can be
 * obtained from {@link PortalRequest#getInstance()}.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public interface Portal {
    /**
     * Returns a site given the <code>SiteId</code>. Can return null if the site does not exist.
     *
     * @param siteId the siteId
     * @return the site or null if the site does not exist
     * @throws IllegalArgumentException if siteId is null
     * @throws ApiException if something prevented this operation to succeed
     */
    Site getSite(SiteId siteId);

    /**
     * Creates a a site given the <code>SiteId</code>. This site is not saved until
     * {@link Portal#saveSite(org.gatein.api.site.Site)} is called. Will use the default site template configured by
     * the portal.
     *
     * @param siteId the site id
     * @return the new site which has not been saved yet.
     * @throws IllegalArgumentException if siteId is null
     * @throws EntityAlreadyExistsException if the site already exists
     * @throws ApiException if something prevented this operation to succeed
     */
    Site createSite(SiteId siteId);

    /**
     * Creates a a site given the <code>SiteId</code>. This site is not saved until
     * {@link Portal#saveSite(org.gatein.api.site.Site)} is called.
     *
     * @param siteId the site id
     * @param templateName the name of the template to use to create the site
     * @return the new site which has not been saved yet.
     * @throws IllegalArgumentException if siteId is null
     * @throws EntityAlreadyExistsException if the site already exists
     * @throws ApiException if something prevented this operation to succeed
     */
    Site createSite(SiteId siteId, String templateName);

    /**
     * Finds sites given the <code>SiteQuery</code>
     *
     * @param query the site query
     * @return list of sites found. The list will be empty if no sites were found.
     * @throws IllegalArgumentException if query is null
     * @throws ApiException if something prevented this operation to succeed
     */
    List<Site> findSites(SiteQuery query);

    /**
     * Saves a site
     *
     * @param site the site to save
     * @throws IllegalArgumentException if site is null
     * @throws ApiException if an exception occurred trying to save the site
     */
    void saveSite(Site site);

    /**
     * Removes a site
     *
     * @param siteId the id of the site to remove
     * @return true if the site was removed, false otherwise
     * @throws IllegalArgumentException if siteId is null
     * @throws ApiException if something prevented this operation to succeed
     */
    boolean removeSite(SiteId siteId);

    /**
     * Returns the navigation of a site given the <code>SiteId</code>. Can return null if the navigation does not exist.
     *
     * @param siteId the site id
     * @return navigation for a site, or null if the navigation does not exist
     * @throws IllegalArgumentException if siteId is null
     * @throws ApiException if something prevented this operation to succeed
     */
    Navigation getNavigation(SiteId siteId);

    /**
     * Returns a representation of the Application Registry.
     * @return a representation of the Application Registry.
     */
    ApplicationRegistry getApplicationRegistry();

    /**
     * Returns the page of a site given the <code>PageId</code>. Can return null if the page does not exist.
     *
     * @param pageId the page id
     * @return the page or null if the page does not exist
     * @throws IllegalArgumentException if pageId is null
     * @throws ApiException if something prevented this operation to succeed
     */
    Page getPage(PageId pageId);

    /**
     * Creates a page for a site given the <code>PageId</code>. This page is not saved until
     * {@link Portal#savePage(org.gatein.api.page.Page)} is called.
     *
     * @param pageId the page id
     * @return the new page which has not been saved yet.
     * @throws IllegalArgumentException if pageId is null
     * @throws EntityAlreadyExistsException if the page already exists
     * @throws EntityNotFoundException if the site does not exist
     * @throws ApiException if something prevented this operation to succeed
     */
    Page createPage(PageId pageId);

    /**
     * Finds pages given the <code>PageQuery</code>
     *
     * @param query the page query
     * @return list of pages found. List is empty if no pages were found.
     * @throws IllegalArgumentException if query is null
     * @throws ApiException if something prevented this operation to succeed
     */
    List<Page> findPages(PageQuery query);

    /**
     * Saves a page
     *
     * @param page the page to save
     * @throws IllegalArgumentException if page is null
     * @throws ApiException if something prevented this operation to succeed
     */
    void savePage(Page page);

    /**
     * Removes a page
     *
     * @param pageId the id of the page to remove
     * @return true if the page was removed, false otherwise
     * @throws IllegalArgumentException if pageId is null
     * @throws ApiException if something prevented this operation to succeed
     */
    boolean removePage(PageId pageId);

    /**
     * Returns true if the given user has the rights represented by the permission
     *
     * @param user the user
     * @param permission the permission
     * @return true if the user has rights represented by the permission
     * @throws ApiException if something prevented this operation to succeed
     */
    boolean hasPermission(User user, Permission permission);

    /**
     * Return {@link org.gatein.api.oauth.OAuthProvider} for given key. Key could be {@link OAuthProvider#FACEBOOK},
     * {@link OAuthProvider#GOOGLE}, {@link OAuthProvider#TWITTER} or other OAuth provider registered in Portal via OAuth SPI
     *
     * @param oauthProviderKey Key of OAuth provider
     * @return OAuth provider or null if OAuth provider with given key was not found
     */
    OAuthProvider getOAuthProvider(String oauthProviderKey);

    /**
     * Returns a new {@link org.gatein.api.composition.PageBuilder} instance, which can be used to
     * compose page content out of {@link Container}s and {@link Application}s. Note that the page built
     * through a {@link PageBuilder} page is not saved until
     * {@link Portal#savePage(org.gatein.api.page.Page)} is called.
     *
     * @return a new instance of the default implementation for PageBuilder
     */
    PageBuilder newPageBuilder();
}
