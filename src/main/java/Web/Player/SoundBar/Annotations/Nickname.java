package Web.Player.SoundBar.Annotations;

import Web.Player.SoundBar.Annotations.Validators.NicknameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NicknameValidator.class)
public @interface Nickname {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}