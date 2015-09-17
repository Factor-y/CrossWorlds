package org.openntf.conferenceapp.components;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class IconHeaderComponent extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4465325630005163198L;
	Label banner = null;

	public IconHeaderComponent() {
		setHeight("90px");

		banner = new Label(
				"<div style=\"background-color: white; height:90px\" ><img src=\"http://iconuk.org/iconuk/iconuk2014.nsf/0/595A4D983C7601DA80257CCB007D0A13/$FILE/ICONUK Roundel DKGray.png\" height=\"90\" ></div>");
		banner.setCaptionAsHtml(true);
		banner.setContentMode(ContentMode.HTML);

		setCompositionRoot(banner);
	}

}
