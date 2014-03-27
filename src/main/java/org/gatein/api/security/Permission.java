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

import org.gatein.api.internal.ObjectToStringBuilder;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents the permissions for a resource
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Permission implements Serializable {
    private static final Permission EVERYONE = new Permission();

    /**
     * Returns a permission where everyone can access the resource (public)
     *
     * @return the permission
     */
    public static Permission everyone() {
        return EVERYONE;
    }

    /**
     * Returns a permission where users with any membership type in the specified group can access the resource
     *
     * @param group the group
     * @return the permission
     */
    public static Permission any(String... group) {
        return new Permission(Membership.any(group));
    }

    private final Set<Membership> memberships;

    /**
     * Creates a permission where everyone can access the resource (public)
     */
    public Permission() {
        memberships = Collections.emptySet();
    }

    /**
     * Creates a permission where users with the specified member type in the specified group can access the resource
     *
     * @param membershipType the membership type
     * @param group the group
     */
    public Permission(String membershipType, Group group) {
        this(new Membership(membershipType, group));
    }

    /**
     * Creates a permission where the the specified user can access the resource
     *
     * @param user the user
     */
    public Permission(User user) {
        this(new Membership(user));
    }

    /**
     * Creates a permission where users with the specified membership can access the resource
     *
     * @param membership the membership
     */
    public Permission(Membership membership) {
        this(Collections.singleton(membership));
    }

    /**
     * Creates a permission where users with one or more of the specified memberships can access the resource
     *
     * @param memberships the memberships
     */
    public Permission(Set<Membership> memberships) {
        this(new LinkedHashSet<Membership>(memberships));
    }

    private Permission(LinkedHashSet<Membership> memberships) {
        this.memberships = memberships;
    }

    /**
     * Returns all memberships with permissions to access the resource
     *
     * @return the memberships
     */
    public Set<Membership> getMemberships() {
        return Collections.unmodifiableSet(memberships);
    }

    /**
     * Creates a new permission that includes all memberships of this permission and the specified membership
     *
     * @param membership the membership to add
     * @return the new permission
     */
    public Permission addMembership(Membership membership) {
        LinkedHashSet<Membership> newMemberships = new LinkedHashSet<Membership>(memberships.size() + 1);
        newMemberships.addAll(memberships);
        newMemberships.add(membership);

        return new Permission(newMemberships);
    }

    /**
     * Returns true if everyone has permissions to access the item
     *
     * @return true if resource is public
     */
    public boolean isAccessibleToEveryone() {
        return memberships.isEmpty();
    }

    @Override
    public String toString() {
        return ObjectToStringBuilder.toStringBuilder().add("memberships", memberships.isEmpty() ? "Everyone" : memberships)
                .toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return memberships != null ? memberships.hashCode() : 0;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Permission) {
            Permission other = (Permission) o;
            return this.memberships == other.memberships
                    || (this.memberships != null && this.memberships.equals(other.memberships));
        } else {
            return false;
        }
    }


}
