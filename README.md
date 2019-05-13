# Backend Engineering Challenge

- This challenge was made using native Java 1.8
- Maven architecture was used to build this project
- No frameworks were used to accomplish this challenge
- For testing proposes, I've used JUnit Jupiter with Maven Surefire plugin and JaCoCO to test coverage measurement.

# Running the challenge

- Install Maven.
- Define the `M2_HOME` environment variable to allow the `mvn` usage.
- On ${PROJECT_HOME}, run ``mvn clean package`` to generate the executable JAR file. This command will generate an uber-jar named `challenge-unbabel-jar-with-dependencies.jar`
- The JAR file will be located on `${PROJECT_HOME}/target` folder.
- To execute the JAR, run ```java -jar target/challenge-unbabel-jar-with-dependencies.jar```.
- After running the above-mentioned command, a dynamic console interaction will begin.
- The first interaction is based on the file path. If you use a file on the same level as the JAR file, you can just pass the file name, such as `events.json`. Otherwise, you need to provide the absolute file path, such as `/the/full/path/to/events.json`. (By the way, there is a file `events.json` in the project root. Try it!! =D ) 
- The second and last interaction is based on the window size to extract the translation metrics.
- The response will be shown in the console and also will be exported to an external file (the file path will be displayed in the console)

## Validations and exceptions

- The file path must not be null.
- An error will occur if the file path was wrong or the file doesn't exist.
- The file can't have empty content.
- The line properties must match with the properties mentioned in the challenge description.
- The window size can't be negative.
- The window size can be zero. This will consider the most recent timestamp as value.
- The timestamp property must be a valid date.

# Running the tests separately

- To run all test scenarios independently from the Maven package phase, you can run ```mvn clean test```
- This will trigger all the test cases from this application.
- The tests were made considering only the business logic layer.
- I would like to increase the encapsulation on the MetricService class setting all the auxiliary methods in a private scope, but this would reduce drastically test cases. That's why I've chosen to give them a package scope and allow the method extraction for explicit test scenarios.
- To access the coverage report generated by JaCoCo, you can open the `index.html` file located on `target/site/jacoco` 

# Important note

- The file structure used to extract the entity model is on compliance with the flat model provided with the original challenge repository.
````
{"timestamp": "2018-12-26 18:11:08.509654","translation_id": "5aa5b2f39f7254a75aa5","source_language": "en","target_language": "fr","client_name": "easyjet","event_name": "translation_delivered","nr_words": 30, "duration": 20}
{"timestamp": "2018-12-26 18:15:19.903159","translation_id": "5aa5b2f39f7254a75aa4","source_language": "en","target_language": "fr","client_name": "easyjet","event_name": "translation_delivered","nr_words": 30, "duration": 31}
{"timestamp": "2018-12-26 18:23:19.903159","translation_id": "5aa5b2f39f7254a75bb33","source_language": "en","target_language": "fr","client_name": "booking","event_name": "translation_delivered","nr_words": 100, "duration": 54}
````
