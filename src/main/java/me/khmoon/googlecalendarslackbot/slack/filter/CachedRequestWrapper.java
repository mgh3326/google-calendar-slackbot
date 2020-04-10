package me.khmoon.googlecalendarslackbot.slack.filter;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CachedRequestWrapper extends HttpServletRequestWrapper {
    private final String body;
    private final Map<String, String[]> parameters;

    public CachedRequestWrapper(HttpServletRequest req) throws IOException {
        super(req);
        this.body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        this.parameters = parseParameters();
    }

    private Map<String, String[]> parseParameters() throws UnsupportedEncodingException {
        if (getMethod().equalsIgnoreCase(HttpMethod.GET.name())) {
            Map<String, String[]> parameters = new HashMap<>();
            Enumeration<String> parameterNames = super.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                parameters.put(parameterName, super.getParameterValues(parameterName));
            }
            return parameters;
        }
        if (getContentType().equalsIgnoreCase(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            String decodedBody = URLDecoder.decode(body, "UTF-8");
            return Stream.of((body.equals(decodedBody) ? body.split("&") : decodedBody.split("&")))
                    .map(s -> s.split("=", 2))
                    .collect(Collectors.groupingBy(
                            s -> s[0],
                            Collectors.mapping(
                                    s -> s[1],
                                    Collectors.collectingAndThen(Collectors.toList(), l -> l.toArray(new String[0])))
                            )
                    );
        }
        return new HashMap<>();
    }

    public String getBody() {
        return body;
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedServletInputStream(new ByteArrayInputStream(body.getBytes()));
    }

    @Override
    public String getParameter(String name) {
        return Optional.ofNullable(parameters.get(name)).map(x -> x[0]).orElse(null);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameters;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new Enumeration<String>() {
            private final Iterator<String> names = parameters.keySet().iterator();

            @Override
            public boolean hasMoreElements() {
                return names.hasNext();
            }

            @Override
            public String nextElement() {
                return names.next();
            }
        };
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameters.get(name);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}