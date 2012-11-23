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

package org.gatein.api.portal.navigation;

import org.gatein.api.portal.Label;
import org.gatein.api.portal.page.PageId;
import org.gatein.api.util.Filter;

import java.net.URI;
import java.util.Comparator;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Node extends Iterable<Node>
{
   String getName();

   Node getParent();

   NodePath getNodePath();

   URI getResolvedURI();

   Label getLabel();

   void setLabel(Label label);

   String getResolvedLabel();

   boolean isVisible();

   Visibility getVisibility();

   void setVisibility(Visibility visibility);

   void setVisibility(boolean visible);

   void setVisibility(PublicationDate publicationDate);

   String getIconName();

   void setIconName(String iconName);

   PageId getPageId();

   void setPageId(PageId pageId);

   boolean isRoot();

   Node addChild(String childName);

   Node addChild(int index, String childName);

   Node getChild(String childName);

   Node getChild(int index);

   int getChildCount();

   boolean hasChild(String childName);

   boolean isChildrenLoaded();

   Node getDescendant(NodePath nodePath);

   int indexOf(String childName);

   boolean removeChild(String childName);

   Node filter(Filter<Node> filter);

   void sort(Comparator<Node> comparator);

   void moveTo(int index);

   void moveTo(Node parent);

   void moveTo(int index, Node parent);
}