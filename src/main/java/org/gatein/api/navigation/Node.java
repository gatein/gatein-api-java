/*
 * JBoss, a division of Red Hat
 * Copyright 2011, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
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

package org.gatein.api.navigation;

import org.gatein.api.Filter;
import org.gatein.api.GateInObject;
import org.gatein.api.IterableResult;
import org.gatein.api.Permission;
import org.gatein.api.Portal;
import org.gatein.api.PropertyInfo;
import org.gatein.api.Query;
import org.gatein.api.id.Id;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public interface Node<T extends Node<T>> extends GateInObject<T>
{
   IterableResult<? extends Node> getChildren();

   <U extends Node> IterableResult<U> getChildren(Query<U> query);

   <U extends Node> IterableResult<U> getChildrenWhere(Filter<U> filter);

   Node getParent();

   boolean accessAllowedFromUser(Id userId, Permission.Type operation);

   boolean hasOwner(Id ownerId);

   String getTitle();

   void setTitle(String title);

   <T> void setProperty(PropertyInfo<T> info, T value);

   <T> T getProperty(PropertyInfo<T> info);

   int getChildrenNumber();

   /**
    * adding permissions only if they don't already exist
    *
    * @param permission
    */
   void addPermission(Permission permission);

   boolean hasChild(String childName);

   <T extends Node<T>> T createChild(String childName, Class<T> childType);

   <T extends Node<T>> T getChild(String childName, Class<T> childType);

   Type<Node<T>> getOwnerType();

   public class Type<U extends Node>
   {
      public static final Type<Portal> SITE = new Type<Portal>();
      public static final Type<Node> DASHBOARD = new Type<Node>();
      public static final Type<Node> GROUP = new Type<Node>();
      public static final Type<Node> SELF = new Type<Node>();
   }
}
