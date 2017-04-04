package mock.weaving;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by XingjieZheng
 * on 2017/4/4.
 */
@Target({TYPE, METHOD, CONSTRUCTOR}) @Retention(CLASS)
public @interface DebugLog {
}
