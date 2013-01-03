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

import org.gatein.api.ApiException;
import org.gatein.api.EntityNotFoundException;
import org.gatein.api.site.SiteId;

/**
 * Navigation for a site responsible for the retrieval, saving, and removal of navigation nodes.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Navigation {
    /**
     * The <code>SiteId</code> of the navigation
     *
     * @return the site id
     */
    SiteId getSiteId();

    /**
     * The priority of the navigation. This can be used to determine the order in which navigations of the same
     * <code>SiteType</code> appear in a navigation menu.
     *
     * @return the priority
     */
    int getPriority();

    /**
     * Saves the priority for this navigation.
     *
     * @param integer the priority to save
     */
    void setPriority(int integer);

    /**
     * Returns a node represented by the node path or null if the node was not found.
     *
     * @param nodePath the path to the node
     * @return the node or null if the node was not found
     * @throws IllegalArgumentException if nodePath is null or empty
     */
    Node getNode(String... nodePath);

    /**
     * Returns a node represented by the node path or null if the node was not found.
     *
     * @param nodePath the path to the node
     * @return the node or null if the node was not found
     * @throws IllegalArgumentException if nodePath is null
     */
    Node getNode(NodePath nodePath);

    /**
     * Returns a node represented by the node path or null if the node was not found. The NodeVisitor is then used to determine
     * further loading of nodes.
     * <p>
     * For example if you want to retrieve the node /foo/bar but also want to load it's children you can pass in
     * <code>Nodes.visitChildren()</code> as the visitor to load the children of /foo/bar.
     * </p>
     *
     * @param nodePath the path to the node
     * @param visitor the visitor used to determine further loading of nodes. The visitor is relative to the node represented by
     *        the node path.
     * @throws IllegalArgumentException if nodePath or visitor is null
     * @return the node or null if the node was not found
     */
    Node getNode(NodePath nodePath, NodeVisitor visitor);

    /**
     * Returns the root node of the navigation with nodes loaded dependent on the <code>NodeVisitor</code>
     *
     * @param visitor the visitor to determine how many nodes to load.
     * @return the root node of the navigation
     * @throws IllegalArgumentException if visitor is null
     */
    Node getRootNode(NodeVisitor visitor);

    /**
     * Will refresh the node with latest from storage. For example if nodes were added/removed, etc you can refresh/sync those
     * changes. The refresh will affect the entire tree even if the node is not the root of the tree. When conflicting changes
     * exist, a merge will be attempted, however it could fail and lead to a non resolvable situation.
     *
     * @param node the node to refresh
     * @throws IllegalArgumentException if node is null
     */
    void refreshNode(Node node);

    /**
     * Will refresh the node with latest from storage. The refresh will affect the entire tree even if the node is not the root
     * of the tree. When conflicting changes exist, a merge will be attempted, however it could fail and lead to a non
     * resolvable situation. The visitor can be used to control new nodes to load.
     *
     * @param node the node to refresh
     * @param visitor the visitor which can load more nodes.
     * @throws IllegalArgumentException if node or visitor is null
     */
    void refreshNode(Node node, NodeVisitor visitor);

    /**
     * Removes a node, represented by the node path
     *
     * @param nodePath the path to the node
     * @return true if the node was removed, false otherwise
     * @throws IllegalArgumentException if nodePath is null
     * @throws EntityNotFoundException if the node could not be found.
     */
    boolean removeNode(NodePath nodePath) throws IllegalArgumentException, EntityNotFoundException;

    /**
     * Saves a node. All changes to the entire tree will be saved even if the node is not the root of the tree.
     *
     * @param node the node to save
     * @throws IllegalArgumentException if node is null
     * @throws ApiException if an exception occurred trying save the node
     */
    void saveNode(Node node) throws IllegalArgumentException, ApiException;
}
