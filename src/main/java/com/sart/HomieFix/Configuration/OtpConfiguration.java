package com.sart.HomieFix.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "twilio")
public class OtpConfiguration {
    private String accountSid;
    private String authToken;
    private String phoneNumber;

    public OtpConfiguration(String accountSid, String authToken, String phoneNumber) {
        super();
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.phoneNumber = phoneNumber;
    }

    public OtpConfiguration() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "OtpConfiguration [accountSid=" + accountSid + ", authToken=" + authToken + ", phoneNumber="
                + phoneNumber + "]";
    }
}