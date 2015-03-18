package org.openntf.conferenceapp.ui;

import java.util.Date;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("valo")
public class ConferenceUI extends UI {

	@Override
	protected void init(VaadinRequest request) {

		getPage().setTitle("[OpenNTF] Conferences");
		
		VerticalLayout vert = new VerticalLayout();
		
		Table sessionTable = new Table();
		
		sessionTable.addContainerProperty("Date", Date.class, null);
		sessionTable.addContainerProperty("Session", String.class, null);
		sessionTable.addContainerProperty("Speaker", String.class, null);
		
		sessionTable.addItem(new Object[] { new Date(), "dev","Daniele Vistalli"},  "DV Session");
		sessionTable.addItem(new Object[] { new Date(), "dev","Paul Withers"},  "PW Session");

		sessionTable.setSizeFull();
		sessionTable.addItemClickListener(new ItemClickListener() {
			
			public void itemClick(ItemClickEvent event) {
				
				System.out.println(event.getItemId() + " clicked");
				
			}
		});

		vert.addComponent(sessionTable);
		vert.setSizeFull();

		setContent(vert);
		
	}

}
