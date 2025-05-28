package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Value("${spring.application.name}")
  public String applicationName;

  @Value("${url.coin-desk-api}")
  public String coinDeskApiUrl;

  @Value("${server.useMock}")
  public boolean useMock;

}
