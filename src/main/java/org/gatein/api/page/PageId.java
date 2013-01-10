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

package org.gatein.api.page;

import org.gatein.api.internal.Parameters;
import org.gatein.api.security.Group;
import org.gatein.api.security.User;
import org.gatein.api.site.SiteId;

import java.io.Serializable;
import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class PageId implements Formattable, Serializable {
    private final SiteId siteId;
    private final String pageName;

    public PageId(String siteName, String pageName) {
        this(new SiteId(siteName), pageName);
    }

    public PageId(Group group, String pageName) {
        this(new SiteId(group), pageName);
    }

    public PageId(User user, String pageName) {
        this(new SiteId(user), pageName);
    }

    public PageId(SiteId siteId, String pageName) {
        this.siteId = Parameters.requireNonNull(siteId, "siteId");
        this.pageName = Parameters.requireNonNull(pageName, "pageName");
    }

    /**
     * @return Id of the site
     */
    public SiteId getSiteId() {
        return siteId;
    }

    /**
     * @return Name of the page
     */
    public String getPageName() {
        return pageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PageId id = (PageId) o;

        return pageName.equals(id.pageName) && siteId.equals(id.siteId);
    }

    @Override
    public int hashCode() {
        int result = siteId.hashCode();
        result = 31 * result + pageName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("Page.Id[%s]", this);
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        if ((flags & FormattableFlags.ALTERNATE) == FormattableFlags.ALTERNATE) {
            formatter.format("%#s.%s", siteId, pageName);
        } else {
            formatter.format("siteId=[%s], pageName=%s", siteId, pageName);
        }
    }

    public static PageId fromString(String idAsString) {
        Parameters.requireNonNull(idAsString, "idAsString");

        int len;
        if (idAsString.startsWith("Page.Id[")) {
            idAsString = idAsString.substring(8, idAsString.length());
            len = idAsString.length() - 1; // cut off trailing ]
        } else {
            len = idAsString.length();
        }

        int end;
        int start;
        String pageName;
        if (idAsString.startsWith("siteId=[")) {
            start = 8;
            end = idAsString.lastIndexOf("], pageName=");
            pageName = idAsString.substring(end + 12, len);
        } else {
            start = 0;
            end = idAsString.lastIndexOf('.');
            pageName = idAsString.substring(end + 1, len);
        }

        SiteId siteId = SiteId.fromString(idAsString.substring(start, end));

        return new PageId(siteId, pageName);
    }
}
