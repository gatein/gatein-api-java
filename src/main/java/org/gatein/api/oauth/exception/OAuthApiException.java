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

import org.gatein.api.ApiException;

/**
 * ApiException when calling some OAuth operation
 *
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class OAuthApiException extends ApiException {

    private final OAuthApiExceptionCode exceptionCode;

    public OAuthApiException(final OAuthApiExceptionCode exceptionCode, final String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public OAuthApiException(final OAuthApiExceptionCode exceptionCode, final String message, final Throwable t) {
        super(message, t);
        this.exceptionCode = exceptionCode;
    }

    /**
     * Return exception code, which specifies which OAuth error actually happened
     *
     * @return exception code
     */
    public OAuthApiExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return exceptionCode + ": " + super.getMessage();
    }
}
