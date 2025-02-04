package com.sart.HomieFix;

import com.sart.HomieFix.Configuration.OtpConfiguration;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomieFixApplication {

	@Autowired
	private OtpConfiguration otpConfiguration;

	@PostConstruct
	public void setup() {
		Twilio.init(otpConfiguration.getAccountSid(), otpConfiguration.getAuthToken());
	}
	public static void main(String[] args) {
		SpringApplication.run(HomieFixApplication.class, args);
	}

}
