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

package org.gatein.api.composition;

import java.util.List;

import org.gatein.api.application.Application;
import org.gatein.api.page.Page;
import org.gatein.api.security.Permission;

/**
 * A common parent for {@link Page} and {@link Container} covering the children and permissions aspects.
 * <p>
 * The children are supposed to implement the {@link ContainerItem} interface. There are two
 * subinterfaces available at present: {@link Application} and {@link Container}.
 * <p>
 * Implementations must use {@link #DEFAULT_ACCESS_PERMISSION},
 * {@link #DEFAULT_MOVE_APPS_PERMISSION} and {@link #DEFAULT_MOVE_CONTAINERS_PERMISSION} as defaults
 * that are returned by {@link #getAccessPermission()}, {@link #getMoveAppsPermission()} and
 * {@link #getMoveContainersPermission()} respectively, if those were not set explicitly.
 *
 * @author <a href="mailto:ppalaga@redhat.com">Peter Palaga</a>
 *
 */
public interface BareContainer {

    /**
     * Implementations must use this constant as a default value
     * returned by {@link #getAccessPermission()} unless accessPermission is set explicitly.
     */
    public static Permission DEFAULT_ACCESS_PERMISSION = Permission.everyone();
    /**
     * Implementations must use this constant as a default value
     * returned by {@link #getMoveAppsPermission()} unless moveAppsPermission is set explicitly.
     */
    public static Permission DEFAULT_MOVE_APPS_PERMISSION = Permission.everyone();
    /**
     * Implementations must use this constant as a default value
     * returned by {@link #getMoveContainersPermission()} unless moveContainersPermission is set explicitly.
     */
    public static Permission DEFAULT_MOVE_CONTAINERS_PERMISSION = Permission.everyone();

    /**
     * Returns all child {@link org.gatein.api.composition.ContainerItem}s included in this container.
     * Returns a reference to the internal modifiable {@link List} instance. Therefore, it is legal to change
     * the returned list but note that any changes are not persisted until you
     * call e.g. {@link org.gatein.api.Portal#savePage(org.gatein.api.page.Page)}.
     *
     * @return  The children of this container
     */
    public List<ContainerItem> getChildren();

    /**
     * Replaces the current list of children on this container by the one provided as parameter.
     */
    public void setChildren(List<ContainerItem> children);

    /**
     * Gets a permission object that represents which users are allowed to access this container.
     * <p>
     * If not set explicitly, returns {@link #DEFAULT_ACCESS_PERMISSION}.
     *
     * @return the access permission
     */
    Permission getAccessPermission();

    /**
     * Optionally, sets the access permission.
     * <p>
     * Unless set explicitly, the default value {@link #DEFAULT_ACCESS_PERMISSION} is effective.
     *
     * @param permission the access permission
     */
    void setAccessPermission(Permission permission);

    /**
     * Gets a permission object that represents which users are allowed to perform move, add
     * and remove operations with child {@link Application}s of this {@link Container}.
     * <p>
     * If not set explicitly, returns {@link #DEFAULT_MOVE_APPS_PERMISSION}.
     *
     * @return the move apps permission
     */
    public Permission getMoveAppsPermission();

    /**
     * Sets the move apps permission for this container.
     * <p>
     * Unless set explicitly, the default value {@link #DEFAULT_MOVE_APPS_PERMISSION} is effective.
     *
     * @param moveAppsPermission    the move apps permission
     */
    public void setMoveAppsPermission(Permission moveAppsPermission);

    /**
     * Gets a permission object that represents which users are allowed to perform move, add
     * and remove operations with child {@link Container}s of this {@link Container}.
     * <p>
     * If not set explicitly, returns {@link #DEFAULT_MOVE_CONTAINERS_PERMISSION}.
     *
     * @return the move containers permission
     */
    public Permission getMoveContainersPermission();

    /**
     * Sets the move container permission for this container.
     * <p>
     * Unless set explicitly, the default value {@link #DEFAULT_MOVE_CONTAINERS_PERMISSION} is effective.
     *
     * @param moveContainersPermission    the move containers permission
     */
    public void setMoveContainersPermission(Permission moveContainersPermission);

}
