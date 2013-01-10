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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class Nodes {
    /**
     * Returns an unmodifiable list with the children of the specified node
     *
     * @param node the parent node
     * @return an unmodifiable list
     */
    public static List<Node> asList(Node node) {
        List<Node> l = new ArrayList<Node>(node.getChildCount());
        for (Node c : node) {
            l.add(c);
        }
        return Collections.unmodifiableList(l);
    }

    // ----------------- Node Visitor Utility Methods

    /**
     * A visitor that will not visit any nodes.
     *
     * @return a visitor object
     */
    public static NodeVisitor visitNone() {
        return visitNodes(0);
    }

    /**
     * A visitor that will visit only children nodes.
     *
     * @return a visitor object
     */
    public static NodeVisitor visitChildren() {
        return visitNodes(1);
    }

    /**
     * A visitor that will visit all nodes. Using this may impact performance.
     *
     * @return a visitor object
     */
    public static NodeVisitor visitAll() {
        return visitNodes(-1);
    }

    /**
     * Creates a <code>NodeVisitor</code> which will visit nodes up to the specified depth. It's best to use the methods
     * <code>visitNone</code>, <code>visitChildren</code>, or <code>visitAll</code> for values of 0, 1, and -1 respectively.
     *
     * @param depth the depth. A value less then 0 will visit all, i.e. <code>visitAll</code>
     * @return a visitor object
     */
    public static NodeVisitor visitNodes(int depth) {
        if (depth == 0)
            return NONE;
        if (depth == 1)
            return CHILDREN;
        if (depth < 0)
            return ALL;

        return new DepthVisitor(depth);
    }

    /**
     * Creates a <code>NodeVisitor</code> which will visit nodes matching the path. Each matching segment will load all children
     * until the end of the path is met, in which nothing else is loaded.
     *
     * @param path the path to the node
     * @return a visitor object
     */
    public static NodeVisitor visitNodes(NodePath path) {
        return visitNodes(path, visitNodes(0));
    }

    /**
     * Creates a <code>NodeVisitor</code> which will visit nodes matching the path. Each matching segment will load all children
     * until the end of the path is met, in which the visitor parameter is used to determine further visiting.
     * <p>
     * Common use is to load the children once the path is met, so calling
     * <code>visitNodes(NodePath.path("foo", "bar"), visitChildren())</code> would load the node for the given path and it's
     * children.
     * </p>
     *
     * @param visitor the visitor object used once the path is met.
     * @param path the path to the node
     * @return a visitor object
     */
    public static NodeVisitor visitNodes(NodePath path, NodeVisitor visitor) {
        return new DelegatingPathVisitor(path, visitor);
    }

    // ----------------- Private visitor stuff

    private static final NodeVisitor NONE = new DepthVisitor(0);

    private static final NodeVisitor CHILDREN = new DepthVisitor(1);

    public static final NodeVisitor ALL = new DepthVisitor(-1);

    // Depth visitor
    private static class DepthVisitor implements NodeVisitor {
        private final int height;

        public DepthVisitor(final int height) {
            this.height = height;
        }

        @Override
        public boolean visit(int depth, String name, NodeDetails details) {
            return (height < 0 || depth < height);
        }
    }

    // NodePath visitor
    private static class DelegatingPathVisitor implements NodeVisitor {
        private final NodePath path;
        private final NodeVisitor visitor;

        public DelegatingPathVisitor(NodePath path, NodeVisitor visitor) {
            this.path = path;
            this.visitor = visitor;
        }

        @Override
        public boolean visit(int depth, String name, NodeDetails details) {
            if (depth < path.size()) {
                return depth == 0 || path.getSegment(depth - 1).equals(name);
            } else if (depth == path.size()) {
                if (depth == 0 || path.getSegment(depth - 1).equals(name)) {
                    return visitor.visit(0, name, details);
                } else {
                    return false;
                }
            } else {
                return visitor.visit(depth - path.size(), name, details);
            }
        }
    }

    private Nodes() {
    }
}
