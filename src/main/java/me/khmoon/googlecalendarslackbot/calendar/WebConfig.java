package me.khmoon.googlecalendarslackbot.calendar;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
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

  @Bean
  public Calendar calendar() throws GeneralSecurityException, IOException {
    final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    return new Calendar.Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(getCredentials()))
            .setApplicationName(APPLICATION_NAME)
            .build();
  }

  private GoogleCredentials getCredentials() throws IOException {
    return GoogleCredentials.fromStream(new ClassPathResource("credentials.json").getInputStream()).createScoped(SCOPES);
  }
}
