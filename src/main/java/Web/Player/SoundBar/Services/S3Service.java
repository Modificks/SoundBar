package Web.Player.SoundBar.Services;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface S3Service extends GeneralInterface<String> {

    List<String> uploadFiles(List<MultipartFile> files);
}