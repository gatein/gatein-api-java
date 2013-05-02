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

package org.gatein.api.oauth.exception;

/**
 * Exception code of OAuth error
 *
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 * @see OAuthApiException
 */
public enum OAuthApiExceptionCode {

    /**
     * This error could happen during saving of user into GateIn identity database.
     * It happens when there is an attempt to save user with facebookUsername (or googleUsername), but there is already an existing
     * user with same facebookUsername.
     *
     * For example: We want to save user 'john' with facebookUsername 'john.doyle' but we already have user 'johny2' with same facebookUsername 'john.doyle'
     */
    DUPLICATE_OAUTH_PROVIDER_USERNAME,

    /**
     * Error when we have invalid or revoked access token
     */
    ACCESS_TOKEN_ERROR,

    /**
     * Generic IO error (for example network error)
     */
    IO_ERROR,

    /**
     * Error when revoking of accessToken of any provider failed
     */
    TOKEN_REVOCATION_FAILED,

    /**
     * Error during DB operation (For example get/set/remove access token from DB)
     */
    PERSISTENCE_ERROR,

    /**
     * Thrown when object of specified type wasn't found or couldn't be obtained and initialized
     */
    SOCIAL_API_OBJECT_NOT_FOUND,

    /**
     * Some other error
     */
    OTHER_ERROR,
}
