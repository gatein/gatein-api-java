/*
 * JBoss, a division of Red Hat
 * Copyright 2013, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
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

package org.gatein.api.oauth;

import java.io.IOException;

import org.gatein.api.oauth.exception.OAuthApiException;
import org.gatein.api.oauth.exception.OAuthApiExceptionCode;

/**
 * Object, which represents single OAuth provider (Social network). It's used to interact with Portal to load/save needed
 * informations or inform Portal that we want to start some OAuth actions.
 *
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public interface OAuthProvider {

    /**
     * Key under which Facebook OAuth provider is registered
     */
    static final String FACEBOOK = "FACEBOOK";

    /**
     * Key under which Google+ OAuth provider is registered
     */
    static final String GOOGLE = "GOOGLE";

    /**
     * Key under which Twitter OAuth provider is registered
     */
    static final String TWITTER = "TWITTER";

    /**
     * Return key of this OAuth provider. For example {@link #FACEBOOK}
     *
     * @return key of this OAuth provider
     */
    String getKey();

    /**
     * Return friendly name of this OAuth provider. For example "Facebook". It might be useful for messages, which will be shown
     * to end user
     *
     * @return friendly name of this OAuth provider
     */
    String getFriendlyName();

    /**
     * Return access token for given user from portal identity storage (DB or LDAP). Return null if access token wasn't found
     *
     * @param username name of portal user
     * @return access token of given user
     * @throws OAuthApiException if error occured. Error code would be {@link OAuthApiExceptionCode#PERSISTENCE_ERROR} if error
     * occured in communication between Portal and Identity storage (DB or LDAP)
     */
    AccessToken loadAccessToken(String username) throws OAuthApiException;

    /**
     * Save access token for given user to portal identity storage (DB or LDAP)
     *
     * @param username name of portal user
     * @param accessToken access token to save
     * @throws OAuthApiException if error occured. Error code would be {@link OAuthApiExceptionCode#PERSISTENCE_ERROR} if error
     * occured in communication between Portal and Identity storage (DB or LDAP)
     */
    void saveAccessToken(String username, AccessToken accessToken) throws OAuthApiException;

    /**
     * Remove access token of given user from portal identity storage (DB or LDAP)
     *
     * @param username name of portal user
     * @throws OAuthApiException if error occured. Error code would be {@link OAuthApiExceptionCode#PERSISTENCE_ERROR} if error
     * occured in communication between Portal and Identity storage (DB or LDAP)
     */
    void removeAccessToken(String username) throws OAuthApiException;

    /**
     * Start OAuth or OAuth2 workflow, which means redirection to OAuth provider (Social network) login screen and authorization
     * screen and obtaining of access token for current portal user. After calling this method, current Servlet request is finished because it's redirected
     * to OAuth provider
     *
     * <p>After whole OAuth workflow is successfully finished, you can obtain access token of current user via
     * {@link #loadAccessToken(String)}</p>
     *
     * @param neededCustomScope required OAuth scope. This parameter can be null and in this case, OAuth workflow will be started
     *                          just with scopes from Portal configuration
     * @throws OAuthApiException if this operation is not supported or some other OAuth error occured
     * @throws IOException if some I/O error occured (For example when calling redirecting current Servlet response)
     */
    void startOAuthWorkflow(String neededCustomScope) throws OAuthApiException, IOException;

    /**
     * Check if given access token is valid and possibly update some info (like scopes or access token itself if it was refreshed)
     * In case that access token has been refreshed (updated) you may call {@link #saveAccessToken(String, AccessToken)} to update
     * it in DB (it's not done by Portal itself during call of this method)
     *
     * @param accessToken access token to validate
     * @return Validated access token with all refreshed info (In some cases it could be equal to passed access token)
     * @throws OAuthApiException if error occured during validation. Error code could be {@link OAuthApiExceptionCode#ACCESS_TOKEN_ERROR}
     * if passed access token is invalid or revoked, or {@link OAuthApiExceptionCode#IO_ERROR} if network error happened during
     * communication with OAuth provider
     */
    AccessToken validateTokenAndUpdateScopes(AccessToken accessToken) throws OAuthApiException;

    /**
     * Revoke current access token on OAuth provider side, so access token won't be valid anymore and portal application
     * can't be seen in list of available applications of OAuth provider (For example:
     * https://www.facebook.com/settings?tab=applications in case of Facebook)
     *
     * @param accessToken access token to revoke
     * @throws OAuthApiException with code {@link OAuthApiExceptionCode#TOKEN_REVOCATION_FAILED} if revocation failed
     * (For example network error or access token has been already revoked before)
     */
    void revokeToken(AccessToken accessToken) throws OAuthApiException;

    /**
     * Return "accessor" object, which can be used to call some operations on Social network
     *
     * @param accessToken access token used to initialize object
     * @param socialApiObjectType Type of requested social API object
     * @return Instance of requested social API object
     * @throws OAuthApiException with code {@link OAuthApiExceptionCode#SOCIAL_API_OBJECT_NOT_FOUND} if object of requested type
     * is not supported by this OAuth provider
     */
    <T> T getAuthorizedSocialApiObject(AccessToken accessToken, Class<T> socialApiObjectType) throws OAuthApiException;
}
