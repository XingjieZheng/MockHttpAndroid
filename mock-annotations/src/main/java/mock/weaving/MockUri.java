package mock.weaving;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by XingjieZheng
 * on 2017/4/4.
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface MockUri {
    String host();

    String port() default "";
}
