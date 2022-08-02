package com.imooc.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
@PropertySource(
		ignoreResourceNotFound = true, 
		value = "classpath:twilio.properties"
		)
public class SMSUtils {
	@Value( "${twilio.sid}" )
	private String twilioSid;
	
	@Value( "${twilio.auth.token}" )
	private String twilioAuthToken;
	
	@Value( "${twilio.number}" )
	private String twilioNumber;
	
	public void sendSMS(String phone, String code) {
		Twilio.init(twilioSid, twilioAuthToken);
        Message.creator(new PhoneNumber(phone),
                        new PhoneNumber(twilioNumber), 
                        "Verification code: " + code
                        ).create();
	}
	

}
