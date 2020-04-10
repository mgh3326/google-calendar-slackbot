package me.khmoon.googlecalendarslackbot.slack.filter;

import lombok.extern.slf4j.Slf4j;
import me.khmoon.googlecalendarslackbot.slack.service.VerificationService;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;

@Component
@Slf4j
public class VerificationFilter implements Filter {

    private static final String SIGNING_SECRET = System.getenv("SIGNING_SECRET");

    private final VerificationService verificationService;
    private FilterConfig filterConfig;

    public VerificationFilter() throws NoSuchAlgorithmException, InvalidKeyException {
        this.verificationService = new VerificationService(Clock.systemUTC(), SIGNING_SECRET);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        CachedRequestWrapper wrapper = new CachedRequestWrapper((HttpServletRequest) req);
        String timestamp = wrapper.getHeader("X-Slack-Request-Timestamp");
        String requestBody = wrapper.getBody();
        String signature = wrapper.getHeader("X-Slack-Signature");
        log.debug("Request Body: {}", wrapper.getBody());
        if (verificationService.verify(timestamp, requestBody, signature)) {
            filterChain.doFilter(wrapper, res);
        } else {
            ((HttpServletResponse) res).sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
