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

import org.gatein.api.security.Group;
import org.gatein.api.security.User;
import org.gatein.api.portal.page.PageId;

import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
*/
public class SiteId implements Formattable
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
      return String.format("Site.Id[%s]", this);
   }

   @Override
   public void formatTo(Formatter formatter, int flags, int width, int precision)
   {
      if ((flags & FormattableFlags.ALTERNATE) == FormattableFlags.ALTERNATE)
      {
         formatter.format("%s.%s", type.getName(), name.replaceAll("/", "~"));
      }
      else
      {
         formatter.format("type=%s, name=%s", type.getName(), name);
      }
   }

   private static final String NAME_REGEX = "[A-Za-z0-9-_/~]";
   private static final String TYPE_REGEX = "\\w{4,9}";
   private static final Pattern PATTERN1 = Pattern.compile("type=("+TYPE_REGEX+"), name=(" + NAME_REGEX + "*)");
   private static final Pattern PATTERN2 = Pattern.compile("("+TYPE_REGEX+")\\.(" + NAME_REGEX +"*)");

   public static SiteId fromString(String idAsString)
   {
      if (idAsString == null) throw new IllegalArgumentException("idAsString cannot be null.");

      int len = idAsString.length();
      if (idAsString.startsWith("Site.Id[type=")) // Handle the toString()
      {
         int begin = idAsString.indexOf("type=") + 5;
         int end = idAsString.indexOf(",", begin);
         String type = idAsString.substring(begin, end);

         begin = idAsString.indexOf("name=") + 5;
         end = len - 1;
         String name = idAsString.substring(begin, end);

         return new SiteId(SiteType.forName(type), name);
      }
      else // Handle both %s and %#s formats
      {
         // Match two patterns (easier then making one complicated regex)
         Matcher matcher = PATTERN1.matcher(idAsString);
         boolean matches = matcher.matches();
         if (!matches)
         {
            matcher = PATTERN2.matcher(idAsString);
            matches = matcher.matches();
         }

         // Parse it
         if (matches)
         {
            String type = matcher.group(1);
            String name = matcher.group(2);
            if (name.contains("~"))
            {
               name = name.replaceAll("~", "/");
            }
            return new SiteId(SiteType.forName(type), name);
         }
      }

      throw new IllegalArgumentException("Unknown syntax for id string " + idAsString);
   }
}
