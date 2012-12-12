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

import org.gatein.api.portal.page.PageNotFoundException;
import org.gatein.api.portal.Permission;
import org.gatein.api.portal.site.SiteNotFoundException;
import org.gatein.api.portal.User;
import org.gatein.api.portal.navigation.Navigation;
import org.gatein.api.portal.page.Page;
import org.gatein.api.portal.page.PageId;
import org.gatein.api.portal.page.PageQuery;
import org.gatein.api.portal.site.Site;
import org.gatein.api.portal.site.SiteId;
import org.gatein.api.portal.site.SiteQuery;

import java.util.List;

/**
 * The main interface of the portal public API. This is available from the <code>PortalRequest</code> object which
 * can be obtained from {@link PortalRequest#getInstance()}.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Portal
{
   /**
    * Returns a site given the <code>SiteId</code>. Can return null if the site does not exist.
    *
    * @param siteId the siteId is null
    * @return the site or null if the site does not exist
    * @throws IllegalArgumentException if id is null
    */
   Site getSite(SiteId siteId) throws IllegalArgumentException;

   /**
    * Finds sites given the <code>SiteQuery</code>
    *
    * @param query the site query
    * @return list of sites found. The list will be empty if no sites were found.
    * @throws IllegalArgumentException if query is null
    */
   List<Site> findSites(SiteQuery query) throws IllegalArgumentException;

   /**
    * Saves a site
    *
    * @param site the site to save
    * @throws IllegalArgumentException if site is null
    * @throws ApiException             if an exception occurred trying to save the site
    */
   void saveSite(Site site) throws IllegalArgumentException, ApiException;

   /**
    * Removes a site
    *
    * @param siteId the id of the site to remove
    * @return true if the site was removed, false otherwise
    * @throws IllegalArgumentException if siteId is null
    * @throws org.gatein.api.portal.site.SiteNotFoundException
    *                                  if the site could not be found
    */
   boolean removeSite(SiteId siteId) throws IllegalArgumentException, SiteNotFoundException;

   /**
    * Returns the navigation of a site given the <code>SiteId</code>. Can return null if the navigation does not exist.
    *
    * @param siteId the site id
    * @return navigation for a site, or null if the navigation does not exist
    * @throws IllegalArgumentException if siteId is null
    * @throws SiteNotFoundException    if the site does not exist for the given site id.
    */
   Navigation getNavigation(SiteId siteId) throws IllegalArgumentException, SiteNotFoundException;

   /**
    * Returns the page of a site given the <code>PageId</code>. Can return null if the page does not exist.
    *
    * @param pageId the page id
    * @return the page or null if the page does not exist
    * @throws IllegalArgumentException if pageId is null
    * @throws SiteNotFoundException    if the site does not exist
    */
   Page getPage(PageId pageId) throws IllegalArgumentException, SiteNotFoundException;

   /**
    * Creates a page for a site given the <code>PageId</code>. This page is not saved until {@link Portal#savePage(org.gatein.api.portal.page.Page)} is called.
    *
    * @param pageId the page id
    * @return the new page which has not been saved yet.
    * @throws IllegalArgumentException     if pageId is null
    * @throws EntityAlreadyExistsException if the page already exists
    * @throws SiteNotFoundException        if the site does not exist
    */
   Page createPage(PageId pageId) throws IllegalArgumentException, EntityAlreadyExistsException, SiteNotFoundException;

   /**
    * Finds pages given the <code>PageQuery</code>
    *
    * @param query the page query
    * @return list of pages found. List is empty if no pages were found.
    * @throws IllegalArgumentException if query is null
    */
   List<Page> findPages(PageQuery query) throws IllegalArgumentException;

   /**
    * Saves a page
    *
    * @param page the page to save
    * @throws IllegalArgumentException if page is null
    * @throws ApiException             if an exception occurred trying to save the page
    */
   void savePage(Page page) throws IllegalArgumentException, ApiException;

   /**
    * Removes a page
    *
    * @param pageId the id of the page to remove
    * @return true if the page was removed, false otherwise
    * @throws SiteNotFoundException if the site was not found
    * @throws PageNotFoundException if the page was not found
    */
   boolean removePage(PageId pageId) throws SiteNotFoundException, PageNotFoundException;

   /**
    * Returns true if the given user has the rights represented by the permission
    *
    * @param user       the user
    * @param permission the permission
    * @return true if the user has rights represented by the permission
    */
   boolean hasPermission(User user, Permission permission);
}
