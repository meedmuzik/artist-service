package org.scuni.artistservice.service;

import io.minio.*;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.scuni.artistservice.configuration.MinioProperties;
import org.scuni.artistservice.entity.Image;
import org.scuni.artistservice.exception.ImageUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static final String JPEG_EXTENSION = "jpeg";
    private static final String PNG_EXTENSION = "png";
    private static final String JPG_EXTENSION = "jpg";

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public Image download(String fileName) {
        byte[] imageBytes;
        try (GetObjectResponse stream =
                     minioClient.getObject(GetObjectArgs
                             .builder()
                             .bucket(minioProperties.getBucket())
                             .object(fileName)
                             .build())) {
            imageBytes = stream.readAllBytes();
        } catch (MinioException | InvalidKeyException | IOException |
                 NoSuchAlgorithmException e) {
            throw new ImageUploadException("Image download failed: " + e.getMessage());
        }
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return new Image(imageBytes, extension);
    }

    public String upload(MultipartFile file) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed: "
                                           + e.getMessage());
        }
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUploadException("Image must have name.");
        }
        String fileName = generateFileName(file);
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed: "
                                           + e.getMessage());
        }
        saveImage(inputStream, fileName);
        return fileName;
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    private String generateFileName(MultipartFile file) {
        String extension = getExtension(file);
        if (Objects.equals(extension, JPEG_EXTENSION) || extension.equals(JPG_EXTENSION) || extension.equals(PNG_EXTENSION))
            return UUID.randomUUID() + "." + extension;
        else throw new ImageUploadException("File is not an image");
    }

    private String getExtension(MultipartFile file) {
        return file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void saveImage(InputStream inputStream, String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }
}
