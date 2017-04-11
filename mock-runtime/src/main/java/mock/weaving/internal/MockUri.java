package mock.weaving.internal;

import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import mock.weaving.DebugMockRetrofit;

/**
 * Created by XingjieZheng
 * on 2017/4/11.
 */
@Aspect
public class MockUri {
    private static volatile boolean enabled = true;
    private static final String ARG_URL = "url";

    @Pointcut("execution(@mock.weaving.MockUri * *(..))")
    public void executionCreateUrl() {
    }

    @Pointcut("execution(@mock.weaving.MockUriRequest * *(..))")
    public void executionRequest() {
    }

    @Pointcut("cflow(executionRequest() && !within(MockUri))")
    public void cflowRequest() {
    }

    @Pointcut("cflowRequest() && executionCreateUrl()")
    public void createUrlInRequest() {
    }

    @Around("createUrlInRequest()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        enterMethod(joinPoint);

        long startNanos = System.nanoTime();
        long stopNanos;
        long lengthMillis;

        String uriString;
        String host = null;
        String port = null;
        String url = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取注解参数
        Class<?> cls = methodSignature.getDeclaringType();
        Method method = methodSignature.getMethod();
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (annotation.annotationType().equals(mock.weaving.MockUri.class)) {
                host = ((mock.weaving.MockUri) annotation).host();
                port = ((mock.weaving.MockUri) annotation).port();
            }
        }

        //获取方法参数
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = methodSignature.getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            System.out.println(paramNames[i] + "," + paramValues[i]);
            if (ARG_URL.equals(paramNames[i])) {
                url = (String) paramValues[i];
            }
        }

        if (host == null
                || !methodSignature.getReturnType().equals(URI.class)
                || url == null) {
            Log.e(asTag(cls), "@MockUri host can not be empty," +
                    " or method return class type is no URI," +
                    " or arg url is null");
            startNanos = System.nanoTime();
            Object result = joinPoint.proceed();
            stopNanos = System.nanoTime();
            lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
            exitMethod(joinPoint, result, lengthMillis);
            return result;
        }
        if (port == null) {
            uriString = host;
        } else {
            uriString = host + ":" + port;
        }
        uriString = "http://" + uriString + "/" + url;

        Log.i(asTag(cls), "@MockUri uri:" + uriString);

        URI uri = new URI(uriString);

        stopNanos = System.nanoTime();
        lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
        exitMethod(joinPoint, uri, lengthMillis);
        return uri;
    }

    private static void enterMethod(JoinPoint joinPoint) {
        if (!enabled) return;

        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class<?> cls = codeSignature.getDeclaringType();
        String methodName = codeSignature.getName();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        StringBuilder builder = new StringBuilder("\u21E2 ");
        builder.append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameterNames[i]).append('=');
            builder.append(Strings.toString(parameterValues[i]));
        }
        builder.append(')');

        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }

        Log.v(asTag(cls), builder.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final String section = builder.toString().substring(2);
            Trace.beginSection(section);
        }
    }

    private static void exitMethod(JoinPoint joinPoint, Object result, long lengthMillis) {
        if (!enabled) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection();
        }

        Signature signature = joinPoint.getSignature();

        Class<?> cls = signature.getDeclaringType();
        String methodName = signature.getName();
        boolean hasReturnType = signature instanceof MethodSignature
                && ((MethodSignature) signature).getReturnType() != void.class;

        StringBuilder builder = new StringBuilder("\u21E0 ")
                .append(methodName)
                .append(" [")
                .append(lengthMillis)
                .append("ms]");

        if (hasReturnType) {
            builder.append(" = ");
            builder.append(Strings.toString(result));
        }

        Log.v(asTag(cls), builder.toString());
    }

    private static String asTag(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return asTag(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }
}
