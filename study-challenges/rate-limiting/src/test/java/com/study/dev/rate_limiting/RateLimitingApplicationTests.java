package com.study.dev.rate_limiting;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateLimitingFilterTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain chain;

	@InjectMocks
	private RateLimitingFilter rateLimitingFilter;

	@Mock
	private ScheduledExecutorService scheduler;

	private AutoCloseable closeable;

	@BeforeEach
	void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		ReflectionTestUtils.setField(rateLimitingFilter, "MAX_REQUESTS_PER_MINUTE", 2);
		ReflectionTestUtils.setField(rateLimitingFilter, "rateDuration", 1000L);
		ReflectionTestUtils.setField(rateLimitingFilter,"scheduler",scheduler);
	}

	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	void doFilter_firstRequest_shouldAllowRequest() throws IOException, ServletException {
		// given
		// when
		when(request.getRemoteAddr()).thenReturn("127.0.0.1");

		rateLimitingFilter.doFilter(request, response, chain);

		// then
		verify(chain, times(1)).doFilter(request, response);
		verify(scheduler).schedule(any(Runnable.class), eq(1000L), eq(TimeUnit.MILLISECONDS));
	}

	@Test
	void doFilter_multipleRequestsWithinLimit_shouldAllowRequests() throws IOException, ServletException {
		// given
		// when
		when(request.getRemoteAddr()).thenReturn("127.0.0.1");

		rateLimitingFilter.doFilter(request, response, chain);
		rateLimitingFilter.doFilter(request, response, chain);

		// then
		verify(chain, times(2)).doFilter(request, response);
		verify(scheduler).schedule(any(Runnable.class), eq(1000L), eq(TimeUnit.MILLISECONDS));
	}

	@Test
	void doFilter_exceedRateLimit_shouldReturn429() throws IOException, ServletException {
		// given
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);

		// when
		when(request.getRemoteAddr()).thenReturn("127.0.0.1");
		when(response.getWriter()).thenReturn(writer);

		rateLimitingFilter.doFilter(request, response, chain);
		rateLimitingFilter.doFilter(request, response, chain);
		rateLimitingFilter.doFilter(request, response, chain);

		// then
		verify(response).setStatus(429);
		verify(chain, times(2)).doFilter(request, response);
		verify(scheduler).schedule(any(Runnable.class), eq(1000L), eq(TimeUnit.MILLISECONDS));
	}

	@Test
	void doFilter_differentIpAddresses_shouldNotInterfere() throws IOException, ServletException {
		// given

		// when
		when(request.getRemoteAddr()).thenReturn("127.0.0.1", "127.0.0.2");

		rateLimitingFilter.doFilter(request, response, chain);
		rateLimitingFilter.doFilter(request, response, chain);

		// then
		verify(chain, times(2)).doFilter(request, response);
		verify(scheduler,times(2)).schedule(any(Runnable.class), eq(1000L), eq(TimeUnit.MILLISECONDS));
	}

	@Test
	void reset_shouldRemoveIpAddressFromMap() throws IOException, ServletException {
		// given

		// when
		when(request.getRemoteAddr()).thenReturn("127.0.0.1");
		rateLimitingFilter.doFilter(request, response, chain);

		Runnable resetTask = mock(Runnable.class);
		// then
		verify(scheduler).schedule(any(Runnable.class), eq(1000L), eq(TimeUnit.MILLISECONDS));

		org.mockito.ArgumentCaptor<Runnable> taskCaptor = org.mockito.ArgumentCaptor.forClass(Runnable.class);
		verify(scheduler).schedule(taskCaptor.capture(), anyLong(), any(TimeUnit.class));
		taskCaptor.getValue().run();


		rateLimitingFilter.doFilter(request, response, chain);
		verify(chain, times(2)).doFilter(request, response);
	}

	@Test
	public void doFilter_requestAfterReset_shouldAllowRequest() throws IOException, ServletException, InterruptedException {
		// given
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);

		// when
		when(request.getRemoteAddr()).thenReturn("127.0.0.1");
		when(response.getWriter()).thenReturn(writer);

		rateLimitingFilter.doFilter(request, response, chain);
		rateLimitingFilter.doFilter(request, response, chain);
		rateLimitingFilter.doFilter(request, response, chain);

		// then
		verify(response).setStatus(429);
		verify(chain, times(2)).doFilter(request, response);

		org.mockito.ArgumentCaptor<Runnable> taskCaptor = org.mockito.ArgumentCaptor.forClass(Runnable.class);
		verify(scheduler).schedule(taskCaptor.capture(), anyLong(), any(TimeUnit.class));
		taskCaptor.getValue().run();

		rateLimitingFilter.doFilter(request, response, chain);
		verify(chain, times(3)).doFilter(request,response);
	}

}