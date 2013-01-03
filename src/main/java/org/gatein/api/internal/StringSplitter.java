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
package org.gatein.api.internal;

import java.util.ArrayList;
import java.util.List;

public class StringSplitter {
    private final String regex;
    private final boolean trim;
    private final boolean ignoreEmptyStrings;
    private final int limit;

    private StringSplitter(String regex) {
        this(regex, false, false, 0);
    }

    private StringSplitter(String regex, boolean trim, boolean ignoreEmptyStrings, int limit) {
        this.regex = regex;
        this.trim = trim;
        this.ignoreEmptyStrings = ignoreEmptyStrings;
        this.limit = limit;
    }

    public StringSplitter trim() {
        return new StringSplitter(regex, true, ignoreEmptyStrings, limit);
    }

    public StringSplitter ignoreEmptyStrings() {
        return new StringSplitter(regex, trim, true, limit);
    }

    public StringSplitter limit(int limit) {
        return new StringSplitter(regex, trim, ignoreEmptyStrings, limit);
    }

    public String[] split(String string) {
        if (string == null)
            return null;

        String[] split = string.split(regex, limit);
        List<String> list = new ArrayList<String>(split.length);
        for (String s : split) {
            if (trim)
                s = trim(s);
            boolean add = !ignoreEmptyStrings || !(s == null || s.length() == 0);
            if (add) {
                list.add(s);
            }
        }

        return list.toArray(new String[list.size()]);
    }

    private String trim(String string) {
        return (string == null) ? null : string.trim();
    }

    public static StringSplitter splitter(String regex) {
        return new StringSplitter(regex);
    }
}