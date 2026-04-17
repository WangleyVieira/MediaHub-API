package com.mediahub.mediahub_api.service.infra;

import com.mediahub.mediahub_api.infrastruture.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public String uploadFile(MultipartFile file, Long albumId) {

        try {
            ensureBucketExists();

            String fileName = "albums/" + albumId + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return generateFileUrl(fileName);

        } catch (Exception e) {
            throw new RuntimeException("Error when uploading", e);
        }
    }

    private String generateFileUrl(String fileName) {
        return minioProperties.getUrl() + "/" + minioProperties.getBucket() + "/" + fileName;
    }

    private void ensureBucketExists() throws Exception {

        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .build()
        );

        if (!exists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .build()
            );
        }
    }
}
