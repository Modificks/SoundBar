package Web.Player.SoundBar.Annotations.Validators;

import Web.Player.SoundBar.Annotations.Nickname;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NicknameValidator implements ConstraintValidator<Nickname, String> {

    private static final String REGEXP = "^[a-zA-Zа-яА-Я0-9'.-_~`!@#$%^&*()=+|/<>₴№:?]{1,38}$";

    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext context) {
        return nickname.matches(REGEXP);
    }
}
/**
 * Valid nicks:
 * Alex_1990
 * Ірина-Київ
 * Super$tar
 * Василь№1
 * Gamer!Pro
 * <p>
 * Invalid nicks:
 * All nicks with length > 38
 * Or specific symbols that were not mentions in REGEXP
 */