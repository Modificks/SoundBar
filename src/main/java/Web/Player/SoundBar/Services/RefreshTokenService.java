package Web.Player.SoundBar.Services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@FunctionalInterface
public interface RefreshTokenService {

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}