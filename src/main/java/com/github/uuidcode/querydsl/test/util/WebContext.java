package com.github.uuidcode.querydsl.test.util;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class WebContext {
    private static ThreadLocal<WebContext> threadLocal = new ThreadLocal<>();

    private Runnable runnable;

    public static WebContext of() {
        return new WebContext();
    }

    public Runnable getRunnable() {
        return this.runnable;
    }

    public WebContext setRunnable(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public static WebContext set(Runnable runnable) {
        WebContext webContext = WebContext.of().setRunnable(runnable);
        threadLocal.set(webContext);
        return webContext;
    }

    public static Optional<WebContext> get() {
        return ofNullable(threadLocal.get());
    }
}
