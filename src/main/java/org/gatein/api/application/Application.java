package org.gatein.api.application;

import org.gatein.api.composition.ContainerItem;
import org.gatein.api.security.Permission;

/**
 * A record in the Application Registry. Currently, there are three types of applications as defined
 * in {@link ApplicationType}: Portlet, WSRP Portlet and Gadget.
 * <p>
 * The same concept is available in the User Interface under "Group > Administration > Application Registry"
 *
 * @author <a href="mailto:jpkroehling+javadoc@redhat.com">Juraci Paixão Kröhling</a>
 */
public interface Application extends ContainerItem {

    /**
     * The application's ID. Example: "Gadgets/Calendar"
     * @return the application's ID
     */
    String getId();

    /**
     * The application's name. Example: "Calendar"
     * @return the application's name
     */
    String getApplicationName();

    /**
     * The category name for this application. Example: "Gadgets"
     * @return the category name for this application
     */
    String getCategoryName();

    /**
     * The type of this application. Example {@code ApplicationType.GADGET}
     * @return the type of this application
     */
    ApplicationType getType();

    /**
     * The display name for this application. Example: "Calendar"
     * @return the display name of this application
     */
    String getDisplayName();

    /**
     * The long description of this application. Example: "What date is it? Use this cool..."
     * @return the long description of this application
     */
    String getDescription();

    /**
     * The URL for this application's icon, relative to the portal's context.
     * Example: "/eXoGadgets/skin/DefaultSkin/portletIcons/Calendar.png"
     * @return the URL for this application's icon
     */
    String getIconURL();

    /**
     * Gets a permission object that represents which users are allowed to access this application.
     * Example: "[*:/platform/users]"
     * @return the access permissions for this application
     */
    Permission getAccessPermission();
}
