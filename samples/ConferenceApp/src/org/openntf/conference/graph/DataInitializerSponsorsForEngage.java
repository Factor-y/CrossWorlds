package org.openntf.conference.graph;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.openntf.conference.graph.Sponsor.Level;
import org.openntf.domino.Session;
import org.openntf.domino.graph2.builtin.DVertexFrameComparator;
import org.openntf.domino.graph2.impl.DGraph;
import org.openntf.domino.junit.TestRunnerUtil;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.FramedTransactionalGraph;

public class DataInitializerSponsorsForEngage implements Runnable {
	private long marktime;
	private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public DataInitializerSponsorsForEngage() {

	}

	@Override
	public void run() {
		long testStartTime = System.nanoTime();
		marktime = System.nanoTime();
		try {
			timelog("Beginning dataInitializer...");

			// Get / create databases
			Session s = Factory.getSession(SessionType.NATIVE);

			// Initialize the graph
			ConferenceGraph graph = new ConferenceGraph();
			FramedTransactionalGraph<DGraph> framedGraph = graph.getFramedGraph();

			loadData(s, graph);

			Iterable<Group> groups = framedGraph.getVertices(null, null, Group.class);

			System.out.println("**Getting organisations**");
			for (Group group : groups) {
				System.out.println(group.getName());
			}
			Iterable<Sponsor> orgs = framedGraph.getVertices(null, null, Sponsor.class);
			System.out.println("Total sponsors: " + Lists.newArrayList(orgs).size());
			Ordering ord = Ordering.from(new DVertexFrameComparator("Level"));
			List<Sponsor> sponsors = ord.sortedCopy(orgs);

			System.out.println("**Getting sponsors**");
			for (Sponsor org : sponsors) {
				System.out.println(org.getLevel().ordinal() + ": " + org.getName());
			}

			List<Group> groupsList = Lists.newArrayList(groups);
			groupsList.addAll(Lists.newArrayList(orgs));
			System.out.println("**Getting organisations and sponsors**");
			for (Group group : groupsList) {
				System.out.println(group.getName());
			}

			Attendee att = graph.getAttendee("Laurent Boes");
			if (null == att) {
				System.out.println("Can't find Laurent");
			} else {
				System.out.println("Found Laurent");
				List<Group> grps = Lists.newArrayList(att.getMemberOfGroups());
				System.out.println("Laurent member of group numbers: " + grps.size());
				for (Group group : grps) {
					System.out.println(group.getName());
					System.out.println(group.hashCode());
				}
				List<Event> presentations = Lists.newArrayList(att.getPresentingEvents());
				System.out.println("Presenting events: " + presentations.size());
				for (Event evt : presentations) {
					System.out.println(evt.getTitle());
				}

				Map<CharSequence, Object> props = att.asMap();
				System.out.println("Outputting properties for Laurent Boes");
				for (CharSequence key : props.keySet()) {
					System.out.println(key + " - " + props.get(key));
				}
			}

			Iterable<Event> evts = framedGraph.getVertices(null, null, Event.class);

		} catch (Throwable t) {
			t.printStackTrace();
		}
		long testEndTime = System.nanoTime();
		System.out.println("Completed " + getClass().getSimpleName() + " run in " + ((testEndTime - testStartTime) / 1000000) + " ms");
	}

	public void loadData(final org.openntf.domino.Session s, final ConferenceGraph graph) {
		try {

			// IBM Benelux
			FramedTransactionalGraph<DGraph> framedGraph = graph.getFramedGraph();
			Sponsor org = createSponsor(
					framedGraph,
					"IBM Benelux",
					Level.STRATEGIC,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/IBM.gif",
					"http://www.ibm.com/social-business/us/en/",
					"Businesses move from liking to leading when they look beyond social media to see how social technologies drive real business value. From marketing and sales to product and service innovation, social is changing the way people connect and the way organizations succeed. Social business technologies help people connect, communicate and share information. Becoming a leader in your marketplace means using social solutions to transform how business gets done—driving cost savings, increasing revenue and cultivating competitive advantages. IBM is at the forefront of this market shift and your best partner to get social and do business");
			addAttendee(graph, framedGraph, "Laurent", "Boes", "laurent_boes@be.ibm.com", org);

			Sponsor org2 = createSponsor(
					framedGraph,
					"BCC",
					Level.SILVER,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/BCC.gif",
					"http://www.bcc.biz",
					"BCC work with companies to secure their social business infrastructure, automate underlying administration processes and ensure regulatory compliance.");
			addAttendee(graph, framedGraph, "Svenja", "Stolz", "svenja_stolzenbach@bcc.biz", org2);

			Sponsor org3 = createSponsor(
					framedGraph,
					"Crossware",
					Level.SILVER,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Crossware.gif",
					"http://www.crossware.co.nz",
					"Crossware Mail Signature gives you the ability to manage enterprise-wide email signatures. It is a server-based application that automatically adds compliant, personalised and attractive signatures to all emails, including those sent from mobile devices! Visit our booth to find out more and to be in to win a great prize!");
			addAttendee(graph, framedGraph, "Henry", "McIntosh", "henry@crossware.co.nz", org3);

			Sponsor org4 = createSponsor(
					framedGraph,
					"Cube Soft Consulting Ltd",
					Level.BRONZE,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/CubeSoft.gif",
					"http://www.cube-soft.co.uk",
					"Cube Soft have over 15 years experience in working with Web, WebSphere, E-Commerce and Social Business based technologies and it is something we are passionate about. Our clients and customers are spread across the globe and based in the UK, USA, Canada and across Europe. We are an IBM™ Business partner specialising in IBM Connections™ , IBM WebSphere™ and other IBM Collaboration solutions – Our services include consultancy, installation, administration and training for the enterprise.");
			addAttendee(graph, framedGraph, "Sharon", "Bellamy", "sharon@cube-soft.co.uk", org4);

			Sponsor org5 = createSponsor(
					framedGraph,
					"EASI",
					Level.SILVER,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Easi.gif",
					"http://www.easi.net",
					"EASI is editor of business software and apps. We realize software and mobile app development projects and provide IT infrastructure solutions and services both on premise and in the Cloud.");
			addAttendee(graph, framedGraph, "Herman", "Clicq", "h.clicq@easi.net", org5);

			Sponsor org6 = createSponsor(
					framedGraph,
					"goodmeeting.biz",
					Level.PLATINUM,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/GoodMeeting.gif",
					"http://goodmeeting.biz",
					"goodmeeting - The brilliant meeting software! (powered by WebGate Consulting AG) Meetings are named as top time killers. But meetings could be collaboration at it's best! Check out goodmeeting.biz");
			addAttendee(graph, framedGraph, "Roman", "Weber", "roman.weber@webgate.biz", org6);

			Sponsor org7 = createSponsor(
					framedGraph,
					"Groupwave",
					Level.SILVER,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Groupwave.gif",
					"http://www.groupwave.be",
					"Groupwave is specialized in ‘collaboration through communication’. The Lotus Portfolio is all about working together faster, better, stronger. Groupwave wants to take this one step further! We can integrate the provided Lotus solutions with VoIP thanks to our UC² knowledge. This is communication on another level! Mail, instant messaging, fax … are already widespread in the business. Telephony and VoIP are used on a daily basis. Integrating these two worlds is not the future, it is the present. Your communications will be more efficient and you will save time, which means money. Groupwave is also known for all its custom made applications. Not only websites, intranets, but also timesheet applications and … we build what customers need! Mobile apps and Xpages development have no secrets for us! Come over and speak to us so that we can guide you in defining the future of your IT environment.");
			addAttendee(graph, framedGraph, "Kris", "De Bisschop", "kris.de.bisschop@groupwave.be", org7);

			Sponsor org8 = createSponsor(
					framedGraph,
					"HADSL",
					Level.SILVER,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Firm.gif",
					"http://hadsl.com",
					"Fed up with resetting passwords? Bored with setting up users for your HR department? Want to enrich your SPML id management system? Need ITIL or SOX compliance? HADSL FirM has been giving control to Domino Administrators for nearly 10 years. Using FirM to delegate creation, control and deletion of Domino User accounts and groups to the right people reduces mistakes and frees up your administrators’ time. FirM can also automatically manage your BlackBerry devices and links to Active Directory for seamless and secure user id creation and directory data synchronisation across your environments.");
			addAttendee(graph, framedGraph, "Richard", "Sampson", "richard.sampson@hadsl.com", org8);

			Sponsor org9 = createSponsor(
					framedGraph,
					"hedersoft GmbH",
					Level.BRONZE,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Hedersoft.gif",
					"http://www.hedersoft.de",
					"hedersoft is specialized on web and mobile development both inside and outside the IBM universe. Our products: - hedersoft Crawler: Easily search data sources for updates and post them either to an activity stream or create an activity in IBM Connections - hedersoft QLite: Leightweight document management system with a web interface, Domino sidebar plugin and webdav interface - hedersoft Workspace: Centrally managed web app and link desktop for users to get to their databases and applications from a browser interface - hedersoft BPM: Activiti BPM (activiti.org) connector to easily worklfow enable IBM Domino databases without any design changes");
			addAttendee(graph, framedGraph, "Henning", "Schmidt", "hschmidt@hedersoft.de", org9);

			Sponsor org10 = createSponsor(framedGraph, "ICON UK", Level.BRONZE, "http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/IconUK.gif",
					"http://iconuk.org", "ICON UK - The UK ICS User Group running");
			addAttendee(graph, framedGraph, "Tim", "Clark", "iconuk@tc-soft.com", org10);

			Sponsor org11 = createSponsor(framedGraph, "iVision Software", Level.BRONZE,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/iVision.gif", "http://www.ivissoft.com", "");
			addAttendee(graph, framedGraph, "Mayank", "Singh", "msingh@ivissoft.com", org11);

			Sponsor org12 = createSponsor(
					framedGraph,
					"Kudos",
					Level.GOLD,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Kudos.gif",
					"http://www.kudosbadges.com",
					"Kudos rewards user adoption, measures performance and increases productivity for IBM Connections. Adding value to organisations in over 30 countries around the world, Kudos is developed by leading Australian collaboration experts and IBM Premier Business Partner; ISW.");
			addAttendee(graph, framedGraph, "Adam", "Brown", "abrown@isw.net.au", org12);

			Sponsor org13 = createSponsor(
					framedGraph,
					"LDC Via",
					Level.GOLD,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/LDC-Via.gif",
					"http://ldcvia.com",
					"Do you need to retire your Domino infrastructure but keep the data? Perhaps you want to extend your Domino data to new horizons, or ease the burden on your Domino servers? Or to explore the new wave of web technologies whilst maintaining your investment in IBM Notes & Domino? LDC Via can help: free your Domino data, making use of a scalable, modern document store with a rich REST API and a variety of template web applications.");
			addAttendee(graph, framedGraph, "Matt", "White", "matt@ldcvia.com", org13);

			Sponsor org14 = createSponsor(
					framedGraph,
					"midpoints GmbH",
					Level.BRONZE,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/MidPoints.gif",
					"http://www.midpoints.de",
					"midpoints specializes in Enterprise Mobility Consulting and development of mobility solutions based on IBM collaboration software. We're offering solutions for integrating mobile devices, applications and content in a secure and efficient way into and from your internal infrastructure. Our solutions - midpoints mobile.profiler: delivers scalable MDM and MAM based on IBM software. - midpoints traveler.rules: enriches IBM Notes Traveler with important enterprise functions. - midpoints doc.Store: realizes the secure delivery of of documents for mobile devices based on existing IBM infrastructure.");
			addAttendee(graph, framedGraph, "Michael", "Ingendoh", "michael.ingendoh@midpoints.de", org14);

			Sponsor org15 = createSponsor(
					framedGraph,
					"OnTime",
					Level.SILVER,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/OnTime.gif",
					"http://ontimesuite.com",
					"OnTime Group Calendar delivers a real-time overview of the people in your organization by location, availability and business division – anytime, anywhere. Available for IBM Notes, iNotes, Web, IBM Connections and Smart phones.");
			addAttendee(graph, framedGraph, "Jeannie", "Overgaard Thanner", "jeo@intravision.dk", org15);

			Sponsor org16 = createSponsor(
					framedGraph,
					"OpenNTF",
					Level.BRONZE,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/OpenNTF.gif",
					"http://openntf.org",
					"OpenNTF is the open source community for the IBM Collaboration Solutions portfolio. We are the home of the famous ExtensionLibrary, the Social Business Toolkit, the OpenNTF Domino API and the XPages Toolkit");
			addAttendee(graph, framedGraph, "Christian", "Guedemann", "christian.guedemann@webgate.biz", org16);

			Sponsor org17 = createSponsor(framedGraph, "Opus Neo", Level.BRONZE, "http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/OpusNeo.gif",
					"http://www.opusneo.com", "");
			addAttendee(graph, framedGraph, "Jan", "Zeuthen", "jfz@opusneo.dk", org17);

			Sponsor org18 = createSponsor(
					framedGraph,
					"panagenda",
					Level.PLATINUM,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Panagenda.gif",
					"http://www.panagenda.com",
					"Debating between migrating or virtualizing? Unsure if the cloud or consolidating is right for you?  Getting serious about upgrades or modernizing and mobilizing apps?  As leaders in optimizing collaboration landscapes, panagenda helps companies around the world minimize cost, complexity and workload and maximize agility. panagenda solutions are the key to making your strategic decisions a success - from analytics to execution, all across clients, servers and applications, email and social business. With over 7 million licenses in 70 countries, panagenda is your single point of contact for optimizing communication and collaboration!");
			addAttendee(graph, framedGraph, "Florian", "Vogler", "florian.vogler@panagenda.com", org18);

			Sponsor org19 = createSponsor(
					framedGraph,
					"Pipalia LTD",
					Level.BRONZE,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Pipalia.gif",
					"http://www.pipalia.co.uk",
					"Pipalia LTD is a company based in London specialising in IBM Notes/Domino and XPages development. We have over 10 years of experience developing in Notes/Domino (version 5 - 9+) and Sage Accounting products. We have recently updated our bespoke end to end system (including accounting integration, back-end order and invocie management, supplier management) for fastkeys.co.uk using XPages, JSF, Bootstrap and Notes Client. We are professional Sage developers and have done projects for many organisations including PwC US and McDonalds.");
			addAttendee(graph, framedGraph, "Samir", "Pipalia", "samir@pipalia.co.uk", org19);

			Sponsor org20 = createSponsor(
					framedGraph,
					"PIXELIXIR",
					Level.GOLD,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Pixelixir.gif",
					"http://www.pixelixir.com",
					"Known for his expertise since 15 years as a renowned technology advisor and leading system integrator, PIXELIXIR with his team of ICT innovators & certified specialists work closely with a lot of companies of all sizes to provide innovative, efficient solutions, and deliver measurable business added-value in the field of Unified Messaging, Social Business, Shared Environnements, Security, Cloud, eCommerce, Mobile Apps, Digital Marketing ...");
			addAttendee(graph, framedGraph, "Thibault", "Anderlin", "ta@pixelixir.com", org20);

			Sponsor org21 = createSponsor(
					framedGraph,
					"Red Pill Development",
					Level.SILVER,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Redpill.gif",
					"http://redpilldevelopment.com",
					"When it comes to the future of your IBM Notes applications, you have a choice. You can take the blue pill and accept the status quo. Or, take the Red Pill and see your Notes applications come to life on your desktop and mobile devices, in a fully responsive, CSS3- and HTML5-compliant design. We get you there in less time than you think, at a price that makes sense, and without the risks of migration and retooling. What’s more, choosing the Red Pill means an immediate, measurable benefit for every user across your enterprise, and an ongoing process for progressive modernization of all your apps. Want to learn more about asymmetric modernization services for your portfolio of Notes applications before we meet at the conference? Visit www.redpilldevelopment.com, or contact us at info@redpilldevelopment.com.");
			addAttendee(graph, framedGraph, "Peter", "Presnell", "peter@redpilldevelopment.com", org21);

			Sponsor org22 = createSponsor(
					framedGraph,
					"Silverside",
					Level.SILVER,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Silverside.gif",
					"",
					"Take responsibility and speak up, that’s what we do as well. We are convinced that the biggest challenge for the next 5 years is the adoption of software in our market. By taking responsibility for the adoption of Microsoft SharePoint and IBM Software we will be the best valued partner in our market. The consultants of Silverside build bridges between business and IT and work focussed on improving efficiency, productivity and ROI with customers. We strive for an optimal mix of collaboration of people, business goals and technological best of breed platforms.");
			addAttendee(graph, framedGraph, "Roland", "Driesen", "r.driesen@silverside.nl", org22);

			Sponsor org23 = createSponsor(framedGraph, "Social Connections", Level.BRONZE,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/SocialConnections.gif", "http://socialconnections.info",
					"April 16-17, 2015 IBM Innovation Centre - 1 Rogers Street - Cambridge, MA 02142");
			addAttendee(graph, framedGraph, "Stuart", "McIntyre", "info@socialconnections.info", org23);

			Sponsor org24 = createSponsor(
					framedGraph,
					"Teamstudio, Inc.",
					Level.SILVER,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Teamstudio.gif",
					"http://teamstudio.com",
					"Whether you want to build a mobile app yourself or have us build one for you, Teamstudio enables your workforce to be productive anytime, anywhere, with uninterrupted offline access to mobile applications. And with innovative tools to manage Notes and Domino, working with Teamstudio is the smartest move you’ll make. Learn more at teamstudio.com");
			addAttendee(graph, framedGraph, "Taline", "Badrikian", "taline_badrikian@teamstudio.com", org24);

			Sponsor org25 = createSponsor(
					framedGraph,
					"TIMETOACT GROUP",
					Level.GOLD,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/TimeToActGroup.gif",
					"http://www.timetoact-group.com",
					"TIMETOACT is a Premier IBM Business Partner with major investment into assets, products and services for IBM Connections. TIMETOACT products for IBM Connections include: - CAT - Connections Administration Toolkit Simplify and Improve IBM Connections Administration and Content Management - UAM - User Access Manager Manage External (Guest) Users for IBM Connections, Internal Users “Terms of Use” Acceptance, Passwords etc. - XCC - Web Content Management & Custom Apps Extension Integrate Internal Communications and Custom Applications into IBM Connections With about about 200 employees, TIMETOACT GROUP is one of the largest IBM Software Services providers in DACH. TIMETOACT GROUP is located in several locations in Germany and Switzerland.");
			addAttendee(graph, framedGraph, "Monika", "Bach", "monika.bach@timetoact.de", org25);

			Sponsor org26 = createSponsor(
					framedGraph,
					"TLCC",
					Level.BRONZE,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/TLCC.gif",
					"http://www.tlcc.com",
					"TLCC is the leading provider of self-paced training courses for Domino developers and administrators including a complete line of XPages courses. Visit tlcc.com to try a FREE course.");
			addAttendee(graph, framedGraph, "Howard", "Greenberg", "howardg@tlcc.com", org26);

			Sponsor org27 = createSponsor(
					framedGraph,
					"Trilog - Soluster",
					Level.SILVER,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Soluster.gif",
					"http://www.project4Connections.com",
					"Project Management Made Social: Planning is not enough! Empower your project teams with powerful social collaboration tools to achieve common goals. Use the Power of IBM Connections: Building upon the IBM Connections platform, ProjExec provides a comprehensive PM solution for leveraging all of your favorite social media platform. A Real Alternative to MS-Project: ProjExec Social Gantt delivers all functions expected by project managers, including an incredible, bi-directional integration with Microsoft Project.");
			addAttendee(graph, framedGraph, "Christophe", "Borlat", "cborlat@soluster.com", org27);

			Sponsor org28 = createSponsor(
					framedGraph,
					"VitalSigns",
					Level.SILVER,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/VitalSigns.gif",
					"http://www.rprvitalsigns.com",
					"VitalSigns helps Customers be more pro-active and agile towards IT Operational Management. Improving the end user experience on the Business side as well as helping Customers to save spending in IT is VitalSigns key mission.");
			addAttendee(graph, framedGraph, "Carwin", "Heierman", "carwin@rprvitalsigns.com", org28);

			Sponsor org29 = createSponsor(
					framedGraph,
					"We4IT",
					Level.GOLD,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/We4IT.gif",
					"http://www.we4it.com",
					"We4IT is an IBM Premier Business Partner and provider of managed services and products relating to IBM Collaboration Solutions (ICS). We help clients by managing or even completely outsourcing their ICS environment and transforming their IBM Notes applications in a web and mobile strategy. To complete our line-up, we offer Software-as-a-Service (SaaS) using either a public or private cloud.");
			addAttendee(graph, framedGraph, "Stefan", "Sucker", "stefan.sucker@we4it.com", org29);

			Sponsor org30 = createSponsor(
					framedGraph,
					"Ytria",
					Level.PLATINUM,
					"http://www.engage.ug/engage.nsf/Pages/Sponsors/$File/Ytria.gif",
					"http://www.ytria.com",
					"Enhance your administration efficiency and save development time on your IBM Notes and Domino platform with EZ Suite tools. Ytria solutions help you find problems before they manifest and fix them in no time. Perform countless tasks, otherwise impossible to achieve, with global-analysis capabilities and mass-modification features. Stop by our booth and see how you can access and modify anything you need to improve productivity, accuracy and response time all at once!");
			addAttendee(graph, framedGraph, "Sonia", "Bounardjian", "sbounardjian@ytria.com", org30);

			framedGraph.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Sponsor createSponsor(FramedTransactionalGraph<DGraph> framedGraph, String name, Level level, String photoUrl, String webUrl,
			String profile) {
		Sponsor org = null;
		Group check = framedGraph.getVertex(name, Group.class);
		if (null != check) {
			Vertex vert = check.asVertex();
			vert.setProperty("form", "Sponsor");
			framedGraph.commit();
			org = framedGraph.getVertex(name, Sponsor.class);
		} else {
			org = framedGraph.addVertex(name, Sponsor.class);
			org.setName(name);
			org.setType(Group.Type.COMPANY);
		}
		org.setLevel(level);
		org.setPhotoUrl(photoUrl);
		org.setUrl(webUrl);
		org.setProfile(profile);
		return org;
	}

	public void addAttendee(ConferenceGraph graph, FramedTransactionalGraph<DGraph> framedGraph, String firstName, String lastName, String email,
			Sponsor org) {
		Attendee att = graph.getAttendee(firstName + " " + lastName);
		if (null == att) {
			att = framedGraph.addVertex(null, Attendee.class);
			att.setFirstName(firstName);
			att.setLastName(lastName);
		}
		att.setEmail(email);
		org.addMember(att);
		org.addContactFor(att);
	}

	public static void main(final String[] args) {
		TestRunnerUtil.runAsDominoThread(new DataInitializerSponsorsForEngage(), TestRunnerUtil.NATIVE_SESSION);
	}

	public void timelog(final String message) {
		long curtime = System.nanoTime();
		long elapsed = curtime - marktime;
		marktime = curtime;
		System.out.println(elapsed / 1000000 + " ms: " + message);
	}

}
