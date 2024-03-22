package Web.Player.SoundBar.Services.Impl;

import Web.Player.SoundBar.Configs.AwsConfig.AwsS3Properties;
import Web.Player.SoundBar.Services.S3Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Service {

    private final AmazonS3 amazonS3;

    private final AwsS3Properties awsS3Properties;

    private String generateUniqueName(MultipartFile multipartFile) {
        UUID uuid = UUID.randomUUID();

        return multipartFile.getOriginalFilename().replace(" ", "_") + "-" + uuid;
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();

        try {
            ExecutorService executorService = Executors.newFixedThreadPool(files.size());

            List<CompletableFuture<String>> completableFutures = new ArrayList<>();

            files.forEach(fileBytes -> {
                CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
                    String fileName = generateUniqueName(fileBytes);
                    ByteArrayInputStream inputStream;
                    try {
                        inputStream = new ByteArrayInputStream(fileBytes.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    ObjectMetadata metadata = new ObjectMetadata();

                    metadata.setContentLength(fileBytes.getSize());
                    metadata.setContentType("MP3");

                    amazonS3.putObject(
                            new PutObjectRequest(awsS3Properties.getBucketName(), fileName, inputStream, metadata)
                                    .withCannedAcl(awsS3Properties.getDefaultAcl())
                    );

                    return amazonS3.getUrl(awsS3Properties.getBucketName(), fileName).toString();
                });

                completableFutures.add(completableFuture);

            });

            completableFutures.forEach(future -> {
                try {
                    String url = future.get();
                    urls.add(url);

                } catch (InterruptedException | ExecutionException exception) {

                    log.error("One of the thread ended with exception. Reason: {}", exception.getMessage());
                    throw new RuntimeException(exception);
                }
            });

            executorService.shutdown();

        } catch (AmazonS3Exception exception) {

            log.error("Error uploading file to storage. Reason: {}", exception.getMessage());
            throw new AmazonS3Exception(exception.getMessage());
        }

        return urls;
    }
}