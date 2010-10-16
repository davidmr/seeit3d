package seeit3d.java.modeler.generator.annotation;

import java.lang.annotation.*;

import com.google.inject.BindingAnnotation;

@BindingAnnotation
@Target(value = { ElementType.PARAMETER, ElementType.LOCAL_VARIABLE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CompilationUnitModeler {

}
