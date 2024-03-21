package com.lofominhili.farmflow.utils;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "FarmFlow Api",
                version = "1.0.0",
                contact = @Contact(
                        name = "daniel",
                        url = "https://github.com/lofominhili/FarmFlow"
                )
        )
)
public class OpenApiConfig {
}
