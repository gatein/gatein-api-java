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

import org.gatein.api.internal.Objects;
import org.gatein.api.portal.Label;
import org.gatein.api.portal.page.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Node implements Serializable
{
   private final String name;
   private Node parent;
   private Label label;
   private Visibility visibility;
   private String iconName;
   private Page.Id pageId;
   private List<Node> children;

   /**
    * Creates a new node with the specified name.
    *
    * @param name the name of the node.
    */
   public Node(String name)
   {
      if (name == null) throw new IllegalArgumentException("name cannot be null");

      this.name = name;
      this.visibility = new Visibility();
      this.children = new ArrayList<Node>();
   }

   /**
    * Creates a new node copying the fields from the node object.
    *
    * @param node the node to copy fields from. Changing this node has no impact on the new node being created.
    */
   public Node(Node node)
   {
      this(node.name, node, null); //dissociate the parent
   }

   /**
    * Creates a new node with a new name, copying the fields from the node object.
    *
    * @param name the new name of the node
    * @param node the node to copy fields from. Changing this node has no impact on the new node being created.
    */
   public Node(String name, Node node)
   {
      this(name, node, null); //dissociate the parent
   }

   private Node(Node node, Node parent)
   {
      this(node.name, node, parent);
   }

   private Node(String name, Node node, Node parent)
   {
      if (name == null) throw new IllegalArgumentException("name cannot be null");
      if (node == null) throw new IllegalArgumentException("node cannot be null");

      this.name = name;
      this.parent = parent;

      this.label = (node.label == null) ? null : new Label(node.label);
      this.visibility = node.visibility;
      this.iconName = node.iconName;
      this.pageId = node.pageId;

      // Copy children and associate their parent's accordingly
      this.children = new ArrayList<Node>(node.children.size());
      for (Node child : node.children)
      {
         this.children.add(new Node(child, this));
      }
   }

   public String getName()
   {
      return name;
   }

   public Node getParent()
   {
      return parent;
   }

   public NodePath getPath()
   {
      NodePath path = new NodePath(name);
      if (parent != null)
      {
         path = parent.getPath().append(path);
      }
      return path;
   }

   public Label getLabel()
   {
      return label;
   }

   public void setLabel(Label label)
   {
      this.label = label;
   }

   public boolean isVisible()
   {
      return visibility.isVisible();
   }

   public Visibility getVisibility()
   {
      return visibility;
   }

   public void setVisibility(Visibility visibility)
   {
      if (visibility == null) throw new IllegalArgumentException("visibility cannot be null");

      this.visibility = visibility;
   }

   public void setVisibility(boolean visible)
   {
      Visibility.Flag flag = (visible) ? Visibility.Flag.VISIBLE : Visibility.Flag.HIDDEN;
      this.visibility = new Visibility(flag, visibility.getPublicationDate());
   }

   public void setVisibility(PublicationDate publicationDate)
   {
      Visibility.Flag flag = visibility.getFlag();
      if (publicationDate != null)
      {
         flag = Visibility.Flag.PUBLICATION;
      }
      else if (flag == Visibility.Flag.PUBLICATION)
      {
         flag = Visibility.Flag.VISIBLE;
      }

      this.visibility = new Visibility(flag, publicationDate);
   }

   public String getIconName()
   {
      return iconName;
   }

   public void setIconName(String iconName)
   {
      this.iconName = iconName;
   }

   public Page.Id getPageId()
   {
      return pageId;
   }

   public void setPageId(Page.Id pageId)
   {
      this.pageId = pageId;
   }

   public Node getChild(int index)
   {
      return children.get(index);
   }

   public Node getChild(String name)
   {
      for (Node node : children)
      {
         if (node.name.equals(name)) return node;
      }

      return null;
   }

   public int getChildCount()
   {
      return children.size();
   }

   public List<Node> getChildren()
   {
      return Collections.unmodifiableList(children);
   }

   public void addChildren(List<Node> children)
   {
      for (Node child : children)
      {
         addChild(child);
      }
   }

   public Node addChild(String name)
   {
      return addChild(-1, name);
   }

   public Node addChild(int index, String name)
   {
      return addChild(index, new Node(name));
   }

   public Node addChild(Node node)
   {
      return addChild(-1, node);
   }

   public Node addChild(int index, Node node)
   {
      if (node == this) throw new IllegalArgumentException("Cannot add itself as a child.");
      if (node.parent != null) throw new IllegalArgumentException("Node being added is already associated with a parent. You can copy the node by passing it to the constructor which will remove the parent association.");

      Node child = getChild(node.name);
      if (child != null) throw new IllegalArgumentException("Child with name " + node.name + " already exists.");

      node.setParent(this);
      checkCyclic();

      if (index < 0)
      {
         children.add(node);
      }
      else
      {
         children.add(index, node);
      }

      return node;
   }

   public Node removeNode(String name)
   {
      Node child = getChild(name);
      if (child != null) children.remove(child);

      return child;
   }

   private void checkCyclic()
   {
      Node current = this;
      while ( (current = current.parent) != null)
      {
         if (this == current) throw new RuntimeException("Circular reference detected for node " + name);
      }
   }

   private void setParent(Node parent)
   {
      this.parent = parent;
   }

   @Override
   public String toString()
   {
      return Objects.toStringBuilder(getClass())
         .add("name", getName())
         .add("path", getPath())
         .add("label", getLabel())
         .add("visibility", getVisibility())
         .add("iconName", getIconName())
         .add("pageId", getPageId())
         .toString();
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof Node)) return false;

      Node node = (Node) o;

      if (!Objects.equals(name, node.name)) return false;
      if (!Objects.equals(label, node.label)) return false;
      if (!Objects.equals(visibility, node.visibility)) return false;
      if (!Objects.equals(iconName, node.iconName)) return false;
      if (!Objects.equals(pageId, node.pageId)) return false;
      if (!Objects.equals(children, node.children)) return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = name.hashCode();
      result = 31 * result + (label != null ? label.hashCode() : 0);
      result = 31 * result + (visibility != null ? visibility.hashCode() : 0);
      result = 31 * result + (iconName != null ? iconName.hashCode() : 0);
      result = 31 * result + (pageId != null ? pageId.hashCode() : 0);
      result = 31 * result + (children != null ? children.hashCode() : 0);
      return result;
   }
}