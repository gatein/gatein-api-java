package org.gatein.api.cdi.context;

import javax.enterprise.context.NormalScope;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>Specifies that a bean is portlet lifecycle scoped.</p>
 *
 * <p>The portlet lifecycle scope is active during:</p>
 *
 * <ul>
 * <li>the <tt>Portlet.processAction()</tt> method,</li>
 * <li>the <tt>EventPortlet.processEvent()</tt> method,</li>
 * <li>the <tt>Portlet.render()</tt> method or</li>
 * <li>the <tt>ResourceServingPortlet.serveResource()</tt> method</li>
 * </ul>
 *
 * of any portlet request.
 *
 * <p>The portlet lifecycle scope is also active during:</p>
 *
 * <ul>
 * <li>the <tt>ActionFilter.doFilter()</tt> method,</li>
 * <li>the <tt>EventFilter.doFilter()</tt> method,</li>
 * <li>the <tt>RenderFilter.doFilter()</tt> method, and</li>
 * <li>the <tt>ResourceFilter.doFilter()</tt> method.</li>
 * </ul>
 *
 * of any portlet filter.
 *
 * <p>The portlet lifecycle scope is destroyed at the end of the portlet request, after any:</p>
 *
 * <ul>
 * <li><tt>Portlet.processAction()</tt> method,</li>
 * <li><tt>EventPortlet.processEvent()</tt> method,</li>
 * <li><tt>Portlet.render()</tt> method or</li>
 * <li><tt>ResourceServingPortlet.serveResource()</tt> method.</li>
 * </ul>
 *
 * and after all:
 *
 * <ul>
 * <li><tt>ActionFilter.doFilter()</tt> methods,</li>
 * <li><tt>EventFilter.doFilter()</tt> methods,</li>
 * <li><tt>RenderFilter.doFilter()</tt> methods, and</li>
 * <li><tt>ResourceFilter.doFilter()</tt> methods.</li>
 * </ul>
 *
 * <p>There are two method call sequences in which the portlet lifecycle scope is active:</p>
 *
 * <ol>
 * <li>during <tt>ActionRequest</tt> -> <tt>EventRequest</tt> -> <tt>RenderRequest</tt>,
 * along with their associated <tt>doFilter()</tt> methods, and</li>
 * <li>during <tt>ResourceRequest</tt>, along with its associated <tt>doFilter()</tt> method.</li>
 * </ol>
 *
 * @author <a href="http://community.jboss.org/people/kenfinni">Ken Finnigan</a>
 */

@Target( { TYPE, METHOD, FIELD })
@Retention(RUNTIME)
@Documented
@NormalScope
@Inherited
public @interface PortletLifecycleScoped {
}
