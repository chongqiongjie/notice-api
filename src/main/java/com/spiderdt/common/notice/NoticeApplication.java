package com.spiderdt.common.notice;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Registers the components to be used by the JAX-RS application
 * 
 * @author spiderdt
 * 
 */
public class NoticeApplication extends ResourceConfig {

	/**
	 * Register JAX-RS application components.
	 */
	public NoticeApplication() {
        packages("com.spiderdt.common.notice");
//		// register filters
//		register(RequestContextFilter.class);
//		register(LoggingResponseFilter.class);
//		register(CORSResponseFilter.class);
//
//		// register exception mappers
//		register(GenericExceptionMapper.class);
//		register(AppExceptionMapper.class);
//      register(CustomReasonPhraseExceptionMapper.class);
//		register(NotFoundExceptionMapper.class);
//
		// register features
		register(JacksonFeature.class);
		register(EntityFilteringFeature.class);
	}
}
