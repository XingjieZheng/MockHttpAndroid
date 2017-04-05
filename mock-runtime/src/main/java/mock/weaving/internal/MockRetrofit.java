package mock.weaving.internal;

import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import mock.weaving.DebugMockRetrofit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by XingjieZheng
 * on 2017/4/4.
 */

@Aspect
public class MockRetrofit {
    private static volatile boolean enabled = true;

    @Pointcut("within(@mock.weaving.DebugMockRetrofit *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@mock.weaving.DebugMockRetrofit * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }

    public static void setEnabled(boolean enabled) {
        MockRetrofit.enabled = enabled;
    }

    @Around("method()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        enterMethod(joinPoint);

        long startNanos = System.nanoTime();
        long stopNanos;
        long lengthMillis;

        String url;
        String host = null;
        String port = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?> cls = methodSignature.getDeclaringType();
        Method method = methodSignature.getMethod();
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (annotation.annotationType().equals(DebugMockRetrofit.class)) {
                host = ((DebugMockRetrofit) annotation).host();
                port = ((DebugMockRetrofit) annotation).port();
            }
        }
        if (host == null || !methodSignature.getReturnType().equals(Retrofit.class)) {
            Log.e(asTag(cls), "@DebugMockRetrofit host can not be empty");
            startNanos = System.nanoTime();
            Object result = joinPoint.proceed();
            stopNanos = System.nanoTime();
            lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
            exitMethod(joinPoint, result, lengthMillis);
            return result;
        }
        if (port == null) {
            url = host;
        } else {
            url = host + ":" + port + "/";
        }
        url = "http://" + url;
        Log.i(asTag(cls), "@DebugMockRetrofit url:" + url);

        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        stopNanos = System.nanoTime();
        lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
        exitMethod(joinPoint, mRetrofit, lengthMillis);
        return mRetrofit;
    }


    private static void enterMethod(ProceedingJoinPoint joinPoint) {
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

        Log.d(asTag(cls), builder.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final String section = builder.toString().substring(2);
            Trace.beginSection(section);
        }
    }

    private static void exitMethod(ProceedingJoinPoint joinPoint, Object result, long lengthMillis) {
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

        Log.d(asTag(cls), builder.toString());
    }

    private static String asTag(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return asTag(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }
}
