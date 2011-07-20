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

import org.gatein.api.GateIn;
import org.gatein.api.id.Id;
import org.gatein.api.id.Identifiable;
import org.gatein.api.util.ParameterValidation;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class ManagedContent<T extends Content<T>> implements Identifiable<ManagedContent<T>>
{
   private final Id<ManagedContent<T>> id;
   private final Content<T> content;
   private String displayName;
   private String description;

   public ManagedContent(Content<T> content)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(content, "Content");
      this.id = content.getGateIn().managedContentId(content.getId());
      this.content = content;
   }

   public Id<ManagedContent<T>> getId()
   {
      return id;
   }

   public String getName()
   {
      return content.getName();
   }

   public String getDisplayName()
   {
      if (displayName != null)
      {
         return displayName;
      }
      return content.getDisplayName();
   }

   public GateIn getGateIn()
   {
      return content.getGateIn();
   }

   public void setDisplayName(String displayName)
   {
      this.displayName = displayName;
   }

   public Content<T> getContent()
   {
      return content;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }
}
