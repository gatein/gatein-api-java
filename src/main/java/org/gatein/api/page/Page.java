/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.gatein.api.page;

import java.io.Serializable;

import org.gatein.api.common.Describable;
import org.gatein.api.common.Displayable;
import org.gatein.api.composition.Container;
import org.gatein.api.security.Permission;
import org.gatein.api.site.SiteId;

/**
 * Represents a portal page, that can later be associated with a Node. Note that an existing instance of a Page is not
 * indicative that the page exists on the permanent storage.
 * <p>
 * Implementations must use {@link #DEFAULT_EDIT_PERMISSION} as a default
 * that is returned by {@link #getEditPermission()} unless editPermission is set explicitly.
 *
 * @see org.gatein.api.Portal#savePage(Page)
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Page extends Container, Displayable, Describable, Comparable<Page>, Serializable {

    /**
     * Implementations must use this constant as a default value
     * returned by {@link #getEditPermission()} unless editPermission is set explicitly.
     */
    public static Permission DEFAULT_EDIT_PERMISSION = Permission.any("platform", "administrators");

    /**
     * The id of the page
     *
     * @return the id
     */
    PageId getId();

    /**
     * The id of the site the page belongs to
     *
     * @return the site id
     */
    SiteId getSiteId();

    /**
     * The name of the page
     *
     * @return the name
     */
    String getName();

    /**
     * The permissions that represents what users are allowed to modify the page.
     * <p>
     * If not set explicitly, returns {@link #DEFAULT_EDIT_PERMISSION}.
     *
     * @return the edit permission
     */
    Permission getEditPermission();

    /**
     * Sets the edit permission.
     * <p>
     * Unless set explicitly, the default value {@link #DEFAULT_EDIT_PERMISSION} is effective.
     *
     * @param permission the edit permission
     */
    void setEditPermission(Permission permission);

    /**
     * The page's title
     * @return the page's title
     */
    String getTitle();

    /**
     * Sets the title of the page
     * @param title the title to set
     */
    void setTitle(String title);
}
