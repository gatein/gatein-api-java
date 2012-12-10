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

package org.gatein.api.portal.site;

import org.gatein.api.portal.BaseId;
import org.gatein.api.portal.Group;
import org.gatein.api.portal.User;
import org.gatein.api.portal.page.PageId;

import java.util.regex.Pattern;

/**
* @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
*/
public class SiteId extends BaseId
{
   private final SiteType type;
   private final String name;

   public SiteId(String name)
   {
      this(SiteType.SITE, name);
   }

   public SiteId(Group group)
   {
      this(SiteType.SPACE, group.getId());
   }

   public SiteId(User user)
   {
      this(SiteType.DASHBOARD, user.getId());
   }

   public SiteId(SiteType type, String name)
   {
      if (type == null) throw new IllegalArgumentException("type cannot be null");
      if (name == null) throw new IllegalArgumentException("name cannot be null");

      this.type = type;
      this.name = name;
   }

   /**
    * The SiteType for this SiteId
    *
    * @return the SiteType
    */
   public SiteType getType()
   {
      return type;
   }

   /**
    * The name of the site.
    *
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * Creates a new PageId from this SiteId
    *
    * @param pageName the name of the page
    * @return a new PageId
    */
   public PageId page(String pageName)
   {
      return new PageId(this, pageName);
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      SiteId id = (SiteId) o;

      return name.equals(id.name) && type == id.type;
   }

   @Override
   public int hashCode()
   {
      int result = type.hashCode();
      result = 31 * result + name.hashCode();
      return result;
   }

   @Override
   public String toString()
   {
      return format("Site.Id[type=%s, name=%s]");
   }

   public String format()
   {
      return format("%s.%s", groupAdapter());
   }

   public static SiteId fromString(String idAsString)
   {
      if (idAsString == null) throw new IllegalArgumentException("idAsString cannot be null.");

      String[] parts = idAsString.split(Pattern.quote("."));
      if (parts.length != 2) throw new IllegalArgumentException("Invalid id format '" + idAsString + "'");

      String typeName = parts[0];
      String siteName = parts[1];
      siteName = siteName.replaceAll("~", "/");

      SiteType type = SiteType.forName(typeName);
      if (type == null)
         throw new IllegalArgumentException("Invalid site type '" + typeName + "'" + " for id format '" + idAsString + "'");

      return new SiteId(type, siteName);
   }

   @Override
   public Object[] getFormatArguments()
   {
      return getFormatArguments(null);
   }

   @Override
   public Object[] getFormatArguments(Adapter adapter)
   {
      Object[] args = new Object[2];
      args[0] = adapt(0, type.getName(), adapter);
      args[1] = adapt(1, name, adapter);

      return args;
   }

   private Object adapt(int index, Object original, Adapter adapter)
   {
      return (adapter == null) ? original : adapter.adapt(index, original);
   }
}
