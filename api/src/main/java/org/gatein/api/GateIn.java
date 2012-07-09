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

import org.gatein.api.commons.Filter;
import org.gatein.api.exception.EntityNotFoundException;
import org.gatein.api.portal.Site;
import org.gatein.api.portal.SiteQuery;
import org.gatein.api.commons.Range;
import org.gatein.api.commons.PropertyType;

import java.util.List;

/**
 * Main entry point to GateIn Portal Public API
 *
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @author <a href="mailto:bdawidow@redhat.com">Boleslaw Dawidowicz</a>
 */
public interface GateIn
{
   String GATEIN_API = "org.gatein.api.instance";

   /**
    * Return all sites
    *
    * @return List of sites
    */
   List<Site> getSites();

   /**
    * Return sites
    *
    * @param range The range that will limit returned results
    * @return List of sites
    */
   List<Site> getSites(Range range);

   /**
    * Return sites
    *
    * @param siteType type of sites to be returned
    * @return List of sites
    */
   List<Site> getSites(Site.Type siteType);

   /**
    * Return sites
    *
    * @param siteType The type of sites to be returned
    * @param range The range that will limit returned results
    * @return List of sites
    */
   List<Site> getSites(Site.Type siteType, Range range);

   /**
    * Return sites
    *
    * @param filter the filter to filter sites from returned list
    * @return List of sites
    */
   List<Site> getSites(Filter<Site> filter);

   /**
    * Return sites
    *
    * @param filter the filter to filter sites from returned list
    * @param range The range that will limit returned results
    * @return List of sites
    */
   List<Site> getSites(Filter<Site> filter, Range range);

   /**
    *
    * @param siteId The id of site to be returned
    * @return The site
    */
   Site getSite(Site.Id siteId);

   /**
    *
    * @param type The type of syte to be returned
    * @param name The name of site to be returned
    * @return The site
    */
   Site getSite(Site.Type type, String name);

   /**
    *
    * @return The default site in portal container
    */
   Site getDefaultSite();

   /**
    * Create SiteQuery
    *
    * @return SiteQuery instance
    */
   SiteQuery<Site> createSiteQuery();

   /**
    * Add new site to portal container
    *
    * @param id The id of new site to create
    * @return Created site
    */
   Site addSite(Site.Id id);

   /**
    * Add new site to portal container
    *
    * @param siteType The type of site to be created
    * @param name The name of site to be created
    * @return Created site
    */
   Site addSite(Site.Type siteType, String name);

   /**
    * Remove site from portal container
    *
    * @param siteId The id of site to be removed
    * @throws EntityNotFoundException
    */
   void removeSite(Site.Id siteId) throws EntityNotFoundException;

   /**
    * Remove site from portal container
    *
    * @param siteType The type of site to be removed
    * @param name The name of site to be removed
    * @throws EntityNotFoundException
    */
   void removeSite(Site.Type siteType, String name) throws EntityNotFoundException;

   /**
    *
    * @param property The type of property to be obtained
    * @param <T> The value type of property to be obtained
    * @return Property value
    */
   <T> T getProperty(PropertyType<T> property);

   /**
    *
    * @param property The type of property to be stored
    * @param value The property value to be stored
    * @param <T> The value type of property to be stored
    */
   <T> void setProperty(PropertyType<T> property, T value);

}
