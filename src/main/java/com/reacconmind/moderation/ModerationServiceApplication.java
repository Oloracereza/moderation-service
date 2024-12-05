package com.reacconmind.moderation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.reacconmind.moderation.config.AzureConfig;

@SpringBootApplication
@EnableConfigurationProperties(AzureConfig.class)
@OpenAPIDefinition(
    info = @Info(
        title = "Content Moderation API",
        version = "1.0",
        description = "API for moderating text, image, and video content using Azure Content Moderator"
    )
)
public class ModerationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModerationServiceApplication.class, args);
    }
}
