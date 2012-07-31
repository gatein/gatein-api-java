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

import org.gatein.api.exception.EntityNotFoundException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Node extends Iterable<Node>
{
   /**
    * @return Id of the node
    */
   Id getId();

   /**
    * @return Name of the node
    */
   String getName();

   /**
    * @return Parent of the node
    */
   Node getParent();

   /**
    * Removes this node and detaches it from it's parent.
    * @return true if the node was removed, false otherwise
    */
   boolean removeNode();

   /**
    * @param name Name of the child node
    * @return Child node
    */
   Node getChild(String name);

   /**
    * Remove child node with given name
    * @param name Name of the node to remove
    * @return true if the child was removed, false otherwise
    * @throws EntityNotFoundException if the child does not exist
    */
   boolean removeChild(String name) throws EntityNotFoundException;

   /**
    * Create new child node
    * @param name Name of the node
    * @return New child node
    */
   Node addChild(String name);

   /**
    * Returns the number of child nodes for the given node.
    * @return number of child nodes
    */
   int getChildCount();

   /**
    * @return URI of the node
    */
   URI getURI();

   /**
    * @return Label of the node
    */
   Label getLabel();

   /**
    * @return Name of the icon for the node
    */
   String getIconName();

   /**
    * Sets the name of the icon for the node
    * @param iconName Name of the icon
    */
   void setIconName(String iconName);

   /**
    * @return Page associated with the node
    */
   Page getPage();

   /**
    * @return Id of the page associated with the node
    */
   Page.Id getPageId();

   /**
    * Associate Page with the node
    * @param pageId Id of the page
    */
   void setPageId(Page.Id pageId);

   /**
    * @return Visibility of the node
    */
   Visibility getVisibility();

   /**
    * Make the node be displayed higher in the UI list.
    */
   void moveUp();

   /**
    * Make the node be displayed lower in the UI list.
    */
   void moveDown();

   /**
    * The start date of the publication of the navigation node.
    *
    * @return the publication start date
    */
   Date getStartPublicationDate();

   /**
    * The end date of the publication of the navigation node.
    *
    * @return the publication end date
    */
   Date getEndPublicationDate();

   /**
    * Set's the publication of the navigation node. This will set the visibility to {@link Visibility#PUBLICATION}
    *
    * @param start the start date of the publication of the navigation node
    * @param end   the end date of the publication of the navigation node
    */
   void setPublication(Date start, Date end);

   /**
    * Set's the publication of the navigation node. This will set the visibility to {@link Visibility#PUBLICATION}. This also
    * removes any end publication date data.
    *
    * @param start the start date of the publication of the navigation node
    */
   void setPublication(Date start);

   /**
    * Removes the publication data of the navigation node, setting the visibility to the default {@link Visibility#VISIBLE}
    */
   void removePublication();

   /**
    * Sets the visibility of the navigation node.
    *
    * @param visible setting to true will set the node to it's default state of {@link Visibility#VISIBLE}, setting to false
    *                will set the node to it's hidden state {@link Visibility#HIDDEN}
    */
   void setVisible(boolean visible);

   public static enum Visibility
   {
      VISIBLE,
      HIDDEN,
      PUBLICATION,
      SYSTEM
   }

   class Id
   {
      private static final String[] EMPTY = new String[0];
      private final Site.Id siteId;
      private final String[] path;

      private Id(Site.Id siteId, String[] path)
      {
         this.siteId = siteId;
         this.path = (path == null) ? EMPTY : path;
      }

      public static Id create(Site.Id siteId, String...path)
      {
         if (siteId == null) throw new IllegalArgumentException("siteId cannot be null");

         return new Id(siteId, path);
      }

      public Site.Id getSiteId()
      {
         return siteId;
      }

      public String[] getPath()
      {
         return path;
      }

      public String getPathAsString()
      {
         if (path.length == 0) return PATH_SEP;

         StringBuilder sb = new StringBuilder();
         for (int i=0; i<path.length; i++)
         {
            if (i < path.length)
            {
               sb.append(PATH_SEP);
            }
            sb.append(path[i]);
         }

         return sb.toString();
      }

      @Override
      public String toString()
      {
         return format("Node.Id[siteType=%s, siteName=%s, path=%s]", false);
      }

      public String format(String format, boolean urlSafe)
      {
         if (format == null) throw new IllegalArgumentException("format cannot be null.");

         String typeName = siteId.getType().name;
         String siteName = siteId.getName();
         String nodePath = getPathAsString();

         if (urlSafe) siteName = Site.Id.fixSiteName(siteName);
         if (urlSafe) nodePath = fixNodePath(nodePath);

         return new Formatter().format(format, typeName, siteName, nodePath).toString();
      }

      public String format()
      {
         return format(FORMAT, true);
      }

      public static Id fromString(String idAsString)
      {
         if (idAsString == null) throw new IllegalArgumentException("idAsString cannot be null.");

         String[] parts = idAsString.split(Pattern.quote("" + SEP));
         if (parts.length != 3) throw new IllegalArgumentException("Invalid id format '" + idAsString + "'");

         Site.Id siteId = Site.Id.fromString(parts[0] + SEP + parts[1]);

         // Parse node path and fix
         String nodePath = parts[2];
         nodePath = nodePath.replaceAll(PATH_SEP_URL_SAFE, PATH_SEP);

         // Strip any empty paths
         String[] path = nodePath.split(PATH_SEP);
         List<String> list = new ArrayList<String>();
         for (String s : path)
         {
            if (s != null && s.length() > 0) list.add(s.trim());
         }

         return create(siteId, list.toArray(new String[list.size()]));
      }

      @Override
      public boolean equals(Object o)
      {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;

         Id id = (Id) o;

         if (!Arrays.equals(path, id.path)) return false;
         if (!siteId.equals(id.siteId)) return false;

         return true;
      }

      @Override
      public int hashCode()
      {
         int result = siteId.hashCode();
         result = 31 * result + Arrays.hashCode(path);
         return result;
      }

      private static String fixNodePath(String path)
      {
         return path.replaceAll(PATH_SEP, PATH_SEP_URL_SAFE);
      }

      static final char SEP = Site.Id.SEP;
      private static final String PATH_SEP = Site.Id.GROUP_SEP;
      private static final String PATH_SEP_URL_SAFE = Site.Id.GROUP_SEP_URL_SAFE;
      private static final String FORMAT = Site.Id.FORMAT + SEP + "%s";
   }
}
