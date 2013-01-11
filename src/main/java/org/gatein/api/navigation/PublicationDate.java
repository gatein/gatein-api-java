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

import org.gatein.api.internal.ObjectToStringBuilder;
import org.gatein.api.internal.Parameters;
import org.gatein.api.navigation.Visibility.Status;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents the dates a {@link Node} with the {@link Visibility#getStatus()} set to {@link Status#PUBLICATION} is visible.
 * 
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class PublicationDate implements Serializable {
    /**
     * Creates a new publication date that starts on the specified date
     * 
     * @param start the start date
     * @return the publication date
     * @throws IllegalArgumentException if start is null
     */
    public static PublicationDate startingOn(Date start) {
        Parameters.requireNonNull(start, "start");

        return new PublicationDate(Parameters.requireNonNull(start, "start").getTime(), -1);
    }

    /**
     * Creates a new publication date that ends on the specified date
     * 
     * @param end the end date
     * @return the publication date
     * @throws IllegalArgumentException if end is null
     */
    public static PublicationDate endingOn(Date end) {
        Parameters.requireNonNull(end, "end");

        return new PublicationDate(-1, end.getTime());
    }

    /**
     * Creates a new publication date that starts on the specified start date and ends on the specified end date
     * 
     * @param start the start date
     * @param end the end date
     * @return the publication date
     * @throws IllegalArgumentException if start or end is null, or end date is before start date
     */
    public static PublicationDate between(Date start, Date end) {
        Parameters.requireNonNull(start, "start");
        Parameters.requireNonNull(end, "end");
        
        if (end.before(start)) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        return new PublicationDate(start.getTime(), end.getTime());
    }

    private final long start;
    private final long end;

    private PublicationDate(long start, long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns true if the publication date is within the specified date
     * 
     * @param date the date
     * @return true if the publication date is within the specified date; false otherwise
     * @throws IllegalArgumentException if date is null
     */
    public boolean within(Date date) {
        Parameters.requireNonNull(date, "date");

        return within(date.getTime());
    }

    /**
     * Returns true if the publication date is within the specified date
     * 
     * @param time the date
     * @return true if the publication date is within the specified date; false otherwise
     */
    public boolean within(long time) {
        if (start != -1 && start > time) {
            return false;
        }
        if (end != -1 && end < time) {
            return false;
        }

        return true;
    }

    /**
     * Returns the start date or null if no start date is set
     * 
     * @return the start date or null if no start date is set
     */
    public Date getStart() {
        return (start < 0) ? null : new Date(start);
    }

    /**
     * Returns the end date or null if no end date is set
     * 
     * @return the end date or null if no end date is set
     */
    public Date getEnd() {
        return (end < 0) ? null : new Date(end);
    }

    @Override
    public String toString() {
        return ObjectToStringBuilder.toStringBuilder().add("start", new Date(start)).add("end", new Date(end)).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PublicationDate))
            return false;

        PublicationDate that = (PublicationDate) o;

        return (end == that.end) && (start == that.start);
    }

    @Override
    public int hashCode() {
        int result = (int) (start ^ (start >>> 32));
        result = 31 * result + (int) (end ^ (end >>> 32));
        return result;
    }
}
