# Action-monitor

`[Client 1]------v                                                                         /--> [ Websocket client 1 ]
[Client 2]----->[ HSQLDB ] --- Trigger ---> [ ActiveMQ ] ---> [ Spring Integration ] --------> [ Websocket client 2 ]
[Client n]------^                                                                         \--> [ Websocket client n ]
 ...                                                                                             ...
`
**Prerequisites**: JDK 8, Maven 3

**Usage**:

1. mvn package

2. cd webserver

3. * mvn spring-boot:run **or**
   * java -jar target/webserver-1.0.war **or**
   * deploy target/webserver-1.0.war file to container **or**
   * start hu.ferencbalogh.actionmonitor.web.Application class in your IDE

4. Visit localhost:8080 with browser(s)

6. cd database/database-util
   
7. * java -jar target/database-util-1.0.jar **or**
   * run.bat (on windows)

8. Follow the instructions to interact with the database and see the action reports in the connected browser(s)