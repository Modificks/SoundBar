package Web.Player.SoundBar.Configs.AwsConfig;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class AwsConfig {

    private final AwsS3Properties awsS3Properties;

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new EndpointConfiguration(
                                awsS3Properties.getEndpointUrl(),
                                awsS3Properties.getRegion()
                        )
                )
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(awsS3Properties.getAccessKey(), awsS3Properties.getSecretKey())
                ))
                .build();
    }
}