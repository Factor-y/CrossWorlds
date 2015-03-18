package org.openntf.conferenceapp.ui;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;

@WebServlet(value = { "/app/*" , "/VAADIN/*" }, asyncSupported = true)
@VaadinServletConfiguration(productionMode = false, ui = ConferenceUI.class)
public class ConferenceUIServlet extends VaadinServlet {

}
