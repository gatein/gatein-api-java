package org.gatein.api.composition;

import org.gatein.api.page.Page;

/**
 *
 * A block of content to be displayed on a {@link Page}. Its main responsibility is to provide
 * a template that renders the children of this {@link Container} in the user interface.
 *
 * @author <a href="mailto:jpkroehling+javadoc@redhat.com">Juraci Paixão Kröhling</a>
 */
public interface Container extends ContainerItem, BareContainer {

    /**
     * Returns an internal URL to a template file that should be used to render this container.
     * Note that the present default implementation supports only Groovy templates.
     * <p>
     * Examples used in the default implementation:
     * <ul>
     * <li>{@code "system:/groovy/portal/webui/container/UIContainer.gtmpl"}</li>
     * <li>{@code "system:/groovy/portal/webui/container/UITableColumnContainer.gtmpl"}</li>
     * </ul>
     * Beware that the template URLs used by implementations may change without notice.
     *<p>
     * @return the internal URL to the container's template file
     */
    public String getTemplate();

    /**
     * Sets an internal URL to a template file that should be used to render this container.
     * Note that the present default implementation supports only Groovy templates.
     * <p>
     * This method is provided as a last resort to allow customisation in cases when there is
     * no suitable Container provided by the API itself. The Containers provided by the API can be
     * accessed through {@code new*Builder()} methods of {@link PageBuilder}
     * and {@link ContainerBuilder}. Please check them before falling back to this method.
     * <p>
     * Beware that the template URLs used by implementations may change without notice.
     * <p>
     * @param template the internal URL to the container's template file.
     */
    public void setTemplate(String template);
}
