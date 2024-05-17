package com.example.social_media.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.TimeUnit;

@Component
@Order(1) // Optional: Set a filter execution order if needed
public class RateLimitFilter implements Filter {

    private final ConcurrentHashMap<String, RateLimitEntry> requestCounts = new ConcurrentHashMap<>();
    private final long rateLimit = 500; // Maximum requests allowed within the time window

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ipAddress = request.getRemoteAddr();

        // Retrieve or create a RateLimitEntry for the IP address
        RateLimitEntry entry = requestCounts.computeIfAbsent(ipAddress, k -> new RateLimitEntry());

        // Check if the request exceeds the  rate limit within the current time window
        if (entry.isRequestOverLimit(rateLimit)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            httpResponse.getWriter().write("Rate limit exceeded. Please try again later.");
            return;
        }
        // Allow the request to proceed and update the counter
        entry.incrementCount();
        chain.doFilter(request, response);
    }
}

class RateLimitEntry {
    private final AtomicLong count = new AtomicLong();
    private  long lastResetTime = System.currentTimeMillis();
    private final long timeWindow = TimeUnit.SECONDS.toMillis(3);
    public boolean isRequestOverLimit(long limit) {
        // Check if the time window has elapsed since the last request
        if (isTimeWindowReset()) {
            count.set(0);
            lastResetTime = System.currentTimeMillis();
        }

        return count.incrementAndGet() > limit;
    }

    private boolean isTimeWindowReset() {
        return System.currentTimeMillis() - lastResetTime >= timeWindow;
    }

    public void incrementCount() {
        count.incrementAndGet();
    }
}
