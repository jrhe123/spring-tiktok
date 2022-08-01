package com.imooc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

	
	@Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("swagger-bootstrap-ui-demo RESTful APIs")
                        .description("# swagger-bootstrap-ui-demo RESTful APIs")
                        .termsOfServiceUrl("https://github.com/jrhe123")
                        .contact("jiarong1213@gmail.com")
                        .version("1.0.0")
                        .build())
                //group name
                .groupName("1.0.0")
                .select()
                //scan controllers
                .apis(RequestHandlerSelectors.basePackage("com.imooc.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
