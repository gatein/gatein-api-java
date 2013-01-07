/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.gatein.api;

import java.util.Collection;

public class Parameters {
    private static final String NAME_VALID_CHARS = "[\\w-]*";

    public static <S, T extends Collection<S>> T requireNonEmpty(T value, String paramName) {
        value = requireNonNull(value, paramName);

        if (value.size() == 0) {
            throw new IllegalArgumentException(paramName + " cannot be empty");
        }

        return value;
    }

    public static <T> T[] requireNonEmpty(T[] value, String paramName) {
        value = requireNonNull(value, paramName);

        if (value.length == 0) {
            throw new IllegalArgumentException(paramName + " cannot be empty");
        }

        return value;
    }

    public static <T> T requireNonNull(T value, String paramName) {
        if (value == null) {
            throw new IllegalArgumentException(paramName + " cannot be null");
        }

        return value;
    }

    public static <T> T requireNonNull(T value, String paramName, String message) {
        if (value == null) {
            throw new IllegalArgumentException(paramName + " cannot be null. " + message);
        }

        return value;
    }

    public static String requireValidName(String value, String paramName) {
        value = requireNonNull(value, paramName);

        int l = value.length();
        if (l < 3 || l > 30) {
            throw new IllegalArgumentException(paramName + " must be between 3 and 30 characters");
        }

        if (!value.matches(NAME_VALID_CHARS)) {
            throw new IllegalArgumentException(paramName + " can only contain alpha, digit, dash and underscore characters");
        }

        return value;
    }
}