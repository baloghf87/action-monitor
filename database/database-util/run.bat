cmd /c mvn clean package
cd target
java -jar database-util-1.0.jar
cd ..