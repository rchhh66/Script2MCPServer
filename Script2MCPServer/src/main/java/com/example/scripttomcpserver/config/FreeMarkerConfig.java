package com.example.scripttomcpserver.config;

import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
public class FreeMarkerConfig {

    @Value("${mcp.server.template.directory}")
    private String templateDirectory;

    @Bean
    public freemarker.template.Configuration freemarkerConfig(ResourceLoader resourceLoader) throws IOException {
        freemarker.template.Configuration config = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
        config.setDirectoryForTemplateLoading(resourceLoader.getResource(templateDirectory).getFile());
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(false);
        return config;
    }
}