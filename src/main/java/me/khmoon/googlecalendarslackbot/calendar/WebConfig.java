package me.khmoon.googlecalendarslackbot.calendar;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Set;

@Configuration
public class WebConfig {

  private static final String APPLICATION_NAME = "Reservation By H3";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final Set<String> SCOPES = CalendarScopes.all();

  @Value("${credential.filepath}")
  private String credentialsFilePath;

  private static HttpRequestInitializer setHttpTimeout(
          final HttpRequestInitializer requestInitializer) {
    return new HttpRequestInitializer() {
      @Override
      public void initialize(HttpRequest httpRequest) throws IOException {
        requestInitializer.initialize(httpRequest);
        // This allows the API to call (and avoid timing out on)
        // functions that take up to 6 minutes to complete (the maximum
        // allowed script run time), plus a little overhead.
        httpRequest.setReadTimeout(380000);
      }
    };
  }

  @Bean
  public Calendar calendar() throws GeneralSecurityException, IOException {
    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    GoogleCredentials credentials = getCredentials();
    HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
    return new Calendar.Builder(httpTransport, JSON_FACTORY, setHttpTimeout(requestInitializer))
            .setApplicationName(APPLICATION_NAME)
            .build();
  }

  private GoogleCredentials getCredentials() throws IOException {
    return GoogleCredentials.fromStream(new ClassPathResource("credentials.json").getInputStream()).createScoped(SCOPES);
  }

}
