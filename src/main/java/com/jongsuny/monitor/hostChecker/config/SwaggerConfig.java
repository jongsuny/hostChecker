package com.jongsuny.monitor.hostChecker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

/**
 * Created by jongsuny on 17/12/13.
 */

@Configuration
@EnableSwagger2
//@Profile({"local", "alpha", "beta"})
public class SwaggerConfig extends WebMvcConfigurerAdapter {
    private String SITE_URL = "http://kimnote.com/";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jongsuny.monitor.hostChecker.controller"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("HostChecker", "HostChecker api documents", "0.0.1-SNAPSHOT", SITE_URL,
                new Contact("HostChecker Dev", SITE_URL, "admin@localhost.com"), "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0", Arrays.asList(
                new StringVendorExtension("Admin", "Admin")));
        return apiInfo;
    }
}
