package seeit3d.java.modeler.generator.metrics.annotation;

import java.lang.annotation.*;

import com.google.inject.BindingAnnotation;

@BindingAnnotation
@Target(value = { ElementType.LOCAL_VARIABLE })
@Retention(RetentionPolicy.RUNTIME)
public @interface LOC {

}
