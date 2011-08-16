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
import org.gatein.api.id.Id;
import org.gatein.api.id.Identifiable;
import org.gatein.api.util.IterableIdentifiableCollection;

import java.util.Collection;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public interface Page extends Identifiable<Page>
{

   Id getId();

   Site getSite();

   String getTitle();

   void setTitle(String title);

   public static class Id extends BaseId<Page>
   {

      /** . */
      private final Site.Id site;

      /** . */
      private final String name;

      public Id(Site.Id site, String name)
      {
         if (site == null)
         {
            throw new NullPointerException();
         }
         if (name == null)
         {
            throw new NullPointerException();
         }

         //
         this.site = site;
         this.name = name;
      }

      public Site.Id getSite()
      {
         return site;
      }

      public String getName()
      {
         return name;
      }

      public Class<Page> getIdentifiableType()
      {
         return Page.class;
      }

      @Override
      public boolean equals(Object obj)
      {
         if (obj == this)
         {
            return true;
         }
         if (obj instanceof Id)
         {
            Id that = (Id)obj;
            return site.equals(that.site) && name.equals(that.name);
         }
         return false;
      }

      @Override
      public int hashCode()
      {
         return site.hashCode() ^ name.hashCode();
      }
   }

}
