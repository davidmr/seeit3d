package seeit3d.general.model.factory.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface SeeIT3DFactoryEnabled {

	boolean singleton();
}
