package com.flipkart.databuilderframework.engine.util;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
public class TimedExecutor {

    public static <T, E extends Exception> T run(final String name, HandlerAdapterWithException<T, E> handler) throws E {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            return handler.run();
        } finally {
            log.debug("Execution of {} took {} ms", name, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
    }
}
