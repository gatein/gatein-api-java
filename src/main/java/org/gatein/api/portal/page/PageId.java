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

package org.gatein.api.portal.page;

import org.gatein.api.internal.ArraysExt;
import org.gatein.api.internal.Strings;
import org.gatein.api.portal.BaseId;
import org.gatein.api.portal.Group;
import org.gatein.api.portal.User;
import org.gatein.api.portal.site.SiteId;

import java.util.regex.Pattern;

/**
* @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
*/
public class PageId extends BaseId
{
   private final SiteId siteId;
   private final String pageName;

   public PageId(String siteName, String pageName)
   {
      this(new SiteId(siteName), pageName);
   }

   public PageId(Group group, String pageName)
   {
      this(new SiteId(group), pageName);
   }

   public PageId(User user, String pageName)
   {
      this(new SiteId(user), pageName);
   }

   public PageId(SiteId siteId, String pageName)
   {
      if (siteId == null) throw new IllegalArgumentException("siteId cannot be null");
      if (pageName == null) throw new IllegalArgumentException("pageName cannot be null");

      this.siteId = siteId;
      this.pageName = pageName;
   }

   /**
    * @return Id of the site
    */
   public SiteId getSiteId()
   {
      return siteId;
   }

   /**
    * @return Name of the page
    */
   public String getPageName()
   {
      return pageName;
   }

   @Override
   public Object[] getFormatArguments()
   {
      return ArraysExt.concat(siteId.getFormatArguments(), pageName);
   }

   @Override
   public Object[] getFormatArguments(Adapter adapter)
   {
      return ArraysExt.concat(siteId.getFormatArguments(adapter), adapter.adapt(2, pageName));
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      PageId id = (PageId) o;

      return pageName.equals(id.pageName) && siteId.equals(id.siteId);
   }

   @Override
   public int hashCode()
   {
      int result = siteId.hashCode();
      result = 31 * result + pageName.hashCode();
      return result;
   }

   @Override
   public String toString()
   {
      return format("Page.Id[siteType=%s, siteName=%s, pageName=%s]");
   }

   public String format()
   {
      return format("%s.%s.%s", groupAdapter());
   }

   public static PageId fromString(String idAsString)
   {
      if (idAsString == null) throw new IllegalArgumentException("idAsString cannot be null.");
      String[] parts = Strings.splitter(Pattern.quote(".")).split(idAsString);
      if (parts.length != 3) throw new IllegalArgumentException("Invalid id format '" + idAsString + "'");

      SiteId siteId = SiteId.fromString(parts[0] + "." + parts[1]);
      String pageName = parts[2];

      return new PageId(siteId, pageName);
   }
}
