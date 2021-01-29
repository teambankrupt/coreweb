package com.example.coreweb.listeners;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EndpointsListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final List<Endpoint> endpoints = new ArrayList<>();

    public static List<String> getEndpoints(RequestMethod requestMethod) {
        return EndpointsListener.endpoints.stream()
                .filter(endpoint -> requestMethod.equals(endpoint.getMethod()))
                .filter(endpoint -> endpoint.getPattern() != null
                        && !endpoint.getPattern().contains("admin")
                        && !endpoint.getPattern().isEmpty()
                )
                .map(Endpoint::getPattern)
                .map(s -> s.replaceAll("\\{\\w+}","*"))
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods()
                .forEach((requestMappingInfo, handlerMethod) -> {
                    endpoints.add(toUrl(requestMappingInfo));
                });
    }

    public Endpoint toUrl(RequestMappingInfo info) {
        Endpoint endpoint = new Endpoint();

        if (!info.getMethodsCondition().isEmpty()) {
            Set<RequestMethod> patterns = info.getMethodsCondition().getMethods();
            RequestMethod method = (patterns.size() == 1 ? patterns.iterator().next() : null);
            endpoint.setMethod(method);
        }

        if (!info.getPatternsCondition().isEmpty()) {
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            endpoint.setPattern(patterns.size() == 1 ? patterns.iterator().next() : null);
        }

        return endpoint;
    }

    static class Endpoint {
        private RequestMethod method;
        private String pattern;

        public RequestMethod getMethod() {
            return method;
        }

        public void setMethod(RequestMethod method) {
            this.method = method;
        }

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }
    }
}
