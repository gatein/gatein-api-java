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

import org.gatein.api.internal.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class NodePath implements Iterable<String>
{
   private static final NodePath ROOT_PATH = new NodePath();

   public static NodePath path(String first, String...elements)
   {
      if (first == null) throw new IllegalArgumentException("first part of path cannot be null");
      NodePath path = new NodePath(first);
      if (elements != null)
      {
         path = path.append(elements);
      }

      return path;
   }

   public static NodePath root()
   {
      return ROOT_PATH;
   }

   public static NodePath fromString(String path)
   {
      return new NodePath(Strings.splitter("/").trim().ignoreEmptyStrings().split(path));
   }

   private final List<String> pathList;

   private NodePath()
   {
      this(Collections.<String>emptyList());
   }

   private NodePath(String...pathList)
   {
      this(Arrays.asList(pathList));
   }

   private NodePath(List<String> pathList)
   {
      this.pathList = new ArrayList<String>(pathList);
   }

   public NodePath append(String...elements)
   {
      if (elements == null) throw new IllegalArgumentException("elements cannot be null");

      return append(new NodePath(elements));
   }

   public NodePath append(NodePath path)
   {
      List<String> list = new ArrayList<String>(pathList.size() + path.pathList.size());
      list.addAll(pathList);
      list.addAll(path.pathList);

      return new NodePath(list);
   }

   public NodePath subPath(int fromIndex)
   {
      return subPath(fromIndex, size());
   }

   public NodePath subPath(int fromIndex, int toIndex)
   {
      return new NodePath(new ArrayList<String>(pathList.subList(fromIndex, toIndex)));
   }

   public String getSegment(int index)
   {
      return pathList.get(index);
   }

   public String getLastSegment()
   {
      int size = size();
      return (size == 0) ? null : getSegment(size-1);
   }

   public NodePath getParent()
   {
      if (pathList.isEmpty()) return null;

      return subPath(0, size()-1);
   }

   public int size()
   {
      return pathList.size();
   }

   @Override
   public Iterator<String> iterator()
   {
      final Iterator<String> iterator = pathList.iterator();
      return new Iterator<String>()
      {
         @Override
         public boolean hasNext()
         {
            return iterator.hasNext();
         }

         @Override
         public String next()
         {
            return iterator.next();
         }

         @Override
         public void remove()
         {
            throw new UnsupportedOperationException("Remove operation not supported");
         }
      };
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      NodePath that = (NodePath) o;

      return pathList.equals(that.pathList);
   }

   @Override
   public int hashCode()
   {
      return pathList.hashCode();
   }

   @Override
   public String toString()
   {
      return Strings.joiner("/").leading().join(pathList);
   }
}
