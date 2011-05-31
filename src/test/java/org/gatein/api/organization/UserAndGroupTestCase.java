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

package org.gatein.api.organization;

import org.gatein.api.Ids;
import org.gatein.api.IterableResult;
import org.gatein.api.id.Id;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class UserAndGroupTestCase
{
   private User root;
   private Id<User> id;

   @BeforeTest
   public void setUp()
   {
      id = Ids.userId("root");
      root = Users.get(id);
   }

   @Test(enabled = false)
   public void userGroupsFromGroupsOrUserShouldMatch()
   {
      IterableResult<Group> rootGroups = root.getGroups();
      assert rootGroups.equals(Groups.getForUser(id));
   }

   @Test(enabled = false)
   public void membershipsShouldMatch()
   {
      Id<Group> groupId = Ids.groupId("platform", "administrators");
      final Group adminGroup = root.getGroup(groupId);
      assert adminGroup.equals(Groups.get(groupId));
      assert Groups.isUserMemberOf(root.getId(), groupId);
   }
}
