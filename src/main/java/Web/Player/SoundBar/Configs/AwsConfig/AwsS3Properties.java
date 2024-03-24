package Web.Player.SoundBar.Configs.AwsConfig;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AwsS3Properties {

    @Value("${cloud.aws.s3.endpointUrl}")
    private String endpointUrl;

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.s3.bucket.default.acl}")
    private CannedAccessControlList defaultAcl;
}