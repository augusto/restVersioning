package com.augusto.test.web;

import com.augusto.test.spring.CustomRequestMappingHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = {"com.augusto.test.web.controller"})
public class WebConfiguration {

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        CustomRequestMappingHandlerMapping handlerMapping = new CustomRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setRemoveSemicolonContent(false);
        handlerMapping.setContentNegotiationManager(contentNegotiationManager());

        return handlerMapping;
    }

    @Bean
    public ContentNegotiationManager contentNegotiationManager() {
        ContentNegotiationManagerFactoryBean contentNegotiationManagerFactoryBean = new ContentNegotiationManagerFactoryBean();
        Map<String, MediaType> mediaTypes = new HashMap<>();
        mediaTypes.put("json", MediaType.APPLICATION_JSON);
        mediaTypes.put("html", MediaType.TEXT_HTML);
        contentNegotiationManagerFactoryBean.addMediaTypes(mediaTypes);

        contentNegotiationManagerFactoryBean.afterPropertiesSet();

        return contentNegotiationManagerFactoryBean.getObject();
    }

    @Bean
    public ConversionService conversionService() {
        FormattingConversionServiceFactoryBean conversionServiceFactoryBean = new FormattingConversionServiceFactoryBean();
        conversionServiceFactoryBean.afterPropertiesSet();
        return conversionServiceFactoryBean.getObject();
    }

    @Bean
    public List<HttpMessageConverter<?>> messageConverters() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        messageConverters.add(new ByteArrayHttpMessageConverter());
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setWriteAcceptCharset(false);
        messageConverters.add(stringHttpMessageConverter);
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
        messageConverters.add(new SourceHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());

        return messageConverters;
    }

    @Bean
    public ConfigurableWebBindingInitializer webBindingInitializer() {
        ConfigurableWebBindingInitializer webBindingInitializer = new ConfigurableWebBindingInitializer();
        webBindingInitializer.setConversionService(conversionService());

        return webBindingInitializer;
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        requestMappingHandlerAdapter.setContentNegotiationManager(contentNegotiationManager());
        requestMappingHandlerAdapter.setWebBindingInitializer(webBindingInitializer());
        requestMappingHandlerAdapter.setMessageConverters(messageConverters());

        return requestMappingHandlerAdapter;
    }

    @Bean
    public ConversionServiceExposingInterceptor conversionServiceExposingInterceptor() {
        return new ConversionServiceExposingInterceptor(conversionService());
    }

    @Bean
    public MappedInterceptor mappedInterceptor(ConversionServiceExposingInterceptor conversionServiceExposingInterceptor) {
        return new MappedInterceptor(null, conversionServiceExposingInterceptor);
    }

    @Bean
    public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionResolver.setContentNegotiationManager(contentNegotiationManager());
        exceptionResolver.setMessageConverters(messageConverters());
        exceptionResolver.setOrder(0);

        return exceptionResolver;
    }

    @Bean
    public ResponseStatusExceptionResolver responseStatusExceptionResolver() {
        ResponseStatusExceptionResolver responseStatusExceptionResolver = new ResponseStatusExceptionResolver();
        responseStatusExceptionResolver.setOrder(1);

        return responseStatusExceptionResolver;
    }

    @Bean
    public DefaultHandlerExceptionResolver defaultHandlerExceptionResolver() {
        DefaultHandlerExceptionResolver defaultHandlerExceptionResolver = new DefaultHandlerExceptionResolver();
        defaultHandlerExceptionResolver.setOrder(2);

        return defaultHandlerExceptionResolver;
    }
}
