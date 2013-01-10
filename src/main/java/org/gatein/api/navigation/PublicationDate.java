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

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class PublicationDate implements Serializable {
    public static PublicationDate startingOn(Date start) {
        Parameters.requireNonNull(start, "start");

        return new PublicationDate(Parameters.requireNonNull(start, "start").getTime(), -1);
    }

    public static PublicationDate endingOn(Date end) {
        Parameters.requireNonNull(end, "end");

        return new PublicationDate(-1, end.getTime());
    }

    public static PublicationDate between(Date start, Date end) {
        Parameters.requireNonNull(start, "start");
        Parameters.requireNonNull(end, "end");

        return new PublicationDate(start.getTime(), end.getTime());
    }

    private final long start;
    private final long end;

    private PublicationDate(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public boolean within(Date date) {
        Parameters.requireNonNull(date, "date");

        return within(date.getTime());
    }

    public boolean within(long time) {
        return (time >= start && time <= end);
    }

    public Date getStart() {
        return (start < 0) ? null : new Date(start);
    }

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
