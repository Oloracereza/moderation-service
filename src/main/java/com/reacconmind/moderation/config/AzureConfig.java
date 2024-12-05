package com.reacconmind.moderation.config;

import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.ContentModeratorClient;
import com.microsoft.azure.cognitiveservices.vision.contentmoderator.ContentModeratorManager;
import com.microsoft.rest.RestClient;
import com.microsoft.rest.credentials.ServiceClientCredentials;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

@Data
@Configuration
@ConfigurationProperties(prefix = "azure.content-moderator")
public class AzureConfig {
    private String endpoint;
    private String key;
    private String textModerationEndpoint;
    private String imageModerationEndpoint;

    // Configuraciones de moderación
    private double adultContentThreshold = 0.7;
    private double racyContentThreshold = 0.6;
    private double violentContentThreshold = 0.5;

    // Configuraciones de seguridad
    private int maxImageSizeBytes = 20 * 1024 * 1024; // 20MB
    private List<String> allowedImageTypes = 
        List.of("image/jpeg", "image/png", "image/gif");

    @PostConstruct
    public void validate() {
        Objects.requireNonNull(key, "Azure API key must not be null");
        Objects.requireNonNull(endpoint, "Azure endpoint must not be null");
        Objects.requireNonNull(textModerationEndpoint, "Text moderation endpoint must not be null");
        Objects.requireNonNull(imageModerationEndpoint, "Image moderation endpoint must not be null");
    }

    @Bean
    public TextAnalyticsClient textAnalyticsClient() {
        return new TextAnalyticsClientBuilder()
            .credential(new AzureKeyCredential(key))
            .endpoint(endpoint)
            .buildClient();
    }

    @Bean
    public ContentModeratorClient contentModeratorClient() {
        ServiceClientCredentials credentials = new ServiceClientCredentials() {
            @Override
            public void applyCredentialsFilter(OkHttpClient.Builder builder) {
                builder.addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Ocp-Apim-Subscription-Key", key)
                            .build();
                    return chain.proceed(request);
                });
            }
        };

        RestClient restClient = new RestClient.Builder()
            .withBaseUrl(endpoint)
            .withCredentials(credentials)
            .build();

        return ContentModeratorManager.authenticate(restClient);
    }

    @Bean
    public AzureKeyCredential azureKeyCredential() {
        return new AzureKeyCredential(key);
    }
}
