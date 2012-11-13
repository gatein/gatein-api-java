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

import java.util.List;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
class FilteredNode extends DelegateNode
{
   private final Filter<Node> filter;
   private final List<Node> children;

   public FilteredNode(Filter<Node> filter, Node node)
   {
      super(node);
      this.filter = filter;
      this.children = new FilteredNodeList(filter, node.nodeList());
   }

   @Override
   public Node getChild(String childName)
   {
      return findChild(childName);
   }

   @Override
   public Node getChild(int index)
   {
      for (int i=index; i<delegate.size(); i++)
      {
         Node child = delegate.getChild(i);
         if (filter.accept(child))
         {
            return child;
         }
      }

      return null;
   }

   @Override
   public boolean hasChild(String childName)
   {
      return getChild(childName) != null;
   }

   @Override
   public int indexOf(String childName)
   {
      Node child = getChild(childName);

      return (child != null) ? getChildren().indexOf(child) : -1;
   }

   @Override
   public boolean removeChild(String childName)
   {
      Node child = findChild(childName);

      return child != null && getChildren().remove(child);
   }

   @Override
   public List<Node> getChildren()
   {
      return children;
   }

   @Override
   public int size()
   {
      return getChildren().size();
   }

   private Node findChild(String childName)
   {
      for (Node child : getChildren())
      {
         if (childName.equals(child.getName())) return child;
      }

      return null;
   }
}
