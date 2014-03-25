package org.gatein.api.composition;

import java.util.List;

import org.gatein.api.application.Application;
import org.gatein.api.security.Permission;

/**
 * Basic builder for {@link Container}s.
 *
 * The basic flow of calls to this should be:
 * <ul>
 *     <li>
 *         {@link ContainerBuilder#child(ContainerItem)}
 *         to add a new child object to this {@link Container}
 *     </li>
 *     <li>
 *         {@link ContainerBuilder#newCustomContainerBuilder(Container)},
 *         {@link ContainerBuilder#newColumnsBuilder()} and
 *         {@link ContainerBuilder#newRowsBuilder()}
 *         add a new {@link Container} as a child of this,
 *         and returns the builder related to this new container
 *     </li>
 *     <li>
 *         {@link ContainerBuilder#buildToParentBuilder()} when the caller has finished adding child items
 *     </li>
 *     <li>
 *         {@link ContainerBuilder#buildToTopBuilder()} to finish the work on this builder and return to the top level builder.
 *     </li>
 * </ul>
 *
 * The new*Builder are very similar in behavior, differing only on the final container that it delivers. For instance,
 * a representation of a column would be a {@link Container} object with a specific template.
 *
 * @param <T> The final type that is implementing this interface.
 * @author <a href="mailto:jpkroehling+javadoc@redhat.com">Juraci Paixão Kröhling</a>
 */
public interface ContainerBuilder<T> {

    /**
     * Adds a new child to this {@link Container}
     * @param containerItem the {@link Container} item to add (can be an {@link org.gatein.api.application.Application},
     *                      for instance)
     */
    public ContainerBuilder<T> child(ContainerItem containerItem);

    /**
     * Adds the provided list of children to the existing list of children for this builder. If a null value is provided,
     * the current list of children is cleared.
     *
     * @param children    the list of {@link ContainerItem} to add to this container
     * @return this builder
     */
    public ContainerBuilder<T> children(List<ContainerItem> children);

    /**
     * Builds a {@link Container} based on the provided information and adds it to the parent {@link Container}.
     *
     * If there's no parent (ie, is at the top level), then add itself as a child of the root {@link Container}
     * (usually at the {@link org.gatein.api.page.Page} level) and returns itself.
     *
     * @return the parent container builder or itself if this container is placed at the top level
     */
    public ContainerBuilder<T> buildToParentBuilder();

    /**
     * Marks the end of the work on building {@link Container}s. Gathers all work done in the {@link ContainerBuilder}
     * and adds the generated children to the top level builder (usually a {@link org.gatein.api.composition.PageBuilder}),
     * returning the top level builder.
     *
     * @return the {@link org.gatein.api.composition.PageBuilder} that started this {@link ContainerBuilder}.
     */
    public T buildToTopBuilder();

    /**
     * Starts a new builder, using the column template. Children added to this new builder will be rendered as
     * columns on the screen.
     *
     * @return a newly created {@link ContainerBuilder}, specialized in rendering columns
     */
    public ContainerBuilder<T> newColumnsBuilder();

    /**
     * Starts a new builder, using the row template. Children added to this new builder will be rendered as
     * rows on the screen.
     *
     * @return a newly created {@link ContainerBuilder}, specialized in rendering rows
     */
    public ContainerBuilder<T> newRowsBuilder();

    /**
     * Starts a new builder, that builds on top of the provided Container. Useful when a custom container type is
     * required.
     *
     * @return a newly created {@link ContainerBuilder}
     */
    public ContainerBuilder<T> newCustomContainerBuilder(Container container);

    /**
     * Starts a new builder, that builds on top of the provided Container, using a generic container but with a
     * specific template. Useful to prevent creating a specific container type just to change the template.
     *
     * @return a newly created {@link ContainerBuilder}
     */
    public ContainerBuilder<T> newCustomContainerBuilder(String template);

    /**
     * Optionally sets the permission object that represents which users will be allowed to access the {@link Container} being built.
     * <p>
     * Unless set explicitly, the default value {@link Container#DEFAULT_ACCESS_PERMISSION} will be used for
     * the resulting {@link Container}.
     *
     * @param accessPermission the access permission for this container
     * @return this builder
     */
    public ContainerBuilder<T> accessPermission(Permission accessPermission);

    /**
     * Optionally sets the permission object that represents which users will be allowed to perform move, add
     * and remove operations with child {@link Application}s of the {@link Container} being built.
     * <p>
     * Unless set explicitly, the default value {@link Container#DEFAULT_MOVE_APPS_PERMISSION} will be used for
     * the resulting {@link Container}.
     *
     * @param moveAppsPermission the list of move apps permissions for this container
     * @return this builder
     */
    public ContainerBuilder<T> moveAppsPermission(Permission moveAppsPermission);

    /**
     * Optionally sets the permission object that represents which users will be allowed to perform move, add
     * and remove operations with child {@link Container}s of the {@link Container} being built.
     * <p>
     * Unless set explicitly, the default value {@link Container#DEFAULT_MOVE_CONTAINERS_PERMISSION} will be used for
     * the resulting {@link Container}.
     *
     * @param moveContainersPermission the list of move containers permissions for this container
     * @return this builder
     */
    public ContainerBuilder<T> moveContainersPermission(Permission moveContainersPermission);
}
