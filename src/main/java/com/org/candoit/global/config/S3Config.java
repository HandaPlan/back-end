package com.org.candoit.global.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Bean
    @Profile("!local & !test")
    public S3Presigner prodS3Presigner() {
        return S3Presigner.builder()
            .region(Region.AP_NORTHEAST_2)
            .build();
    }

    @Bean
    @Profile("local")
    public S3Presigner localS3Presigner(
        @Value("${cloud.aws.profile}") String profile
    ) {
        return S3Presigner.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(ProfileCredentialsProvider.create(profile))
            .build();
    }
}
