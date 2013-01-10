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
package org.gatein.api.navigation;

import org.gatein.api.common.Filter;
import org.gatein.api.security.User;

/**
 * A filtered node is obtained from {@link Node#filter()} and provides a mechanism for only showing specified nodes. Each method
 * will add an aditional filter that reduces the number of nodes returned. For example by invoking
 * <code>node.filter().showHasAccess(user).showVisible()</code> only nodes that the specified user has access to that are also
 * visible are returned. The filtering applies to all methods that operate on the children of the node. For example
 * {@link #indexOf(String)} can return a different index for a specified name than the {@link Node} the filtered node is
 * operating on.
 * 
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public interface FilteredNode extends Node {
    /**
     * Shows all nodes
     * 
     * @return this filtered node instance
     */
    public FilteredNode showAll();

    /**
     * Adds a predicate to this filter that accepts default nodes (equivalent to
     * <code>showVisible().showHasAccess(PortalRequest.getInstance().getUser())</code>). Default nodes are nodes that are
     * visible and that the current user has permissions to access.
     * 
     * @return this filtered node instance
     */
    public FilteredNode showDefault();

    /**
     * Adds a predicate to this filter that accepts visible nodes
     * 
     * @return this filtered node instance
     */
    public FilteredNode showVisible();

    /**
     * Adds a predicate to this filter that accepts nodes the specified user has permissions to access
     * 
     * @param user the user
     * @return this filtered node instance
     */
    public FilteredNode showHasAccess(User user);

    /**
     * Adds a predicate to this filter that accepts nodes the specified user has permissions to edit
     * 
     * @param user the user
     * @return this filtered node instance
     */
    public FilteredNode showHasEdit(User user);

    /**
     * Adds the specified predicate to this filter
     * 
     * @param predicate
     * @return this filtered node instance
     */
    public FilteredNode show(Filter<Node> filter);
}