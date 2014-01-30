package org.gatein.api.composition;

import java.util.List;

/**
 * A layout builder is a base class for more specialized builders and broader builders. It represents the top-level
 * builder and provides access to methods exposing {@link ContainerBuilder}, which allows callers to specify how this layout
 * should look like.
 *
 * @see PageBuilder
 * @param <T>    the target top-level builder class
 * @author <a href="mailto:jpkroehling+javadoc@redhat.com">Juraci Paixão Kröhling</a>
 */
public interface LayoutBuilder<T extends LayoutBuilder<T>> {

    /**
     * Starts a new builder, using the column template. Children added to this new builder will be rendered as
     * columns on the screen. Similar to the method of the same name from {@link ContainerBuilder}, except that this
     * builder will be placed at the top-level of the layout.
     *
     * @see org.gatein.api.composition.ContainerBuilder#newColumnsBuilder()
     * @return a newly created ContainerBuilder, specialized in rendering columns
     */
    public ContainerBuilder<T> newColumnsBuilder();

    /**
     * Starts a new builder, using the row template. Children added to this new builder will be rendered as
     * rows on the screen. Similar to the method of the same name from {@link ContainerBuilder}, except that this
     * builder will be placed at the top-level of the layout.
     *
     * @see org.gatein.api.composition.ContainerBuilder#newRowsBuilder()
     * @return a newly created ContainerBuilder, specialized in rendering rows
     */
    public ContainerBuilder<T> newRowsBuilder();

    /**
     * Starts a new builder, that builds on top of the provided Container. Useful when the caller wants to specify
     * a template. Similar to the method of the same name from {@link ContainerBuilder}, except that this builder
     * will be placed at the top-level of the layout.
     *
     * @see org.gatein.api.composition.ContainerBuilder#newCustomContainerBuilder(Container)
     * @param container    the container to build on top of
     * @return a newly created ContainerBuilder
     */
    public ContainerBuilder<T> newCustomContainerBuilder(Container container);

    /**
     * Adds a new child to this container. Similar to the method of the same name from {@link ContainerBuilder}, except
     * that the child container will be placed at the top-level of the layout.
     *
     * @param containerItem the container item to add (can be an Application, for instance)
     * @return this builder
     */
    public T child(ContainerItem containerItem);

    /**
     * Adds the provided list of children to the existing list of children for this builder. If a null value is provided,
     * the current list of children is cleared.
     *
     * @param children    the list of {@link ContainerItem} to add to this layout
     * @return this builder
     */
    public T children(List<ContainerItem> children);

}
