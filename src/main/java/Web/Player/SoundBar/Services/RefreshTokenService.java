package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.Domains.Entities.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface RefreshTokenService {

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void checkAndDeleteToken(String token);

    void saveNewToken(String newRefreshToken, User user);
}