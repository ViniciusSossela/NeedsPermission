package vsossella.com.needspermission;

import android.support.annotation.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by vsossella on 05/01/18.
 */


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedsPermission {

    String[] permissions() default "";

    Class<?> activityExplanation() default Nullable.class;

}