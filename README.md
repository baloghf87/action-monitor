# action-monitor

Prerequisites: JDK 8, Maven 3

Usage:

I. mvn package

II. cd webserver

III. mvn spring-boot:run
     or
     java -jar target/webserver-1.0.war    
     or
     deploy target/webserver-1.0.war file to container
     or
     start hu.ferencbalogh.actionmonitor.web.Application class in your IDE

IV. Visit localhost:8080 with a browser

V. cd database/database-util
   
VI. java -jar target/database-util-1.0.jar 
    or 
    run.bat (on windows)

VII. Follow the instructions to interact with the database and see the action reports in the connected browser(s)