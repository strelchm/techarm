package ru.strelchm.techarm.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class OpenApiConfig {
    public static final String SUCCESS_MESSAGE_FIELD = "Success";

    private final Environment env;

    @Autowired
    public OpenApiConfig(Environment env) {
        this.env = env;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("maps")
                .packagesToScan("ru.strelchm.techarm.api")
                .pathsToMatch("/api/**")
//            .addOpenApiCustomiser(OpenApiCustomiser(openApi: OpenAPI ->{}))
                .build();
    }

    @Bean
    public OpenAPI mapsOpenAPIInfo()  {
        StringSchema schema = new StringSchema();
        return new OpenAPI()
                .addServersItem(new Server().url(env.getProperty("swagger.conf.base-url"))).info(
                        new Info()
                                .title("Tech ARM REST API")
                                .description("Tech ARM REST API")
                                .version("v1.0")
                                .license(new License()
                                                .name("Apache License Version 2.0")
                                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                                )
                )
                .components(new Components().addParameters(
                                "myGlobalHeader",
                                new HeaderParameter().required(true).name("My-Global-Header").description("My Global Header")
                                        .schema(schema)
                        )
                );
    }

    @Bean
    public OpenApiCustomiser customerGlobalHeaderOpenApiCustomizer() {
        return openApi -> openApi.getPaths().values().stream().flatMap(pathItem -> pathItem.readOperations().stream())
                .forEach(operation -> operation.addParametersItem(new HeaderParameter().$ref("#/components/parameters/myGlobalHeader")));
    }
}
