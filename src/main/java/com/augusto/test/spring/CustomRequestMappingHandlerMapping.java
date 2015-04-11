package com.augusto.test.spring;

import com.augusto.test.spring.version.RestAcceptRequestCondition;
import com.augusto.test.spring.version.VersionedMedia;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        VersionedMedia typeAnnotation = AnnotationUtils.findAnnotation(handlerType, VersionedMedia.class);
        return createCondition(typeAnnotation);
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        VersionedMedia methodAnnotation = AnnotationUtils.findAnnotation(method, VersionedMedia.class);
        return createCondition(methodAnnotation);
    }

    private RequestCondition<?> createCondition(VersionedMedia versionMapping) {
        if (versionMapping != null) {
            return new RestAcceptRequestCondition(versionMapping.media(), versionMapping.from(), versionMapping.to());
        }

        return null;
    }

}