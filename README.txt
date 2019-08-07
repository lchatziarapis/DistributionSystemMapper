To start an instance of a module, you need to run:

java -jar [jar] [master host] [master port] [worker port] [worker type]

Where valid worker types are:
0 - Master (Worker port value does not matter)
1 - MapWorker (Or simply worker - Does the hard storage and sends Google API requests)
2 - MapReducer (Reduces the results from the rest of the workers to a single result)

To build the jar file, execute:
- gradlew.bat jar (Windows)
- ./gradlew jar (Unix, make sure there are execute permissions)

The jar file can be found in build/libs

gradlew.bat jar -Dorg.gradle.java.home="C:\Program Files\Java\jdk1.8.0_31"
