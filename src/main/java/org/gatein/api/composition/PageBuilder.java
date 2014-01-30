package org.gatein.api.composition;

import org.gatein.api.Portal;
import org.gatein.api.page.Page;
import org.gatein.api.security.Permission;

/**
 *
 * A starting point of the Page Composition API. An instance is typically obtained from {@link Portal}:
 * <pre>
 *     Portal myPortal = PortalRequest.getInstance().getPortal();
 *     PageBuilder myPageBuilder = myPortal.newPageBuilder();
 * </pre>
 *
 * <p>
 * There are two main kinds scenarios possible:
 * <ol>
 * <li>
 *      <strong>Flat</strong>
 *      <pre>
 *      Page myPage = myPageBuilder
 *              .child(gadgetCalculator) // first row
 *              .child(helloWorldPortlet) // second row
 *              .siteName(siteName)
 *              .siteType(siteType)
 *              .name("myFlatPage")
 *              .build(); // creates a new Page
 *      </pre>
 * </li>
 * <li>
 *      <strong>With nested containers</strong>
 *      <pre>
 *      Page myPage = myPageBuilder
 *              .child(gadgetCalculator) // first row of the page
 *              .newColumnsBuilder() // a couple of columns in the second row of the page
 *                   .child(storyOfTheDayPortlet) // first column
 *                   .child(productListPortlet) // second column
 *              .build() // finishes the columns
 *              .buildChildren() // returns the top level PageBuilder
 *              .child(helloWorldPortlet) // third row of the page
 *              .siteName(siteName)
 *              .siteType(siteType)
 *              .name("myComplexPage")
 *              .build(); // creates a new Page
 *      </pre>
 *      See also {@link ContainerBuilder#newColumnsBuilder()}, {@link ContainerBuilder#newRowsBuilder()} and newRowsBuilder
 * </li>
 * </ol>
 *
 * Note that the resulting {@link Page} object needs to be persisted using the
 * {@link org.gatein.api.Portal#savePage(Page)} method. See more examples on the usage
 * of this API on the documentation and quickstart.
 *
 * @see org.gatein.api.Portal#newPageBuilder()
 * @see org.gatein.api.Portal#savePage(Page)
 *
 * @author <a href="mailto:jpkroehling+javadoc@redhat.com">Juraci Paixão Kröhling</a>
 */
public interface PageBuilder extends LayoutBuilder<PageBuilder> {

    /**
     * Optionally sets a description for this page.
     * @param description the description to set
     * @return this builder
     */
    PageBuilder description(String description);

    /**
     * Required: Sets the site name to which this page belongs to
     * @param siteName the site name
     * @return this builder
     */
    PageBuilder siteName(String siteName);

    /**
     * Optionally sets the display name of this page.
     *
     * @param displayName the display name
     * @return this builder
     */
    PageBuilder displayName(String displayName);

    /**
     * Optionally sets whether this page should be shown in the "maximized window" mode.
     *
     * @param showMaxWindow the boolean indicating if the option should be shown
     * @return this builder
     */
    PageBuilder showMaxWindow(boolean showMaxWindow);

    /**
     * Sets the access permissions for this page. This information should be always provided. Failure to provide this
     * information might cause inconsistent behavior between the API versions: at a version, implementations might
     * decide that it's better to have Permission#everyone by default, at a later version, might be an admin. Or even
     * an IllegalStateException. It is good practice to *always* provide this when composing a page.
     *
     * @param accessPermission the access permission for this page
     * @return this builder
     */
    PageBuilder accessPermission(Permission accessPermission);

    /**
     * Sets the edit permission for this page. This information should be always provided. Failure to provide this
     * information might cause inconsistent behavior between the API versions: at a version, implementations might
     * decide that it's better to have Permission#everyone by default, at a later version, might be an admin. Or even
     * an IllegalStateException. It is good practice to *always* provide this when composing a page.
     *
     * @param editPermission the edit permission
     * @return this builder
     */
    PageBuilder editPermission(Permission editPermission);

    /**
     * Sets the move apps permissions for this page. This information should be always provided. Failure to provide this
     * information might cause inconsistent behavior between the API versions: at a version, implementations might
     * decide that it's better to have Permission#everyone by default, at a later version, might be an admin. Or even
     * an IllegalStateException. It is good practice to *always* provide this when composing a page.
     *
     * @param moveAppsPermission the list of move apps permissions for this page
     * @return this builder
     */
    PageBuilder moveAppsPermission(Permission moveAppsPermission);

    /**
     * Sets the move containers permissions for this page. This information should be always provided. Failure to provide this
     * information might cause inconsistent behavior between the API versions: at a version, implementations might
     * decide that it's better to have Permission#everyone by default, at a later version, might be an admin. Or even
     * an IllegalStateException. It is good practice to *always* provide this when composing a page.
     *
     * @param moveContainersPermission the list of move containers permissions for this page
     * @return this builder
     */
    PageBuilder moveContainersPermission(Permission moveContainersPermission);

    /**
     * <b>Required</b>
     * <p>
     * The site type of which this page belongs to. Possible values are:
     * <ul>
     *     <li>portal</li>
     *     <li>site</li>
     *     <li>user</li>
     * </ul>
     *
     * @param siteType    the site type, based on the possible values
     * @return this builder
     * @throws java.lang.IllegalArgumentException if the provided value is none of the possible values
     */
    PageBuilder siteType(String siteType);

    /**
     * Builds a new {@link Page} based on the provided information.
     * @return a new {@link Page} object
     * @throws java.lang.IllegalStateException if any mandatory information is not provided
     */
    public Page build();

    /**
     * <b>Required</b>
     * <p>
     * The page name
     *
     * @param name    the page name
     * @return this builder
     */
    public PageBuilder name(String name);

}
