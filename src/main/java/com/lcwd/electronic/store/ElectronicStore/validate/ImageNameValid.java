package com.lcwd.electronic.store.ElectronicStore.validate;

import jakarta.persistence.Table;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
/*
  @Retention specifies how long the annotation is available
  SOURCE:available only in source code, discarded during compilation
  CLASS:stored in the .class file but ignored by the JVM at runtime(default)
  RUNTIME:Available at runtime via reflection-spring relies on this!
  In Spring Boot,almost all custom annotation use
  @Retention(RetentionPolicy.RUNTIME) because Spring's dependency injection and AOP use reflection
  at runtime.
  AOP:stands for Aspect-Oriented Programming.instead of writing logging,security,transaction,caching,
  performance monitoring code repetedly insde core bussiness code,AOP let you write once and apply
  it automatically wherever needed.
 */
@Retention(RetentionPolicy.RUNTIME)
/* when we use documentated annotation on our custom annotation,it means:The annotation will show
up in the generated JavaDocs.
 */
@Documented
/*this annotation that we put on fields,methods,or parameters to validate their value*/
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {
    //error message
    String message() default "Invalid Image Name";

    //represent group of constraints
    Class<?>[] groups() default {};

    //additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
