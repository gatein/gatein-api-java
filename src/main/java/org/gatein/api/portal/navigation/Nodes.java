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

import org.gatein.api.Portal;
import org.gatein.api.internal.Strings;
import org.gatein.api.portal.User;
import org.gatein.api.util.Filter;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Nodes
{
   //----------------------------------- Node Visitor Utility Methods ------------------------------------------------//

   public static NodeVisitor visitNone()
   {
      return visitNodes(0);
   }

   public static NodeVisitor visitChildren()
   {
      return visitNodes(1);
   }

   public static NodeVisitor visitAll()
   {
      return visitNodes(-1);
   }

   /**
    * Creates a <code>NodeVisitor</code> which will visit nodes up to the specified depth. It's best to use the methods
    * <code>visitNone</code>, <code>visitChildren</code>, or <code>visitAll</code> for values of 0, 1, and -1
    * respectively.
    *
    * @param depth the depth. A value less then 0 will visit all, i.e. <code>visitAll</code>
    * @return the visitor object
    */
   public static NodeVisitor visitNodes(int depth)
   {
      if (depth == 0) return SINGLE;
      if (depth == 1) return CHILDREN;
      if (depth < 0) return ALL;

      return new DepthVisitor(depth);
   }

   /**
    * Creates a <code>NodeVisitor</code> which will visit nodes matching the path. Each matching segment will
    * load all children until the end of the path is met, in which nothing else is loaded.
    *
    * @param path the path to the node
    * @return the visitor object
    */
   public static NodeVisitor visitNodes(NodePath path)
   {
      return visitNodes(path, visitNodes(0));
   }

   /**
    * Creates a <code>NodeVisitor</code> which will visit nodes matching the path. Each matching segment will
    * load all children until the end of the path is met, in which the visitor parameter is used to determine further
    * visiting.
    * <p/>
    * <p>
    * Common use is to load the children once the path is met, so calling
    * <pre><code>visitNodes(NodePath.create("some", "path"), visitChildren())</code></pre>
    * would load the node for the given path and it's children.
    * </p>
    *
    * @param visitor the visitor object used once the path is met.
    * @param path    the path to the node
    * @return the visitor object
    */
   public static NodeVisitor visitNodes(NodePath path, NodeVisitor visitor)
   {
      return new DelegatingPathVisitor(path, visitor);
   }

   //------------------------------------ Node Path Utility Methods --------------------------------------------------//

   /**
    * Root path of a node tree. Use this instead of <code>new NodePath()</code> to avoid creating multiple objects for
    * the same thing.
    *
    * @return <code>NodePath</code> representing the root of a node tree
    */
   public static NodePath rootPath()
   {
      return ROOT;
   }

   /**
    * Creates a node path based off strings such as '/foo/bar'
    *
    * @param path the string of the path
    * @return a node path representing the path of the string
    */
   public static NodePath path(String path)
   {
      return new NodePath(Strings.splitter("/").trim().ignoreEmptyStrings().split(path));
   }

   private static NodePath ROOT = new NodePath();

   //------------------------------------ Node Filter Utility Methods ------------------------------------------------//

   public static Filter<Node> userFilter(User user, Portal portal)
   {
      return new NodeFilter.Builder().withAccess(user, portal).build();
   }

   public static NodeFilter.Builder filter()
   {
      return new NodeFilter.Builder();
   }

   //------------------------------------------ Private visitor stuff ------------------------------------------------//

   private static final NodeVisitor SINGLE = new DepthVisitor(0);

   private static final NodeVisitor CHILDREN = new DepthVisitor(1);

   public static final NodeVisitor ALL = new DepthVisitor(-1);

   // Depth visitor
   private static class DepthVisitor implements NodeVisitor
   {
      private final int height;

      public DepthVisitor(final int height)
      {
         this.height = height;
      }

      @Override
      public boolean visit(int depth, String name, NodeDetails details)
      {
         return (height < 0 || depth < height);
      }
   }

   // NodePath visitor
   private static class DelegatingPathVisitor implements NodeVisitor
   {
      private final NodePath path;
      private final NodeVisitor visitor;

      public DelegatingPathVisitor(NodePath path, NodeVisitor visitor)
      {
         this.path = path;
         this.visitor = visitor;
      }

      @Override
      public boolean visit(int depth, String name, NodeDetails details)
      {
         if (depth < path.size())
         {
            return depth == 0 || path.getSegment(depth - 1).equals(name);
         }
         else if (depth == path.size())
         {
            if (depth == 0 || path.getSegment(depth - 1).equals(name))
            {
               return visitor.visit(0, name, details);
            }
            else
            {
               return false;
            }
         }
         else
         {
            return visitor.visit(depth - path.size(), name, details);
         }
      }
   }

   private Nodes()
   {
   }
}
