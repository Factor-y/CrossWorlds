package org.openntf.conferenceapp.components;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

public class TwitterTimelineComponent extends CustomComponent {

	private String twitterId = null;
	private Label htmlLabel = null;
	
	public TwitterTimelineComponent(String twitterId) {
		
		this.twitterId = twitterId;
		
		htmlLabel = new Label();

		htmlLabel.setContentMode(ContentMode.HTML);
		htmlLabel.setCaptionAsHtml(true);
		
		setCompositionRoot(htmlLabel);
		
		updateUI();
		
	}
	
	private void updateUI() {

		if (twitterId != null) {
			
			htmlLabel.setContentMode(ContentMode.HTML);
			htmlLabel.setCaptionAsHtml(true);
			
			StringBuilder sb = new StringBuilder();
//			sb.append("<h1>Hello Twitter</h1>");
			sb.append("<a class=\"twitter-timeline\" href=\"https://twitter.com/" + twitterId + "\" data-widget-id=\"582892772664934400\">Tweet di @" + twitterId + "</a>"
					+ "<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+\"://platform.twitter.com/widgets.js\";fjs.parentNode.insertBefore(js,fjs);}}(document,\"script\",\"twitter-wjs\");</script>");
			
			htmlLabel.setCaption(sb.toString());
			htmlLabel.setVisible(true);
			
			System.out.println("Twitter is enabled");
			
		} else {
			htmlLabel.setCaption("");
			htmlLabel.setVisible(false);
		}
		
	}

	public String getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
		updateUI();
	}
	
	
	
}
