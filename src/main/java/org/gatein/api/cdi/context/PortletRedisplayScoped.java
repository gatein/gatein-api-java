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
 * <p>Specifies that a bean is portlet redisplay scoped.</p>
 *
 * <p>The portlet redisplay scope is active during:</p>
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
 * <p>The portlet redisplay scope is also active during:</p>
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
 * <p>The portlet redisplay scope is destroyed:</p>
 *
 * <ul>
 * <li>when a call is made to the <tt>Portlet.processAction()</tt> method,</li>
 * <li>when a call is made to the <tt>EventPortlet.processEvent()</tt> method after a previous call to
 * <tt>Portlet.render()</tt> has completed,</li>
 * <li>when a call to <tt>ResourceServingPortlet.serveResource()</tt> results in navigation to a new view, or</li>
 * <li>limitations in available resources require the scope to be garbage collected.</li>
 * </ul>
 *
 * <p>During a call to <tt>ResourceServingPortlet.serveResource()</tt> a separate context will be activated for the
 * duration of the method execution, and any <tt>ResourceFilter.doFilter()</tt> methods defined. When the context is
 * destroyed, once <tt>ResourceServingPortlet.serveResource()</tt> and all <tt>ResourceFilter.doFilter()</tt> methods
 * are complete, any beans present in the context that are not present in the existing portlet redisplay context
 * will be added. This ensures that state created as part of <tt>ResourceServingPortlet.serveResource()</tt> is
 * available to subsequent <tt>Portlet.render()</tt> calls.</p>
 *
 * @author <a href="http://community.jboss.org/people/kenfinni">Ken Finnigan</a>
 */

@Target( { TYPE, METHOD, FIELD })
@Retention(RUNTIME)
@Documented
@NormalScope(passivating = true)
@Inherited
public @interface PortletRedisplayScoped {
}
