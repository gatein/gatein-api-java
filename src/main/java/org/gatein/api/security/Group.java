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
import org.gatein.api.internal.StringJoiner;
import org.gatein.api.internal.ObjectToStringBuilder;
import org.gatein.api.internal.StringSplitter;

/**
 * Represents a group. A {@link User} can belong to one or more groups.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Group implements Serializable {
    private final String id;

    /**
     * Creates an new group instance with the specified hierarchy. For example
     * <code>new Group("platform", "administrators")</code>
     *
     * @param group the group
     * @throws IllegalArgumentException if group is null or empty
     */
    public Group(String... group) {
        Parameters.requireNonEmpty(group, "group");

        if (group.length == 1) {
            this.id = group[0];
        } else {
            this.id = StringJoiner.joiner("/").leading().trimToNull().ignoreNulls().join(group);
        }
    }

    /**
     * Creates an new group instance with the specified id. For example <code>new Group("/platform/administrators")</code>
     *
     * @param id the id of the group
     * @throws IllegalArgumentException if id is null
     */
    public Group(String id) {
        this(StringSplitter.splitter("/").trim().ignoreEmptyStrings().split(Parameters.requireNonNull(id, "id")));
    }

    /**
     * Returns the id of the group. For example "/platform/administrators"
     *
     * @return the group id
     */
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return ObjectToStringBuilder.toStringBuilder(getClass()).add("groupId", id).toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Group) {
            Group other = (Group) obj;
            return this.id == other.id
                    || (this.id != null && this.id.equals(other.id));
        } else {
            return false;
        }
    }

}
