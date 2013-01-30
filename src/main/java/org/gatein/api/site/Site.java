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

import java.io.Serializable;
import java.util.Locale;

import org.gatein.api.Portal;
import org.gatein.api.common.Attributes;
import org.gatein.api.common.Describable;
import org.gatein.api.common.Displayable;
import org.gatein.api.security.Permission;

/**
 * Represents a portal site. A portal site can be a standard site, a group space or a user dashboard. Any chances to a site is
 * not perisisted until {@link Portal#saveSite(Site)} is invoked.
 * 
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Site extends Displayable, Describable, Comparable<Site>, Serializable {
    /**
     * Returns the id of the site
     * 
     * @return the id of the site
     */
    SiteId getId();

    /**
     * Returns the type of the site. This can be a standard site, a group space or a user dashboard.
     * 
     * @return the type of the site
     */
    SiteType getType();

    /**
     * Returns the name of the site
     * 
     * @return the name of the site
     */
    String getName();

    /**
     * Returns the default locale associated with the site
     * 
     * @return the locale
     */
    Locale getLocale();

    /**
     * Changes the default locale associated with the site
     * 
     * @param locale
     */
    public void setLocale(Locale locale);

    /**
     * Returns the skin associated with the site
     * 
     * @return the skin
     */
    public String getSkin();

    /**
     * Changes the skin associated with the site
     * 
     * @param skin the skin
     */
    public void setSkin(String skin);

    /**
     * Returns the attributes (properties) associated with the site
     * 
     * @return
     */
    public Attributes getAttributes();

    /**
     * The permissions that represents what users are allowed to access the site
     * 
     * @return the access permission
     */
    public Permission getAccessPermission();

    /**
     * Changes the access permission for the site
     * 
     * @param permission the access permission
     */
    public void setAccessPermission(Permission permission);

    /**
     * The permissions that represents what users are allowed to modify the site
     * 
     * @return the edit permission
     */
    public Permission getEditPermission();

    /**
     * Changes the edit permission for the site
     * 
     * @param permission the edit permission
     */
    public void setEditPermission(Permission permission);

    /**
     * Contains attribute keys for default site attributes
     * 
     * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
     */
    public static final class AttributeKeys {
        /**
         * The site attribute that sets if a session is kept alive or not. Valid values are 'onDemand', 'always',
         * and 'never' with the default being 'onDemand'.
         */
        public static final Attributes.Key<String> SESSION_BEHAVIOR = Attributes.key("sessionAlive", String.class);
        /**
         * The site attribute that sets if the info bar is shown by default when adding applications to pages, default 'false'
         */
        public static final Attributes.Key<Boolean> SHOW_PORTLET_INFO_BAR = Attributes.key("showPortletInfo", Boolean.class);

        private AttributeKeys() {
        }
    }
}