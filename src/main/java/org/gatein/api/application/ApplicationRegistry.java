package org.gatein.api.application;

import org.gatein.api.ApiException;

import java.util.List;

/**
 * Basic contract that can be used to retrieve {@link org.gatein.api.application.Application}s from the permanent storage.
 *
 * @author <a href="mailto:jpkroehling+javadoc@redhat.com">Juraci Paixão Kröhling</a>
 */
public interface ApplicationRegistry {

    /**
     * Retrieves all {@link org.gatein.api.application.Application}s registered on the present Portal Container.
     *
     * @return a list of all known {@link org.gatein.api.application.Application}s
     * @throws ApiException in case of problems with the permanent storage.
     */
    List<Application> getApplications() throws ApiException;

    /**
     * Retrieves a specific {@link org.gatein.api.application.Application}, based on it's ID.
     * @param id the id of the {@link org.gatein.api.application.Application} to retrieve
     * @return the {@link org.gatein.api.application.Application} belonging to the ID, or null if none is found under this ID.
     * @throws ApiException in case of problems with the permanent storage.
     */
    Application getApplication(String id) throws ApiException;

    /**
     * Imports applications to the registry that were deployed but were not registered yet. For instance, upon deployment of a
     * WAR that contains a portlet, the portlet is not available in the registry until this method is executed.
     * <p>
     * This method has the same effects as "Import Applications" action available in the UI.
     *
     * @throws ApiException in case of problems with the permanent storage.
     */
    void importApplications() throws ApiException;

}
