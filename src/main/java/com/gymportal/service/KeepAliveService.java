package com.gymportal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KeepAliveService {

    private static final Logger logger = LoggerFactory.getLogger(KeepAliveService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.url}")
    private String appUrl;

    /**
     * Pings the application's health endpoint every 10 minutes (600,000 ms)
     * to keep it awake on Render's free tier.
     */
    @Scheduled(fixedRate = 600000)
    public void pingSelf() {
        try {
            String healthUrl = appUrl + "/actuator/health";
            logger.info("Sending keep-alive ping to: {}", healthUrl);
            String response = restTemplate.getForObject(healthUrl, String.class);
            logger.info("Keep-alive ping successful. Response: {}", response);
        } catch (Exception e) {
            logger.error("Keep-alive ping failed: {}", e.getMessage());
        }
    }
}
