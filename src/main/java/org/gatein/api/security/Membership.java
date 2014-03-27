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
import org.gatein.api.internal.StringSplitter;

/**
 * Represents a membership in a group
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Membership implements Serializable {
    public static final String ANY = "*";

    /**
     * Creates a new membership for any membership type in the specified group
     *
     * @param group the group
     * @return the membership
     */
    public static Membership any(String... group) {
        return new Membership(ANY, new Group(group));
    }

    private final String membershipType;
    private final Group group;

    /**
     * Creates a new membership for users with the specified membership type in the specified group
     *
     * @param membershipType the membership type
     * @param group the group
     */
    public Membership(String membershipType, Group group) {
        this.membershipType = Parameters.requireNonNull(membershipType, "membershipType");
        this.group = Parameters.requireNonNull(group, "group");
    }

    /**
     * Creates a new membership for the specified user
     *
     * @param user the user
     */
    public Membership(User user) {
        this.membershipType = user.getId();
        this.group = null;
    }

    /**
     * Returns the membership type
     *
     * @return the membership type
     */
    public String getMembershipType() {
        return membershipType;
    }

    /**
     * Returns the group
     *
     * @return the group
     */
    public Group getGroup() {
        return group;
    }

    @Override
    public String toString() {
        if (group == null) {
            return membershipType;
        } else {
            return membershipType + ":" + group.getId();
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((membershipType == null) ? 0 : membershipType.hashCode());
        return result;
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
        if (!(obj instanceof Membership)) {
            return false;
        }
        Membership other = (Membership) obj;
        if (group == null) {
            if (other.group != null) {
                return false;
            }
        } else if (!group.equals(other.group)) {
            return false;
        }
        if (membershipType == null) {
            if (other.membershipType != null) {
                return false;
            }
        } else if (!membershipType.equals(other.membershipType)) {
            return false;
        }
        return true;
    }

    public static Membership fromString(String membership) {
        Parameters.requireNonNull(membership, "membership");

        String[] parts = StringSplitter.splitter(":").split(membership);
        if (parts.length == 1) {
            return new Membership(new User(parts[0]));
        } else if (parts.length == 2) {
            return new Membership(parts[0], new Group(parts[1]));
        } else {
            throw new IllegalArgumentException("Invalid membership string " + membership);
        }
    }
}
