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

import java.io.Serializable;

/**
 * Access token object, which encapsulates all important informations about OAuth1 or OAuth2 access token
 *
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public interface AccessToken extends Serializable {

    /**
     * Return list of all available scopes in single String. Scopes are divided by delimiter character (usually space).
     *
     * <p>In OAuth specification, each scope represents some kind of permission. So note that this access token could be used only to
     * call operations, for which scope is available.</p>
     *
     * <p>For example: scope "email" could be used in Facebook to retrieve information about user's email address. So if you have this
     * scope, you can use this access token object to retrieve email address of this user from Facebook.</p>
     *
     * @return list of all available scopes in single String
     */
    String getAvailableScopes();

    /**
     * Return true if given scope is available in list of OAuth scopes of this access token
     *
     * @param scope to test
     * @return true if given scope is available
     * @see #getAvailableScopes
     */
    boolean isScopeAvailable(String scope);

    /**
     * Return string representation of this access token. Note that it may not be enough in some cases, so this operation is
     * useful only for some OAuth providers
     *
     * @return access token
     */
    String getAccessToken();
}
