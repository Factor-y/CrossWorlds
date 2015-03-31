package org.openntf.conferenceapp.ui.pages;

import java.util.Date;
import java.util.List;

import javax.swing.event.DocumentEvent.EventType;

import org.openntf.conference.graph.Presentation;
import org.openntf.conference.graph.TimeSlot;
import org.openntf.conferenceapp.service.EventFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.components.calendar.event.CalendarEvent;

public class CalendarView extends VerticalLayout implements View {

	public static final String VIEW_NAME = "CalendarView";
	public static final String VIEW_DESC = "On calendar";

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	
	public CalendarView() {
		
		Calendar cal = new Calendar();

		cal.setStartDate(new Date(115,2,30));
		cal.setEndDate(new Date(115,2,31));
		
		cal.setFirstVisibleHourOfDay(8);
		cal.setLastVisibleHourOfDay(20);
		cal.setReadOnly(true);
		
		List<Presentation> presentationsList = EventFactory.getPresentationsSortedByProperty("");
		
		for (Presentation presentation : presentationsList) {
			
			final Presentation presF = presentation;
			Iterable<TimeSlot> slots = presentation.getTimes();
			
			for (TimeSlot timeSlot : slots) {
				
				final TimeSlot slotF = timeSlot;
				cal.addEvent(new CalendarEvent() {

					@Override
					public Date getStart() {
						return slotF.getStartTime().getTime();
					}

					@Override
					public Date getEnd() {
						return slotF.getEndTime().getTime();
					}

					@Override
					public String getCaption() {
						// TODO Auto-generated method stub
						return presF.getTitle();
					}

					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return presF.getDescription();
					}

					@Override
					public String getStyleName() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public boolean isAllDay() {
						// TODO Auto-generated method stub
						return false;
					}
					
				});
			}
		}
		
		cal.setSizeFull();
		
		addComponent(cal);
		setSizeFull();
	}

}
