package com.imooc.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.imooc.exceptions.MyCustomException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
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
		try {
			Twilio.init(twilioSid, twilioAuthToken);
	        Message.creator(
	        		new PhoneNumber(phone),
	                new PhoneNumber(twilioNumber), 
	                "Verification code: " + code
	                ).create();
		} catch (ApiException e) {
			throw new MyCustomException(ResponseStatusEnum.NO_AUTH);
		}
	}
	

}
