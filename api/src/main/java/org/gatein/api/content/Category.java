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

import org.gatein.api.id.BaseId;
import org.gatein.api.id.Identifiable;
import org.gatein.api.util.IterableCollection;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public interface Category extends Identifiable<Category>
{

   Id getId();

   boolean contains(String managedContentName);

   /**
    * Adds the Content referenced by the specified identifier to the set of Contents managed by this Category.
    *
    * @param contentId the id of the Content to be managed by this Category
    * @param name      the name (if any) associated with the Content once managed by this Category
    * @return a new ManagedContent proxying the associated Content and that can be manipulated in the context of this
    *         Category
    */
   ManagedContent addContent(Content.Id contentId, String name);

   String getDescription();

   void setDescription(String description);

   void setDisplayName(String displayName);

   void removeContent(String managedContentName);

   ManagedContent getManagedContent(String name);

   IterableCollection<String> getKnownManagedContentNames();

   IterableCollection<ManagedContent> getManagedContents();

   final class Id extends BaseId<Category>
   {

      /** . */
      private final String name;

      public Id(String name)
      {
         if (name == null)
         {
            throw new NullPointerException();
         }

         //
         this.name = name;
      }

      public Class<Category> getIdentifiableType()
      {
         return Category.class;
      }

      @Override
      public boolean equals(Object obj)
      {
         return obj instanceof Id && name.equals(((Id)obj).name);
      }

      @Override
      public int hashCode()
      {
         return name.hashCode();
      }
   }
}
