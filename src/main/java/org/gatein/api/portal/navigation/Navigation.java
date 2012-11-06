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

import org.gatein.api.portal.Label;
import org.gatein.api.portal.site.SiteId;
import org.gatein.api.util.Filter;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Navigation for a site.
 *
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Navigation
{
   SiteId getSiteId();

   Integer getPriority();

   void setPriority(Integer integer);

   Node getNode(NodePath nodePath);

   Node getNode(NodeVisitor visitor, Filter<Node> filter);

   void loadNodes(Node parent, NodeVisitor visitor);

   void saveNode(Node node);

   String resolveLabel(Node node, Locale locale);
}
