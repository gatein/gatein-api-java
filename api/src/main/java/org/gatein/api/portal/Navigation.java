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

package org.gatein.api.portal;

import org.gatein.api.exception.EntityAlreadyExistsException;
import org.gatein.api.exception.EntityNotFoundException;

/**
 * Representation of Navigation. It is a tree structure of nodes with associated Pages.
 * Each Navigation has a matching Site.
 *
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @author <a href="mailto:bdawidow@redhat.com">Boleslaw Dawidowicz</a>
 */
public interface Navigation extends Iterable<Node>
{
   /**
    * Returns the site associated with the navigation.
    *
    * @return the site
    */
   Site getSite();

   /**
    * //TODO: Not sure if this is proper description of priority and why/how it's used. Maybe we don't expose this ?
    * Priority defines order in which navigation should be displayed in the UI.
    *
    * @return priority index
    */
   int getPriority();

   /**
    * @param priority priority index to be set.
    */
   void setPriority(int priority);

   /**
    * Returns the node at the given path.
    *
    * @param path of a specific navigation node
    * @return Node associated with specified path
    */
   Node getNode(String... path);

   /**
    * Removes the node specified by the path
    *
    * @param path Path of node to be removed
    * @return true if the node was removed, false otherwise
    * @throws EntityNotFoundException if the node to be removed was not found
    */
   boolean removeNode(String... path) throws EntityNotFoundException;

   /**
    * Adds a node specified by the path.
    *
    * @param path Path of the node to add
    * @return New node.
    */
   Node addNode(String... path) throws EntityAlreadyExistsException;

   /**
    * The node count representing the child nodes of the root navigation.
    *
    * @return the node count
    */
   int getNodeCount();
}
