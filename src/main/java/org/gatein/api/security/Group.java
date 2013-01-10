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

import org.gatein.api.internal.Parameters;
import org.gatein.api.internal.StringJoiner;
import org.gatein.api.internal.ObjectToStringBuilder;
import org.gatein.api.internal.StringSplitter;

/**
 * Represents a group. A {@link User} can belong to one or more groups.
 * 
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Group {
    private final String id;

    /**
     * Creates an new group instance with the specified hierarchy. For example
     * <code>new Group("platform", "administrators")</code>
     * 
     * @param group the group
     */
    public Group(String... group) {
        Parameters.requireNonNull(group, "group");

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
     */
    public Group(String id) {
        this(StringSplitter.splitter("/").trim().ignoreEmptyStrings().split(id));
    }

    /**
     * Returns the specified group. For example "/platform/administrators"
     * 
     * @return
     */
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return ObjectToStringBuilder.toStringBuilder(getClass()).add("groupId", id).toString();
    }
}
