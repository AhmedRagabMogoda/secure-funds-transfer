package com.transferapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Explicitly maps file extensions to correct MIME types.
     * Without this, Railway's JVM environment sometimes serves .js files
     * as text/plain, causing browsers to reject them with a strict MIME
     * type checking error — while Postman ignores this entirely.
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .favorParameter(false)
                .ignoreAcceptHeader(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("js",    MediaType.parseMediaType("application/javascript"))
                .mediaType("mjs",   MediaType.parseMediaType("application/javascript"))
                .mediaType("css",   MediaType.parseMediaType("text/css"))
                .mediaType("html",  MediaType.parseMediaType("text/html;charset=UTF-8"))
                .mediaType("ico",   MediaType.parseMediaType("image/x-icon"))
                .mediaType("json",  MediaType.parseMediaType("application/json"))
                .mediaType("woff",  MediaType.parseMediaType("font/woff"))
                .mediaType("woff2", MediaType.parseMediaType("font/woff2"))
                .mediaType("map",   MediaType.parseMediaType("application/json"));
    }

    /**
     * Explicitly registers static resource handlers.
     * Ensures Angular's compiled assets are served correctly
     * before any controller mapping has a chance to intercept them.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(
                        "/*.js", "/*.css", "/*.ico",
                        "/*.json", "/*.txt", "/*.map",
                        "/*.woff", "/*.woff2"
                )
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(86400);

        registry
                .addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCachePeriod(86400);

        registry
                .addResourceHandler("/media/**")
                .addResourceLocations("classpath:/static/media/")
                .setCachePeriod(86400);
    }
}