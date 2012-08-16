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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public interface Site
{
   State getState();

   void saveState(State state);

   static class State implements Serializable
   {
      private final Label.State label;
      private final String description;

      public State(Label.State label, String description)
      {
         this.label = label;
         this.description = description;
      }
   }

   static class StateBuilder
   {
      private Label.StateBuilder label;
      private String description;

      public StateBuilder(State state)
      {
         this.label = new Label.StateBuilder(state.label);
         this.description = state.description;
      }

      public StateBuilder label(String label)
      {
         return this;
      }

      public StateBuilder label(Label.State labelState)
      {
         this.label = new Label.StateBuilder(labelState);
         return this;
      }

      public StateBuilder description(String description)
      {
         this.description = description;
         return this;
      }

      public State build()
      {
         return new State(label.build(), description);
      }
   }

   static enum Type
   {
      SITE("site"), SPACE("space"), DASHBOARD("dashboard");

      private final String name;

      Type(String name)
      {
         this.name = name;
      }

      public String getName()
      {
         return name;
      }

      public static Type forName(String name)
      {
         return MAP.get(name);
      }

      private static final Map<String, Type> MAP;
      static
      {
         final Map<String, Type> map = new HashMap<String, Type>();
         for (Type type : values())
         {
            final String name = type.getName();
            if (name != null) map.put(name, type);
         }
         MAP = map;
      }
   }
}
