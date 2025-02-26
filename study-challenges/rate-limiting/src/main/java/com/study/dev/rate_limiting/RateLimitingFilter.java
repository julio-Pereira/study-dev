package com.study.dev.rate_limiting;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@PropertySource("classpath:application.yml")
@Component
public class RateLimitingFilter implements Filter {

    private final ConcurrentHashMap<String, AtomicInteger> requestCountsPerIpAddress = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Value("${spring.app.rate.limit}")
    private int MAX_REQUESTS_PER_MINUTE;

    @Value("${spring.app.rate.duration}")
    private long rateDuration;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String clientIpAddress = httpServletRequest.getRemoteAddr();

        requestCountsPerIpAddress.putIfAbsent(clientIpAddress, new AtomicInteger(0));
        AtomicInteger requestCount = requestCountsPerIpAddress.get(clientIpAddress);

        int requests = requestCount.incrementAndGet();

        if (requests > MAX_REQUESTS_PER_MINUTE) {
            httpServletResponse.setStatus(429);
            httpServletResponse.getWriter().write("Too many requests. Please try again later.");
            return;
        }

        chain.doFilter(request, response);

        if (requests == 1) {
            scheduler.schedule(() -> reset(clientIpAddress), rateDuration, TimeUnit.MILLISECONDS);
        }
    }

    private void reset(String clientIpAddress) {
        requestCountsPerIpAddress.remove(clientIpAddress);
    }
}