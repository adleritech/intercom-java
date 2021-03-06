package io.intercom.api;

import java.net.URI;

public class Intercom {

    static class Context {
        private volatile String token;
        private volatile int connectionTimeout = 3 * 1000;
        private volatile int requestTimeout = 60 * 1000;
        private volatile boolean requestUsingCaches = false;
    }

    private static final URI API_BASE_URI = URI.create("https://api.intercom.io/");

    private static volatile boolean useThreadLocal = false;

    private static volatile URI apiBaseURI = API_BASE_URI;

    private static final String VERSION = "2.8.0";

    public static final String USER_AGENT = "intercom-java/" + Intercom.VERSION;

    private static ThreadLocal<Context> threadContext = newThreadLocalContext();

    private static final Context staticContext = new Context();

    private static Context getContext() {
        if (useThreadLocal) {
            return threadContext.get();
        }
        return staticContext;
    }

    private static volatile HttpConnectorSupplier httpConnectorSupplier = HttpConnectorSupplier.defaultSupplier;

    public static long currentTimestamp() {
        return System.currentTimeMillis()/1000;
    }

    public static int getConnectionTimeout() {
        return getContext().connectionTimeout;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void setConnectionTimeout(int connectionTimeout) {
        getContext().connectionTimeout = connectionTimeout;
    }

    public static int getRequestTimeout() {
        return getContext().requestTimeout;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void setRequestTimeout(int requestTimeout) {
        getContext().requestTimeout = requestTimeout;
    }

    public static boolean isRequestUsingCaches() {
        return getContext().requestUsingCaches;
    }

    public static void setRequestUsingCaches(boolean requestUsingCaches) {
        getContext().requestUsingCaches = requestUsingCaches;
    }

    public static HttpConnectorSupplier getHttpConnectorSupplier() {
        return httpConnectorSupplier;
    }

    public static void setHttpConnectorSupplier(HttpConnectorSupplier supplier) {
        Intercom.httpConnectorSupplier = supplier;
    }

    public static void setToken(String token) {
        Context context = getContext();
        context.token = token;
    }

    public static URI getApiBaseURI() {
        return Intercom.apiBaseURI;
    }

    public static void setApiBaseURI(URI apiBaseURI) {
        Intercom.apiBaseURI = apiBaseURI;
    }

    public static String getToken() {
        return getContext().token;
    }

    public static boolean usesThreadLocal() {
        return Intercom.useThreadLocal;
    }

    public static void setUseThreadLocal(boolean useThreadLocal) {
        Intercom.useThreadLocal = useThreadLocal;
    }

    public static void clearThreadLocalContext() {
        threadContext.remove();
    }

    public static void clearThreadLocalContexts() {
        threadContext = newThreadLocalContext();
    }

    private static ThreadLocal<Context> newThreadLocalContext() {
        return new ThreadLocal<Context>() {
            @Override protected Context initialValue() {
                return new Context();
            }
        };
    }
}
