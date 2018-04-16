package com.contactbook.service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import com.contactbook.Constants;


/**
 * Basic Authentication Filter, right now we added only one Role "USER"
 * Only requests with role set to "USER" are accepted. User name and password
 * are hard coded in code as of know. We can create more roles if needed.
 */
@Provider
public class UserAuthenticationFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;

	public static final String USER_ROLE = "USER";

	private static final String AUTHORIZATION = "Authorization";
	private static final String AUTHENTICATION_BASIC = "Basic";
	private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
			.entity("You are not authorized to do this operation").build();

	@Override
	public void filter(ContainerRequestContext requestContext) {
		Method method = resourceInfo.getResourceMethod();

		final MultivaluedMap<String, String> headers = requestContext.getHeaders();

		final List<String> authorization = headers.get(AUTHORIZATION);

		if (authorization == null || authorization.isEmpty()) {
			requestContext.abortWith(ACCESS_DENIED);
			return;
		}

		final String encodedCredentials = authorization.get(0).replaceFirst(AUTHENTICATION_BASIC + " ", "");

		String credentials = new String(Base64.decode(encodedCredentials.getBytes()));

		final StringTokenizer tokenizer = new StringTokenizer(credentials, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();

		if (method.isAnnotationPresent(RolesAllowed.class)) {
			RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
			Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));

			if (!isUserAllowed(username, password, rolesSet)) {
				requestContext.abortWith(ACCESS_DENIED);
				return;
			}
		}

	}

	private boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet) {
		boolean isAllowed = false;

		if (username.equals(Constants.REST_API_USER_NAME) && password.equals(Constants.REST_API_PASSWORD)) {
			if (rolesSet.contains(USER_ROLE)) {
				isAllowed = true;
			}
		}
		return isAllowed;
	}

}