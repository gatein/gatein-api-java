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

package org.gatein.api.id;

import org.gatein.api.GateInObject;
import org.gatein.api.Ids;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class ContextTestCase
{
   @Test
   public void simpleContext()
   {
      Context context = new Context.ContextBuilder("foo").withDefaultSeparator("-").requiredComponent("component", GateInObject.class, Pattern.compile(".*")).createContext();

      assert 0 == context.getIndexFor("component");
      assert context.isComponentRequired("component");
      context.validate("foo");
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testExtraComponents()
   {
      Ids.PORTLET.validate("container", "portal", "foo", "bar", "barInstance", "unknown");
   }

   @Test
   public void testPortletCase()
   {
      Ids.PORTLET.validate("container", "portal");
      Ids.PORTLET.validate("container", "portal", "foo");
      Ids.PORTLET.validate("container", "portal", "foo", "bar");
      Ids.PORTLET.validate("container", "portal", "foo", "bar", "barInstance");
   }

   @Test
   public void simpleHierarchicalContextShouldWork()
   {
      final Context context = new Context.ContextBuilder("hierarchical").withDefaultSeparator("/")
         .requiredComponent("foo", GateInObject.class, Pattern.compile(".*foo$"))
         .requiredUnboundedHierarchicalComponent("bar", GateInObject.class, Pattern.compile("^bar.*"))
         .createContext();
      assert context.isComponentUnboundedHierarchical("bar");
      assert context.isComponentRequired("bar");
      context.validate("foo", "bar");
      context.validate("foo", "bar", "bar");
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void missingRequiredHierarchicalShouldBeDetected()
   {
      final Context context = new Context.ContextBuilder("hierarchical").withDefaultSeparator("/")
         .requiredComponent("foo", GateInObject.class, Pattern.compile(".*foo$"))
         .requiredUnboundedHierarchicalComponent("bar", GateInObject.class, Pattern.compile("^bar.*"))
         .createContext();
      context.validate("foo");
   }

   @Test
   public void shouldValidateProperlyWhenThereAreRequiredComponentsAfterHierarchical()
   {
      final Context context = new Context.ContextBuilder("hierarchical").withDefaultSeparator("/")
         .requiredComponent("foo", GateInObject.class, Pattern.compile(".*foo$"))
         .requiredUnboundedHierarchicalComponent("bar", GateInObject.class, Pattern.compile("^bar.*"))
         .requiredComponent("baz", GateInObject.class, Pattern.compile("^baz\\d*"))
         .createContext();
      context.validate("foo", "bar", "baz");
      context.validate("foo", "bar", "bar2", "baz");
   }
}
