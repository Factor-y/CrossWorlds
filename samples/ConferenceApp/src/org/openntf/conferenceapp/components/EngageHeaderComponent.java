package org.openntf.conferenceapp.components;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;


public class EngageHeaderComponent extends CustomComponent  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4465325630005163198L;
	Label banner = null;
	
	public EngageHeaderComponent() {
		setHeight("90px");
		
		banner = new Label("<div style=\"background-color: white\" ><img src=\"http://www.engage.ug/engage.nsf/Pages/Logo/$file/Logo_Trans.png\" height=\"90\" ></div>");
		banner.setCaptionAsHtml(true);
		banner.setContentMode(ContentMode.HTML);
		
		setCompositionRoot(banner);
	}
	
}
