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

import org.gatein.api.util.Filter;

import java.util.Iterator;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
class FilteredNode extends DelegateNode
{
   private final Filter<Node> filter;
   private final Node node;

   public FilteredNode(Filter<Node> filter, Node node)
   {
      super(node);
      this.filter = filter;
      this.node = node;
   }

   @Override
   public boolean hasChild(String childName)
   {
      return getChild(childName) != null;
   }

   @Override
   public boolean hasDescendant(NodePath path)
   {
      return getDescendant(path) != null;
   }

   @Override
   public Node getChild(int index)
   {
      Node child = node.getChild(index);
      return (filter.accept(child)) ? child : null;
   }

   @Override
   public Node getChild(String childName)
   {
      Node child = node.getChild(childName);
      return (filter.accept(child)) ? child : null;
   }

   @Override
   public Node getDescendant(NodePath path)
   {
      Node descendant = node.getDescendant(path);
      return (filter.accept(descendant)) ? descendant : null;
   }

   @Override
   public int size()
   {
      int size = 0;
      for (Node node : this)
      {
         if (filter.accept(node)) size++;
      }

      return size;
   }

   @Override
   public Iterator<Node> iterator()
   {
      return new FilteredNodeList(filter, node.nodeList()).iterator();
   }
}
