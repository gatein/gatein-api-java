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

import java.io.Serializable;
import java.net.URI;
import java.util.Comparator;
import java.util.List;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Node extends Serializable
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

   boolean addChild(Node child);

   Node addChild(String childName);

   Node getChild(String childName);

   Node getChild(int index);

   List<Node> getChildren();

   boolean hasChild(String childName);

   boolean isChildrenLoaded();

   int indexOf(String childName);

   boolean removeChild(String childName);

   int size();

   Node filter(Filter<Node> node);

   void sort(Comparator<Node> comparator);
}