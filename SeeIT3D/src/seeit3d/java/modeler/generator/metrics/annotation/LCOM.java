package seeit3d.java.modeler.generator.metrics.annotation;

import java.lang.annotation.*;

import com.google.inject.BindingAnnotation;

@BindingAnnotation
@Target(value = { ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface LCOM {

}
