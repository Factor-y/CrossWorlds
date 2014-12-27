# j2eeenabler for CrossWorlds appplications

This JAR project is a Servlet 3.0 WebFragment that should be added to any application needing CrossWorlds 
services.

This jar add to the web application

1) An ApplicationListener  which controls initialization / registration / deregistration / shutdown of services

2) A servlet filter applying to /* which sets up/tear down Domino thread context per request

The jar from this project needs to be copied into the WEB-INF/lib directory of you web applications.


It only contains the web-fragment.xml as the classed are shared in the core library at the applicaiton server level.