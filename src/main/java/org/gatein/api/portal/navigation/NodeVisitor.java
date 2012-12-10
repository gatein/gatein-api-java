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

package org.gatein.api.portal.navigation;

import org.gatein.api.portal.page.PageId;

/**
 * A node visitor is used to walk the node tree. Some node visitor's are available in the <code>Nodes</code> utility
 * class, i.e. {@link Nodes#visitNodes(int)}
 *
 * @see Nodes
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface NodeVisitor
{
   /**
    * Determines if more nodes should be visited.
    *
    * @param depth the current depth in the node hierarchy. This can be relative depending on where the visitor begins.
    * @param name the name of the node. This will be null if visiting the root node.
    * @param details the node details that are available while visiting. This will be null if visiting the root node.
    * @return true to continue visiting children nodes
    */
   boolean visit(int depth, String name, NodeDetails details);

   public static interface NodeDetails
   {
      /**
       * The <code>Visibility</code> of the node being visited.
       *
       * @return the visibility
       */
      Visibility getVisibility();

      /**
       * The icon name of the node being visited.
       *
       * @return the icon name
       */
      String getIconName();

      /**
       * The <code>PageId</code> of the node being visited.
       *
       * @return the page id
       */
      PageId getPageId();

      /**
       * The <code>NodePath</code> of the node being visited.
       *
       * @return the node path
       */
      NodePath getNodePath();
   }
}
