/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.gatein.api.navigation;

import java.io.Serializable;
import java.net.URI;
import java.util.Comparator;

import org.gatein.api.EntityAlreadyExistsException;
import org.gatein.api.common.Displayable;
import org.gatein.api.page.PageId;

/**
 * A node object which represents the current state of a node retrieved from the portal. All changes to the node are not saved
 * until {@link Navigation#saveNode(Node)} is called.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Node extends Displayable, Iterable<Node>, Serializable {
    /**
     * The name of the node.
     *
     * @return the name of the node. Should never be null.
     */
    String getName();

    /**
     * Sets the name of the node.
     *
     * @param name the name of the node.
     * @throws IllegalArgumentException if the name is null.
     */
    void setName(String name) throws IllegalArgumentException;

    /**
     * Returns the parent of the node, or null if the node is the root node.
     *
     * @return the parent node
     */
    Node getParent();

    /**
     * The path of the node.
     *
     * @return the node path
     */
    NodePath getNodePath();

    /**
     * Resolves the URI given the current context.
     *
     * @return the resolved URI for this navigation node.
     */
    URI getURI();

    /**
     * If the node is visible. Convenience method for doing <code>Node.getVisibility().isVisible()</code>
     *
     * @return true if the node is visible, false otherwise
     */
    boolean isVisible();

    /**
     * Returns the visibility object of this navigation node. This should never be null.
     *
     * @return the visibility of this node. This should never be null.
     */
    Visibility getVisibility();

    /**
     * Sets the visibility for this navigation node.
     *
     * @param visibility the visibility to set
     * @throws IllegalArgumentException if the publication date is null
     */
    void setVisibility(Visibility visibility) throws IllegalArgumentException;

    /**
     * Sets the visibility of this navigation node either {@link Visibility.Status#VISIBLE} or {@link Visibility.Status#HIDDEN}.
     * This does not remove any publication information, just sets the flag.
     *
     * @param visible if this node should be visible or not
     */
    void setVisibility(boolean visible);

    /**
     * Sets the visibility of this navigation node to be {@link Visibility.Status#PUBLICATION}, in which the dates of the
     * <code>PublicationDate</code> are used to calculate the visibility.
     *
     * @param publicationDate the publication date
     * @throws IllegalArgumentException if the publication date is null
     */
    void setVisibility(PublicationDate publicationDate) throws IllegalArgumentException;

    /**
     * Name of the icon for this navigation node. Can be null.
     *
     * @return the icon name or null
     */
    String getIconName();

    /**
     * Sets the name of the icon name for this navigation node. Can be null.
     *
     * @param iconName the name of the icon, or null.
     */
    void setIconName(String iconName);

    /**
     * The <code>PageId</code> of the page this node points to.
     *
     * @return the page id or null
     */
    PageId getPageId();

    /**
     * Sets the <code>PageId</code> of the page this node should point to. Can be null.
     *
     * @param pageId the page id or null
     */
    void setPageId(PageId pageId);

    /**
     * If this node is the root node of the navigation.
     *
     * @return true if it's the root node, false otherwise
     */
    boolean isRoot();

    /**
     * Adds a child to this navigation node.
     *
     * @param childName the name of the child
     * @return the newly created child node
     * @throws IllegalArgumentException if childName is null
     * @throws IllegalStateException if this node's children have not been loaded.
     * @throws EntityAlreadyExistsException if a child of the same name already exists.
     */
    Node addChild(String childName) throws IllegalArgumentException, IllegalStateException, EntityAlreadyExistsException;

    /**
     * Inserts a child to this navigation node at the specified index.
     *
     * @param index the index at which the child is to be inserted
     * @param childName the name of the child
     * @return the newly created child node
     * @throws IllegalArgumentException if childName is null
     * @throws IllegalStateException if this node's children have not been loaded.
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws EntityAlreadyExistsException if a child of the same name already exists.
     */
    Node addChild(int index, String childName) throws IllegalArgumentException, IllegalStateException,
            IndexOutOfBoundsException, EntityAlreadyExistsException;

    /**
     * Returns the child node.
     *
     * @param childName the name of the child node
     * @return the child node or null if the child does not exist.
     * @throws IllegalArgumentException the childName is null
     * @throws IllegalStateException if this node's children have not been loaded
     */
    Node getChild(String childName) throws IllegalArgumentException, IllegalStateException;

    /**
     * Returns the child node of the given index.
     *
     * @param index the index of the child
     * @return the child at the given index
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws IllegalStateException if this node's children have not been loaded
     */
    Node getChild(int index) throws IndexOutOfBoundsException, IllegalStateException;

    /**
     * Returns the number of child nodes.
     *
     * @return number of child nodes
     * @throws IllegalStateException if this node's children have not been loaded
     */
    int getChildCount() throws IllegalStateException;

    /**
     * If this node has a child of the specified childName
     *
     * @param childName the name of the child
     * @return true if the child exists, false otherwise
     * @throws IllegalArgumentException if childName is null
     * @throws IllegalStateException if this node's children have not been loaded
     */
    boolean hasChild(String childName) throws IllegalArgumentException, IllegalStateException;

    /**
     * If this node's children has been loaded. This should be called prior to calling any child methods on this node.
     *
     * @return true if the children have been loaded, false otherwise
     */
    boolean isChildrenLoaded();

    /**
     * Returns the node specified by the node path, relative to this node.
     * <p>
     * For example: if the current tree is /foo/bar/baz and the current node is foo, then calling
     * <code>foo.getNode("bar", "baz")</code> would return the baz node.
     * </p>
     *
     * @param nodePath the node path relative to this node.
     * @return the node
     * @throws IllegalArgumentException if nodePath is null
     * @throws IllegalStateException if any of the nodes represented by the path do not have their children loaded
     */
    Node getNode(String... nodePath) throws IllegalArgumentException, IllegalStateException;

    /**
     * Returns the node specified by the node path, relative to this node.
     * <p>
     * For example: if the current tree is /foo/bar/baz and the current node is foo, then calling
     * <code>foo.getNode(NodePath.path("bar", "baz"))</code> would return the baz node.
     * </p>
     *
     * @param nodePath the node path relative to this node
     * @return the node
     * @throws IllegalArgumentException if nodePath is null
     * @throws IllegalStateException if any of the nodes represented by the path do not have their children loaded
     */
    Node getNode(NodePath nodePath) throws IllegalArgumentException, IllegalStateException;

    /**
     * The index of the child with the specified name.
     *
     * @param childName the name of the child
     * @return the index of the child or -1 if the child does not exist.
     * @throws IllegalArgumentException if childName is null
     * @throws IllegalStateException if this node's children have not been loaded
     */
    int indexOf(String childName) throws IllegalArgumentException;

    /**
     * Removes the child from this navigation node.
     *
     * @param childName the name of the child
     * @return true if the child was removed, false otherwise
     * @throws IllegalArgumentException if childName was null
     * @throws IllegalStateException if this node's children have not been loaded
     */
    boolean removeChild(String childName) throws IllegalArgumentException;

    /**
     * Will return a filtered view of this navigation node. All child operations consider the filter.
     * <p>
     * For example if a node has 3 visible and one hidden children nodes and a filter is applied to only accept visible nodes,
     * getChildrenCount would only return 2, iterating over the filtered node would not include the hidden node, etc.
     * </p>
     *
     * @return a filtered node
     * @throws IllegalArgumentException if filter is null
     */
    FilteredNode filter() throws IllegalArgumentException;

    /**
     * Will sort the children of this node per the comparator.
     *
     * @param comparator the comparator responsible for comparing nodes for sorting.
     * @throws IllegalArgumentException if comparator is null
     */
    void sort(Comparator<Node> comparator) throws IllegalArgumentException;

    /**
     * Moves the node to the specified index.
     *
     * param index the index to move the node to.
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    void moveTo(int index) throws IndexOutOfBoundsException;

    /**
     * Moves this node to another parent.
     *
     * @param parent the parent to move this node to.
     * @throws IllegalArgumentException if parent is on a different branch, is a child of the current node, or if it is null
     * @throws EntityAlreadyExistsException if a node with the same name already exists at the parent location
     * @throws IllegalStateException if the children of the parent to move to have not been loaded
     */
    void moveTo(Node parent) throws IllegalArgumentException, EntityAlreadyExistsException;

    /**
     * Moves this node to another parent inserting it at the specified index.
     *
     * @param index the index to be inserted at
     * @param parent the parent to move this node to
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws IllegalArgumentException if parent is null
     * @throws EntityAlreadyExistsException if a node with the same name already exists at the parent location
     * @throws IllegalStateException if the children of the parent to move to have not been loaded
     */
    void moveTo(int index, Node parent) throws IndexOutOfBoundsException, IllegalArgumentException,
            EntityAlreadyExistsException;
}