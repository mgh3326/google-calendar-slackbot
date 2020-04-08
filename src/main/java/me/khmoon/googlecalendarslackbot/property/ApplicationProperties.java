package me.khmoon.googlecalendarslackbot.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("my-values")
@Getter
@Setter
public class ApplicationProperties {
  private Google google;
  private Credential credential;

  @Getter
  @Setter
  public static class Google {
    private String calenderId;
  }

  @Getter
  @Setter
  public static class Credential {
    private String filepath;
  }
}
