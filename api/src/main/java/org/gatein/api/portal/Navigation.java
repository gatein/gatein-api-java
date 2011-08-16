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

package org.gatein.api.portal;

import org.gatein.api.id.BaseId;
import org.gatein.api.id.Identifiable;
import org.gatein.api.util.HierarchicalContainer;

import java.net.URI;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public interface Navigation extends Identifiable<Navigation>, HierarchicalContainer<String, Navigation>
{

   Id getId();

   Page getTargetPage();

   void setTargetPage(Page target);

   void setTargetPageRef(Page.Id targetId);

   URI getURI();

   Site getSite();

   class Id extends BaseId<Navigation>
   {

      /** . */
      private final String value;

      public Id(String value)
      {
         if (value == null)
         {
            throw new NullPointerException();
         }
         this.value = value;
      }

      public Class<Navigation> getIdentifiableType()
      {
         return Navigation.class;
      }

      @Override
      public boolean equals(Object obj)
      {
         return obj == this || (obj instanceof Id && value.equals(((Id)obj).value));
      }

      @Override
      public int hashCode()
      {
         return value.hashCode();
      }
   }
}
