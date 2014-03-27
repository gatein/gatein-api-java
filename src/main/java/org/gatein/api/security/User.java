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

package org.gatein.api.security;

import java.io.Serializable;

import org.gatein.api.internal.Parameters;

/**
 * Represents a user
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class User implements Serializable {
    private final String id;

    /**
     * Creates a new user with the specified id. To create a anonymous user use the static method {@link User#isAnonymous()}
     *
     * @param id the user
     * @throws IllegalArgumentException if id is null
     */
    public User(String id) {
        this.id = Parameters.requireNonNull(id, "id", "If the user is unknown, use User.anonymous() instead");
    }

    private User() {
        this.id = null;
    }

    /**
     * Returns the user id
     *
     * @return the user id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns true if the user is anonymous
     *
     * @return true if anonymous user
     */
    public boolean isAnonymous() {
        return this == ANONYMOUS;
    }



    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * Creates a anonymous user
     *
     * @return the anonymous user
     */
    public static User anonymous() {
        return ANONYMOUS;
    }

    private static final User ANONYMOUS = new User();
}
