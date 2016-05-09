package org.openntf.conferenceapp.ui;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;

@WebServlet(value = { "/app/*" , "/VAADIN/*" }, asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = ConferenceUI.class)
public class ConferenceUIServlet extends VaadinServlet {

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		
		getService().addSessionInitListener(new SessionInitListener() {
			
			@Override
			public void sessionInit(SessionInitEvent event) throws ServiceException {
				
				event.getSession().addBootstrapListener(new BootstrapListener() {
					
					@Override
					public void modifyBootstrapPage(final BootstrapPageResponse response) {
						
//						String contextPath = response.getRequest().getContextPath();
						
						response.getDocument().head().append(
						"<meta name=\"apple-mobile-web-app-capable\" content=\"YES\">"
						+ "<meta name=\"mobile-web-app-capable\" content=\"yes\">"
						+ "<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">"
						);
						
						response.getDocument().head().append(
								"<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/clipicon.57x57.png\"	sizes=\"57x57\" rel=\"apple-touch-icon\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/SplashScreen.p.320x460.png\" media=\"(device-width: 320px) and (device-height: 480px)	and (-webkit-device-pixel-ratio: 1)\" rel=\"apple-touch-startup-image\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/clipicon.114x114.png\" sizes=\"114x114\" rel=\"apple-touch-icon\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/SplashScreen.p.640x920.png\" media=\"(device-width: 320px) and (device-height: 480px) and (-webkit-device-pixel-ratio: 2)\" rel=\"apple-touch-startup-image\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/SplashScreen.p.640x1096.png\" media=\"(device-width: 320px) and (device-height: 568px) and (-webkit-device-pixel-ratio: 2)\"	rel=\"apple-touch-startup-image\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/clipicon.72x72.png\" sizes=\"72x72\" rel=\"apple-touch-icon\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/SplashScreen.p.768x1004.png\" media=\"(device-width: 768px) and (device-height: 1024px)	and (orientation: portrait)	and (-webkit-device-pixel-ratio: 1)\"	rel=\"apple-touch-startup-image\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/SplashScreen.l.748x1024.png\" media=\"(device-width: 768px) and (device-height: 1024px)	and (orientation: landscape) and (-webkit-device-pixel-ratio: 1)\"	rel=\"apple-touch-startup-image\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/clipicon.144x144.png\"	sizes=\"144x144\"	rel=\"apple-touch-icon\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/SplashScreen.p.1536x2008.png\"	media=\"(device-width: 768px) and (device-height: 1024px) and (orientation: portrait) and (-webkit-device-pixel-ratio: 2)\"	rel=\"apple-touch-startup-image\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/SplashScreen.l.1496x2048.png\"	media=\"(device-width: 768px) and (device-height: 1024px) and (orientation: landscape)	and (-webkit-device-pixel-ratio: 2)\" rel=\"apple-touch-startup-image\">"
								+ "<!-- iPhone6 and 6 plus -->"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/clipicon.180x180.png\"	sizes=\"120x120\" rel=\"apple-touch-icon\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/SplashScreen.p.750x1294.png\" media=\"(device-width: 375px) and (device-height: 667px) and (orientation: portrait) and (-webkit-device-pixel-ratio: 2)\" rel=\"apple-touch-startup-image\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/SplashScreen.p.1242x2148.png\" media=\"(device-width: 414px) and (device-height: 736px) and (orientation: portrait) and (-webkit-device-pixel-ratio: 3)\" rel=\"apple-touch-startup-image\">"
								+ "<link href=\"http://www.engage.ug/dragon/dragonengage2015.nsf/images/SplashScreen.l.1182x2208.png\"	media=\"(device-width: 414px) and (device-height: 736px) and (orientation: landscape) and (-webkit-device-pixel-ratio: 3)\" rel=\"apple-touch-startup-image\">"
								);
						
					}
					
					@Override
					public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
						// TODO Auto-generated method stub
						
					}
				});
				
			}
		});
	}

}
