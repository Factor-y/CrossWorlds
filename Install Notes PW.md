=====================================

Updating ODA
1. Remove from feature project
2. Remove jars from build path in org.xworlds.oda
3. Delete jars
4. Update build.properties
5. Run build.xml
6. Delete source jars
7. Add others to build path
8. In MANIFEST.MF update version number and add jars to bin. Also update Bundle-ClassPath
9. In Project > Properties, add all jars to Order and Export tab
10. Right-click on feature and choose Update Feature to republish to the server

=====================================

https://developer.ibm.com/wasdev/downloads/liberty-profile-using-non-eclipse-environments/
1. Download or start Eclipse Mars IDE for Java EE Developers. Typically, I recommend a separate installation for CrossWorlds (as far any development environment)
2. Install Eclipse plugins (go to Help > Eclipse Marketplace... and search for "Liberty". Install "IBM Websphere Application Server Developer Tools for Mars"
3. Install "Vaadin plugin for Eclipse 2.3.5.20150923"
4. Restart Eclipse

5. Amend Maven Settings in Eclipse to add was-liberty (see screenshots)
6. In Java-EE perspective, on Servers tab, right-click and select New > Server. Select Websphere Application Server Liberty. Click Next. Change name to "Websphere Application Server Liberty Profile", select "Install from an archive or a repository".
7. (optional) Click "Configure JREs..." and add Java 6 and Java 7 (if not already installed, you will need to download from Oracle website)
8. Click Next. Enter a destination path in which to install the liberty server, e.g. C:\Program Files\IBM\Liberty. Select "Download and install a new runtime environment from ibm.com" and choose the latest "WAS Liberty with Java EE 7 Web Profile"
(9. In "Install Additional Content", select "OSGi debug console". Click Next. Accept terms and conditions. Finish. Liberty will be extracted and installed.)
10. In Window > Preferences, go to Run/Debug > String Substitution. Create a new entry called was_liberty_root and point to the folder for liberty (the folder will contain subfolders bin, clients, dev, java etc)
11. Right-click on Websphere Application Server Liberty Profile and select New > Folder. Create a new folder called "extension". Right-click on the extension folder and select New > Folder. Create a new folder called "lib".
12. Right-click on the server in the Servers view and select New > Server Environment File > server.env. Add a variable called PATH with a value mapping to your Domino server. E.g. PATH=C:\Program Files\IBM\Domino.
13. Add a file under default server called bootstrap.properties. Add a variable xworlds.developermode=true. (Here is where you would also define the port for the osgi console - osgi.console=5676)

14. Right-click in the Enterprise Explorer pane and select Import..., then General > Existing Projects into Workspace. Select the CrossWorlds download, ensuring Copy projects into workspace is ticked.
16. In Window > Preferences, go to Plug-in Development > Target Platform and select "Websphere Application Server Liberty Profile with SPI".

17. Open org.openntf.xworlds.dominodeps project. Open dependencies.txt and note the five external jars required. Copy and paste them into BundleContent folder in org.openntf.xworlds.dominodeps
	notes.jar is in jvm\lib\ext
	com.ibm.icu_3.8.1.v20120530 is in Domino\osgi\rcp\eclipse\plugins
	com.ibm.icu.base_3.8.1.v20080530 is in Domino\osgi\rcp\eclipse\plugins
	lwpd.commons.jar is in Domino\osgi\shared\eclipse\plugins\com.ibm.commons_9.0.1.20131004-1200
	lwpd.domino.napi.jar is in Domino\osgi\shared\eclipse\plugins\com.ibm.domino.napi_9.0.1.20131004-1200
18. Right-click on org.openntf.xworlds.dominodeps and select New > Folder. Create a new folder called src.

19. Download ODA zip file and extract contents to e.g. C:\temp\oda
20. Open org.openntf.xworlds.oda. In build.properties (top-level file) amend the variables according to notes, so the first maps to the plugins folder of the update site (using "/", not "\") and the second is the unique date-time suffix of the main org.openntf.domino.... jars.
21. Right-click on org.openntf.xworlds.oda and select New > Folder. Create a new folder called src.
22. Right-click on build.xml and select Run As > Ant Build.... Ensure the JRE (on JRE tab) is greater than JRE1.6. Once completed, select org.openntf.xworlds.oda and press F5 to refresh the files.

23. Right-click on org.openntf.xworlds.features.server and select Install Feature.... (If it says it's already loaded, use Update Feature instead)
24. Open servers > default server > server.xml and click on Feature Manager. Click Add... and select usr:CrossWorlds-1.0. Repeat for cdi-1.2, jaxrs-2.0, localConnector-1.0.

25. Right-click and select Import > Maven Projects and navigate to folder containing pom.xml of OdaDemoAppLiberty.
26. If you receive errors with org.openntf.xworlds.webapp.j2eeenabler, try closing the project and re-opening it. If that doesn't fix it, check Maven Settings in Eclipse for repository location. Then check org/openntf/xworlds/webapp/j2eenabler folder exists. If not, extract from attached, then close and re-open the project.
