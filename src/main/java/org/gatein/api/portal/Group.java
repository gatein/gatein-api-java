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

import org.gatein.api.internal.Objects;
import org.gatein.api.internal.Strings;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class Group
{
   private final String id;

   public Group(String... group)
   {
      if (group == null) throw new IllegalArgumentException("group cannot be null");
      if (group.length == 1)
      {
         this.id = group[0];
      }
      else
      {
         this.id = Strings.joiner("/").leading().trimToNull().ignoreNulls().join(group);
      }
   }

   public Group(String id)
   {
      this(Strings.splitter("/").trim().ignoreEmptyStrings().split(id));
   }

   public String getId()
   {
      return id;
   }

   @Override
   public String toString()
   {
      return Objects.toStringBuilder(getClass())
         .add("groupId", id)
         .toString();
   }
}
