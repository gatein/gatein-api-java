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

import org.gatein.api.internal.Parameters;
import org.gatein.api.internal.StringJoiner;
import org.gatein.api.internal.StringSplitter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class NodePath implements Iterable<String>, Comparable<NodePath>, Serializable {
    private static final NodePath ROOT_PATH = new NodePath();

    /**
     * Creates a node path with the value of the specified elements
     * 
     * @param elements the path elements
     * @return a node path
     */
    public static NodePath path(String... elements) {
        return new NodePath(Parameters.requireNonEmpty(elements, "elements"));
    }

    /**
     * Returns the path to the root node
     * 
     * @return a node path
     */
    public static NodePath root() {
        return ROOT_PATH;
    }

    /**
     * Creates a node path with a value represented by the specified path (for example '/home/node1')
     * 
     * @param path the string representation of a path
     * @return a node path
     */
    public static NodePath fromString(String path) {
        return new NodePath(StringSplitter.splitter("/").trim().ignoreEmptyStrings().split(path));
    }

    private final List<String> pathList;

    private NodePath() {
        this(Collections.<String> emptyList());
    }

    private NodePath(String... pathList) {
        this(Arrays.asList(pathList));
    }

    private NodePath(List<String> pathList) {
        this.pathList = new ArrayList<String>(pathList);
    }

    /**
     * Adds the specified elements to the end of this path
     * 
     * @param elements the elements to append
     * @return the combined path
     */
    public NodePath append(String... elements) {
        return append(new NodePath(Parameters.requireNonNull(elements, "elements")));
    }

    /**
     * Adds the specified path to the end of this path
     * 
     * @param path the path to append
     * @return the combined path
     */
    public NodePath append(NodePath path) {
        List<String> list = new ArrayList<String>(pathList.size() + path.pathList.size());
        list.addAll(pathList);
        list.addAll(path.pathList);

        return new NodePath(list);
    }

    /**
     * Returns the sub-path starting at fromIndex
     * 
     * @param fromIndex the start of the sub-path
     * @return the sub-path
     */
    public NodePath subPath(int fromIndex) {
        return subPath(fromIndex, size());
    }

    /**
     * Returns the sub-path starting at fromIndex and ending at toIndex
     * 
     * @param fromIndex the start of the sub-path
     * @param toIndex the end of the sub-path
     * @return the sub-path
     */
    public NodePath subPath(int fromIndex, int toIndex) {
        return new NodePath(new ArrayList<String>(pathList.subList(fromIndex, toIndex)));
    }

    /**
     * Returns the part of the path at the specified index
     * 
     * @param index the index
     * @return the specific part of the path
     */
    public String getSegment(int index) {
        return pathList.get(index);
    }

    /**
     * Returns the last part of the path
     * 
     * @return the last part of the path
     */
    public String getLastSegment() {
        int size = size();
        return (size == 0) ? null : getSegment(size - 1);
    }

    /**
     * Returns the path to the parent
     * 
     * @return the path
     */
    public NodePath parent() {
        if (pathList.isEmpty())
            return null;

        return subPath(0, size() - 1);
    }

    /**
     * Returns true if this path is an ancestor of the specified path
     * 
     * @param path the path
     * @return true if the specified path is a descendant of this path
     */
    public boolean isParent(NodePath path) {
        if (size() >= path.size())
            return false;

        for (int i = 0; i < size(); i++) {
            String name = getSegment(i);
            if (!name.equals(path.getSegment(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the size of the path
     * 
     * @return
     */
    public int size() {
        return pathList.size();
    }

    /**
     * Returns the path as an unmodifiable list of strings.
     *
     * @return the path as an unmodifiable list of strings
     */
    public List<String> asList() {
        return Collections.unmodifiableList(pathList);
    }

    /**
     * Returns the path as an array of strings
     *
     * @return the path as an array of strings
     */
    public String[] asArray() {
        return pathList.toArray(new String[pathList.size()]);
    }

    @Override
    public int compareTo(NodePath other) {
        int size = size();
        int otherSize = other.size();

        for (int i = 0; i < otherSize; i++) {
            if (i >= size)
                break;

            int result = getSegment(i).compareTo(other.getSegment(i));
            if (result != 0)
                return result;
        }

        return (size < otherSize ? -1 : (size == otherSize ? 0 : 1));
    }

    @Override
    public Iterator<String> iterator() {
        final Iterator<String> iterator = pathList.iterator();
        return new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove operation not supported");
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        NodePath that = (NodePath) o;

        return pathList.equals(that.pathList);
    }

    @Override
    public int hashCode() {
        return pathList.hashCode();
    }

    @Override
    public String toString() {
        return StringJoiner.joiner("/").leading().join(pathList);
    }
}
