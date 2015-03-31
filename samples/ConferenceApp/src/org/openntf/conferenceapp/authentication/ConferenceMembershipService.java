package org.openntf.conferenceapp.authentication;

import java.util.logging.Logger;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.openntf.conference.graph.Attendee;
import org.openntf.conferenceapp.service.AttendeeFactory;

import com.factor_y.util.crypto.DesEncrypter;
import com.factor_y.util.crypto.DesEncrypter.DesEncrypterException;

public class ConferenceMembershipService {

	static DesEncrypter crypter = null;
	
	static {
		try {
			crypter = new DesEncrypter(1L, "passowrd");
		} catch (DesEncrypterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	Logger log = Logger.getLogger(ConferenceMembershipService.class.getName());
	
	public static String generateAccessToken(String email) {
		
		String result = null;
		
		try {
			result = crypter.encrypt(email);
		} catch (DesEncrypterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return email;
	}
	
	/**
	 * @param accessToken
	 * @return the Attendee Key form an access token
	 */
	public static String getAttendeeEmailFromAccesTokenAccessToken(String accessToken) {
		String result = null;
		
//		try {
//			result = crypter.decrypt(accessToken);
//		} catch (DesEncrypterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return accessToken;
	}
	
	public static void sendInvitationEmail(String emailAddress, String token) {

		System.out.println("Sending invitation to: " + emailAddress + " url: http://engageapp.factor-y.com/ConferenceApp/app?accesstoken=" + token);
		
		try {
			Email email = new SimpleEmail();
			email.setHostName("localhost");
			email.setSmtpPort(25);
			email.setFrom("engage2015@factor-y.com","Enage.ug 2015");
			email.setSubject("Welcome to the conference app - ACTION REQUIRED [ODA Session]");
			email.setMsg("Use this link to get access to the app and to setup your profile\n\n"
					+ "http://engageapp.factor-y.com/ConferenceApp/app?accesstoken=" + token);
			email.addTo(emailAddress);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	public static Attendee findUserProfileByEmail(String email) {
		return AttendeeFactory.getAttendeeByEmail(email);
	}
	
}
