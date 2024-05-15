package Web.Player.SoundBar.Services;

import Web.Player.SoundBar.ViewLayers.DTOs.TokenDTO;
import Web.Player.SoundBar.Domains.Entities.User;

public interface RefreshTokenService {

    TokenDTO generateTokens(User user);
}