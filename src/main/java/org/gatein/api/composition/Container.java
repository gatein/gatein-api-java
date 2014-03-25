package org.gatein.api.composition;

import java.util.List;

import org.gatein.api.application.Application;
import org.gatein.api.page.Page;
import org.gatein.api.security.Permission;

/**
 *
 * A block of content to be displayed on a {@link Page}. Its main responsibility is to provide
 * a template that renders the children of this {@link Container} in the user interface.
 * <p>
 * The children are supposed to implement the {@link ContainerItem} interface. There are two
 * subinterfaces available at present: {@link Application} and {@link Container}.
 *
 * Implementations must use {@link #DEFAULT_ACCESS_PERMISSION},
 * {@link #DEFAULT_MOVE_APPS_PERMISSION} and {@link #DEFAULT_MOVE_CONTAINERS_PERMISSION} as defaults
 * that are returned by {@link #getAccessPermission()}, {@link #getMoveAppsPermission()} and
 * {@link #getMoveContainersPermission()} respectively, if those were not set explicitly.
 *
 * @author <a href="mailto:jpkroehling+javadoc@redhat.com">Juraci Paixão Kröhling</a>
 */
public interface Container extends ContainerItem {

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
     * Returns a reference to the internal modifiable instance. Therefore, it is legal to change
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
     * Returns the internal path to the Groovy template that should be used when rendering this container. Similar
     * to the value at portal-config/portal-layout/portlet-application/portlet/preferences/preference/name=template/value
     * on the portal.xml file.
     * @return the internal path to the container's template file
     */
    public String getTemplate();

    /**
     * Sets which template is to be used by this container. Note that a consumer would almost never set
     * this, as the default templates are set already by the implementations.
     */
    public void setTemplate(String template);


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
