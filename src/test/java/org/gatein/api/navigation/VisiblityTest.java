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
package org.gatein.api.navigation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.gatein.api.navigation.Visibility.Status;
import org.junit.Test;

public class VisiblityTest {

    @Test
    public void isVisible() {
        assertTrue(new Visibility().isVisible());
        assertTrue(new Visibility(Status.VISIBLE).isVisible());
        assertFalse(new Visibility(Status.SYSTEM).isVisible());
        assertFalse(new Visibility(Status.HIDDEN).isVisible());
    }

    @Test
    public void isVisible_PublicationDate() {
        assertTrue(new Visibility(PublicationDate.startingOn(new Date(System.currentTimeMillis() - 1000))).isVisible());
        assertTrue(new Visibility(PublicationDate.endingOn(new Date(System.currentTimeMillis() + 1000))).isVisible());
        assertTrue(new Visibility(PublicationDate.between(new Date(System.currentTimeMillis() - 1000),
                new Date(System.currentTimeMillis() + 1000))).isVisible());

        assertFalse(new Visibility(PublicationDate.startingOn(new Date(System.currentTimeMillis() + 1000))).isVisible());
        assertFalse(new Visibility(PublicationDate.endingOn(new Date(System.currentTimeMillis() - 1000))).isVisible());
        assertFalse(new Visibility(PublicationDate.between(new Date(System.currentTimeMillis() - 1000),
                new Date(System.currentTimeMillis() - 1000))).isVisible());
    }

}
