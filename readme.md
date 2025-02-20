# Instructions for Running

## About

This project is created as part of a test task.

## Test Task Requirements

### Task

Develop a Java application - a messenger based on Redis (user interface can be chosen, a console interface is allowed). The application must have the following functionality:

- Create a user and start communication from that user;
- View the list of registered users;
- Send and receive messages to/from any user in the database;
- Store and display the last message history;
- Implement a configuration file that stores some parameters (at least the connection parameters to Redis);
- Allow "online" communication between users (via Redis);
- Export chat history into a JSON text file.
   
### Requirements

- Create a Gradle project in IntelliJ IDEA;
- Cover the project with unit tests;
- Develop and store the application in a Git repository (e.g., https://bitbucket.org);
- DO NOT use the Spring framework.
   
How It Will Be Checked

- Attempt to run the application according to the instructions;
- Check the main functionality: run one application, create one user, run the second application, create a user, send a message to the first user. The first user should receive the message and send it back, with the second user receiving it;
- Check additional functionality (configuration, export, chat history view);
- Check the cleanliness, formatting, and structure of the code and project.

## Project Architecture

The application consists of a server part written in Java 17 and a client part written in JS.
The server part provides a REST API for managing users and retrieving message history. Websockets are used for real-time message exchange.

### Disclaimer (limitations and assumptions)

This application was developed as part of a test task and has the following limitations/assumptions:

- No secure access to endpoints has been implemented.
- Websocket session management for multi-deployment has not been implemented.
- Despite the requirement to cover the application with unit tests, only one unit test is provided as an example. The motivation behind this exception is the belief that unit test coverage does not necessarily reflect the technical level of the candidate. In a real production code, tests should certainly be present, but for the purpose of this task, full coverage is not meaningful.
- Since the test task is aimed at a backend developer, the client-side code in JS was written with a "works and that's enough" approach. This client-side code is only intended to demonstrate the server-side functionality.

## Quick Start

### Requirements

- Docker installed
- Running Redis instance

### Running the Server Part

1. Clone the project repository chat
2. In the configuration file config.yml (located in the src/main/resources folder), set the Redis connection parameters (if Redis is running in Docker, use its IP to avoid DNS name resolution issues).
3. In the terminal, navigate to the chat project directory.
4. Run the script run.sh (if necessary, give it execution permissions with the command chmod +X run.sh).

If executed successfully, you will see a log message similar to:

```
22-Jun-2023 12:33:21.453 INFO [main] org.apache.catalina.startup.Catalina.start Server startup in [678] milliseconds
```

### Running the Client Part

1. Clone the project repository chatclient
2. In the terminal, navigate to the chatclient project directory.
3. Run the script run.sh (if necessary, give it execution permissions with the command chmod +X run.sh).

If executed successfully, you will see a log message similar to:

```
2023/06/22 12:29:56 [notice] 1#1: start worker process 32
```

## Running on Windows

### Requirements

1. Docker installed

### Running the Server Part

1. In the terminal, navigate to the chat project directory.
2. Run the following commands:

```
gradlew.bat war
gradlew.bat assemble
docker build . -t chat
docker run -p 8080:8080 chat
```

### Running the Client Part

1. In the terminal, navigate to the chatclient project directory.
2. Run the following commands:

```
docker build . -t chatclient
docker run -p 8085:80 chatclient
```

## Checking the Functionality

1. Start the server part.
2. Start the client part.
3. Open two tabs of http://localhost:8085.
4. (Optional step if users are not created yet) In the first tab, enter the username in the "user name" field and click the "Create" button.
5. (Optional step if users are not created yet) To create a second user, repeat step 4 with a different username.
6. Click the "Refresh users" button.
7. From the dropdown menu next to the "Whoami" label, select the current user.
8. From the dropdown menu next to the "Send to" label, select the user you want to chat with.
9. Click the "Start chat" button (when successfully started, the button will change its label to "Connected").
10. In the "message" field, type a message and click "Send" (after successful sending, the message will appear in the text area below with the sender's name).
11. Switch to the second tab and select the user you chose as "Send to" on the first tab as "Whoami" and vice versa (in other words, switch the roles of the users in the second tab).
12. Click the "Start chat" button.
13. In the second tab's "message" field, type a message and click "Send".
14. Go back to the first tab (after successful sending, the message from the second tab will appear in the text area of the first tab).
15. To export the chat history, click the "Download history" button.
