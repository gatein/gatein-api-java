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
import org.gatein.api.util.ExternalizedBase64;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;

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

   class Id extends ExternalizedBase64
   {
      private Site.Id siteId;
      private String[] path;

      private Id(Site.Id siteId, String[] path)
      {
         super(new byte[]{(byte) 'n'});
         this.siteId = siteId;
         this.path = path;
      }

      private Id()
      {
         this(null, null);
      }

      public static Id create(Site.Id siteId, String...path)
      {
         if (siteId == null) throw new IllegalArgumentException("siteId cannot be null");

         return new Id(siteId, path);
      }

      public static Id fromBase64(InputStream stream) throws IOException
      {
         Id id = new Id();
         id.readExternalBase64(stream);
         return id;
      }

      public static Id fromBase64String(String base64)
      {
         try
         {
            return fromBase64(new ByteArrayInputStream(base64.getBytes("UTF-8")));
         }
         catch (IOException e)
         {
            final IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
            iae.setStackTrace(e.getStackTrace());
            throw iae;
         }
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
         if (path == null) return null;

         StringBuilder sb = new StringBuilder();
         for (int i=0; i<path.length; i++)
         {
            if (i > 0)
            {
               sb.append('/');
            }
            sb.append(path[i]);
         }

         return sb.toString();
      }

      @Override
      public String toString()
      {
         return "Node.Id[path=" + getPathAsString() + ", " + siteId + "]";
      }

      @Override
      protected void writeExternal(DataOutput out) throws IOException
      {
         siteId.writeExternal(out);
         out.writeUTF(getPathAsString());
      }

      @Override
      protected void readExternal(DataInput in) throws IOException
      {
         Site.Id si = new Site.Id();
         si.readExternal(in);
         this.siteId = si;
         this.path = in.readUTF().split("/");
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
         result = 31 * result + (path != null ? Arrays.hashCode(path) : 0);
         return result;
      }
   }
}
