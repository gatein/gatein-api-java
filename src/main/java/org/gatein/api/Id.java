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

package org.gatein.api;

import java.util.Arrays;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class Id implements Comparable<Id>
{
   private final static String SEPARATOR = "=";
   private final String containerId;
   private final String portalId;
   private final String invokerId;
   private final String portletId;
   private final String instanceId;

   public Id(String containerId, String portalId, String... children)
   {
      ParameterValidation.throwIllegalArgExceptionIfNullOrEmpty(containerId, "container identifier", null);
      ParameterValidation.throwIllegalArgExceptionIfNullOrEmpty(portalId, "portal identifier", null);

      this.containerId = containerId;
      this.portalId = portalId;

      if (ParameterValidation.existsAndIsNotEmpty(children))
      {
         switch (children.length)
         {
            case 1:
               this.invokerId = children[0];
               this.portletId = null;
               this.instanceId = null;
               return;
            case 2:
               if (!ParameterValidation.isNullOrEmpty(children[0]))
               {
                  this.invokerId = children[0];
                  this.portletId = children[1];
                  this.instanceId = null;
                  return;
               }
               break;
            case 3:
               if (!ParameterValidation.isNullOrEmpty(children[0]))
               {
                  this.invokerId = children[0];

                  if (!ParameterValidation.isNullOrEmpty(children[1]))
                  {
                     this.portletId = children[1];
                     this.instanceId = children[2];
                  }
                  else
                  {
                     this.portletId = null;
                     this.instanceId = null;
                  }
                  return;
               }
               break;
            default:
               String components = children[0];
               for (int i = 1; i < children.length; i++)
               {
                  components += ", " + children[i];
               }
               throw new IllegalArgumentException("Id only has 5 possible components. Got: " + containerId + ", " + portalId + ", " + components);
         }
      }

      this.invokerId = null;
      this.portletId = null;
      this.instanceId = null;
   }

   public static Id parse(String idAsString)
   {
      String[] split = idAsString.split(SEPARATOR);
      if (ParameterValidation.existsAndIsNotEmpty(split) && split.length >= 2)
      {
         return new Id(split[0], split[1], Arrays.copyOfRange(split, 2, split.length));
      }
      else
      {
         throw new IllegalArgumentException("Invalid id: " + idAsString);
      }
   }

   public static Id getIdForChild(Id parent, String childId)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(parent, "Parent resource");
      ParameterValidation.throwIllegalArgExceptionIfNullOrEmpty(childId, "child identifier", null);

      String invokerId = parent.getInvokerId();
      if (invokerId == null)
      {
         return new Id(parent.getContainerId(), parent.getPortalId(), childId);
      }
      else
      {
         String portletId = parent.getPortletId();
         if (portletId == null)
         {
            return new Id(parent.getContainerId(), parent.getPortalId(), invokerId, childId);
         }
         else
         {
            return new Id(parent.getContainerId(), parent.getPortalId(), invokerId, portletId, childId);
         }
      }
   }

   public static String asString(Id key)
   {
      return key.containerId + SEPARATOR + key.portalId + (key.invokerId != null ? SEPARATOR + key.invokerId + (key.portletId != null ? SEPARATOR + key.portletId + (key.instanceId != null ? SEPARATOR + key.instanceId : "") : "") : "");
   }

   public String getContainerId()
   {
      return containerId;
   }

   public String getPortalId()
   {
      return portalId;
   }

   public String getInstanceId()
   {
      return instanceId;
   }

   public String getInvokerId()
   {
      return invokerId;
   }

   public String getPortletId()
   {
      return portletId;
   }

   @Override
   public String toString()
   {
      return asString(this);
   }

   public int compareTo(Id o)
   {
      return Id.asString(this).compareTo(Id.asString(o));
   }

   @Override
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (o == null || getClass() != o.getClass())
      {
         return false;
      }

      Id id = (Id)o;

      if (!containerId.equals(id.containerId))
      {
         return false;
      }
      if (!portalId.equals(id.portalId))
      {
         return false;
      }
      if (invokerId != null ? !invokerId.equals(id.invokerId) : id.invokerId != null)
      {
         return false;
      }
      if (portletId != null ? !portletId.equals(id.portletId) : id.portletId != null)
      {
         return false;
      }
      if (instanceId != null ? !instanceId.equals(id.instanceId) : id.instanceId != null)
      {
         return false;
      }

      return true;
   }

   @Override
   public int hashCode()
   {
      int result = containerId.hashCode();
      result = 31 * result + portalId.hashCode();
      result = 31 * result + (invokerId != null ? invokerId.hashCode() : 0);
      result = 31 * result + (portletId != null ? portletId.hashCode() : 0);
      result = 31 * result + (instanceId != null ? instanceId.hashCode() : 0);
      return result;
   }
}
