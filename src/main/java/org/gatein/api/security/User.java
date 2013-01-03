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

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class User {
    private final String id;

    public User(String id) {
        if (id == null)
            throw new IllegalArgumentException("user id cannot be null. If the user is unknown, use User.anonymous() instead.");

        this.id = id;
    }

    private User() {
        this.id = null; // TODO: Do we want the id to be null, or set it to some strange internal string ? This may produce NPE
                        // if someone is expecting a value here.
    }

    public String getId() {
        return id;
    }

    public boolean isAnonymous() {
        return this == ANONYMOUS;
    }

    public static User anonymous() {
        return ANONYMOUS;
    }

    private static final User ANONYMOUS = new User();
}
