# Bakckend Engineering Challenge

- This challenge was made using native Java 1.8
- No frameworks were used to accomplish this challenge

# Running the challenge

- Install Maven.
- Define the `M2_HOME` environment variable to allow the `mvn` usage.
- On ${PROJECT_HOME}, run ``mvn clean package`` to generate the executable JAR file. This command will generate an uber-jar named `challenge-unbabel-jar-with-dependencies.jar`
- The JAR file will be located on `${PROJECT_HOME}/target` folder.
- To execute the JAR, run ```java -jar target/challenge-unbabel-jar-with-dependencies.jar```.

# Application execution notes

- After running the above mentioned command, a dynamic console interaction will began.
- The first interaction is based on the file path. If you use a file on the same level as the JAR file, you can just pass the file name, such as `events.json`. Otherwise, you need to provide the absolute file path, such as `/the/full/path/to/events.json`.
- The second and last interaction is based on the window size to extract the translation metrics.
- The file structure used and extracted as model is on compliance with the flat model provided with the original challenge repository.
````
{"timestamp": "2018-12-26 18:11:08.509654","translation_id": "5aa5b2f39f7254a75aa5","source_language": "en","target_language": "fr","client_name": "easyjet","event_name": "translation_delivered","nr_words": 30, "duration": 20}
{"timestamp": "2018-12-26 18:15:19.903159","translation_id": "5aa5b2f39f7254a75aa4","source_language": "en","target_language": "fr","client_name": "easyjet","event_name": "translation_delivered","nr_words": 30, "duration": 31}
{"timestamp": "2018-12-26 18:23:19.903159","translation_id": "5aa5b2f39f7254a75bb33","source_language": "en","target_language": "fr","client_name": "booking","event_name": "translation_delivered","nr_words": 100, "duration": 54}
````
