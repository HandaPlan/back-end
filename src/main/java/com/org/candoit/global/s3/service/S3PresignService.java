package com.org.candoit.global.s3.service;

import com.org.candoit.global.s3.dto.PresignedUrlResponse;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3PresignService {

    private final S3Presigner s3Presigner;
    private final Clock clock;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public PresignedUrlResponse createUploadUrl(String memberId) {

        String fileName = createFileName(memberId);
        PutObjectRequest put = PutObjectRequest.builder()
            .bucket(bucket).key("profile/" + fileName)
            .contentType("image/jpeg")
            .build();

        PresignedPutObjectRequest signed = s3Presigner.presignPutObject(
            b -> b.signatureDuration(Duration.ofMinutes(5)).putObjectRequest(put));

        return new PresignedUrlResponse(signed.url().toString(), fileName);
    }

    private String createFileName(String memberId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now(clock).format(formatter);
        return memberId + "_" + timestamp + ".jpg";
    }
}
