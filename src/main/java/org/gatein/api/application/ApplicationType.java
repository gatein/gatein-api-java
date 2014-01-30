package org.gatein.api.application;

/**
 * The set of possible ApplicationTypes.
 *
 * @author <a href="mailto:jpkroehling+javadoc@redhat.com">Juraci Paixão Kröhling</a>
 */
public enum ApplicationType {
    /**
     * Represents a Gadget, a mini web application embedded in a web page and running on an application server platform.
     * Consult the Reference Guide for more information on "Gadgets"
     */
    GADGET("gadget"),

    /**
     * Represents a regular portlet.
     */
    PORTLET("portlet"),

    /**
     * Represents a remote portlet available through WSRP (Web Services for Remote Portlets).
     * Consult the Reference Guide for more information on WSRP.
     */
    WSRP("wsrp");

    private String name;

    private ApplicationType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


}
