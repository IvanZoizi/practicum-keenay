package org.example.exception.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.SQLTransientConnectionException;
import java.sql.SQLTimeoutException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ExceptionDetection {

    private static final List<Class<?>> RETRYABLE_EXCEPTIONS = Arrays.asList(
            SocketTimeoutException.class,
            ConnectException.class,
            UnknownHostException.class,
            SQLTransientConnectionException.class,
            SQLTimeoutException.class
    );

    private static final List<Class<?>> NON_RETRYABLE_EXCEPTIONS = Arrays.asList(
            IllegalArgumentException.class,
            NullPointerException.class
    );

    private static final List<Pattern> RETRYABLE_PATTERNS = Arrays.asList(
            Pattern.compile("(?i).*timeout.*"),
            Pattern.compile("(?i).*timed out.*"),
            Pattern.compile("(?i).*connection refused.*"),
            Pattern.compile("(?i).*connection reset.*"),
            Pattern.compile("(?i).*host unreachable.*"),
            Pattern.compile("(?i).*couldn't connect.*"),
            Pattern.compile("(?i).*rate limit.*"),
            Pattern.compile("(?i).*too many requests.*"),
            Pattern.compile("(?i).*throttling.*"),
            Pattern.compile("(?i).*quota.*"),
            Pattern.compile("(?i).*not enough replicas.*"),
            Pattern.compile("(?i).*leader not available.*"),
            Pattern.compile("(?i).*deadlock.*"),
            Pattern.compile("(?i).*lock timeout.*")
    );

    private static final List<Pattern> NON_RETRYABLE_PATTERNS = Arrays.asList(
            Pattern.compile(".*550\\s+5\\.1\\.1.*"),
            Pattern.compile("(?i).*user unknown.*"),
            Pattern.compile("(?i).*recipient address rejected.*"),
            Pattern.compile("(?i).*mailbox unavailable.*"),
            Pattern.compile(".*554\\s+5\\.7\\.1.*"),
            Pattern.compile("(?i).*relay access denied.*"),
            Pattern.compile("(?i).*message rejected.*"),
            Pattern.compile("(?i).*invalid email.*"),
            Pattern.compile("(?i).*already processed.*"),
            Pattern.compile("(?i).*already confirmed.*"),
            Pattern.compile("(?i).*token not found.*"),
            Pattern.compile("(?i).*invalid token.*"),
            Pattern.compile("(?i).*request not found.*"),
            Pattern.compile("(?i).*not found.*")
    );


    public Boolean checkRetentionException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        for (Class<?> clazz : NON_RETRYABLE_EXCEPTIONS) {
            if (clazz.isAssignableFrom(ex.getClass())) {
                return false;
            }
        }

        for (Class<?> clazz : RETRYABLE_EXCEPTIONS) {
            if (clazz.isAssignableFrom(ex.getClass())) {
                return true;
            }
        }

        String message = ex.getMessage();
        if (message != null) {
            for (Pattern pattern : NON_RETRYABLE_PATTERNS) {
                if (pattern.matcher(message).matches()) {
                    return false;
                }
            }
        }

        if (message != null) {
            for (Pattern pattern : RETRYABLE_PATTERNS) {
                if (pattern.matcher(message).matches()) {
                    return true;
                }
            }
        }

        return true;
    }
}