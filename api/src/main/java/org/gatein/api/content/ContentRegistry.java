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

package org.gatein.api.content;

import java.util.Collection;
import java.util.Set;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public interface ContentRegistry
{
   Category getOrCreateCategory(String name);

   Category getCategory(String name);

   void deleteCategory(String name);

   Set<String> getCategoryNames();

   Collection<Category> getAllCategories();

   Gadget createGadget(String gadget, String source);

   //

   /**
    * Locate a content from its id.
    *
    * @param id the id
    * @param <C> the content generic type
    * @return the content
    * @throws NullPointerException if the argument is null
    * @throws IllegalArgumentException if the argument is not valid
    */
   <C extends Content<C>> C getContent(Content.Id<C> id) throws NullPointerException, IllegalArgumentException;

}
