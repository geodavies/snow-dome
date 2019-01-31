# Snow Dome - DC2060 Coursework

## Installation
This application uses Gradle to manage project dependencies, run the below steps to download the required dependencies and package the application war file.

### Configuration
Application configuration is found in ```/src/main/resources/application.yml```. Here you can change the connection to the MySQL database.

### Build
**Windows**
```
gradlew.bat build
```
**Linux**
```
./gradlew build
```
### Execute
```
java -jar build/libs/snow-dome-1.0.0.war
```
## Architecture
I decided to depart substantially from the lab exercises architecture style. These are the things I've changed and why and explanations of why I've decided to do so.

### Multiple Controllers
I decided to make this application is made up of multiple controllers so that I can have a logical separation of concerns within the application. I have 4 controllers: BookingController, ClientController, IndexController and LessonController. Each of these are responsible for controlling application flow in each of their respective areas.

### Multiple Services
Just like with the controllers I have also split the services into their own classes again for separation of concerns. My service classes are as follows: BookingService, ClientService and LessonService.

### Spring Data (JPA)
This application is using spring data in order to access database content. I chose to use this because it makes interactions with the database easy to write and maintain. I also like the way the repositories are queried by how the interface methods are written.

### Gradle
I am using Gradle to manage project dependencies and compilation because it's so much easier to manage everything in one place. This also allows easy packaging of a java war file which can be run as a standalone web application. This is also common industry practice.

### Exception Propogation
I'm using a custom ControllerExceptionHandler to propagate the exceptions thrown by my application back the client. This handler will intercept and exceptions thrown by my controllers and convert them into a status code and body to be returned back to the user. This makes it simpler to handle exceptions and also keeps the code more maintainable because it means that it is not the responsibility of the controllers to know what to do if something goes wrong. This means all controllers will exhibit the exact same behavior.

### Lombok
The Lombok library is being used in this application to make my database models cleaner and easier to interact with. Lombok will generate useful methods for an appropriately annotated class such as getters and setters as well as equals() and hashCode(). This means that model classes are not cluttered with code which doesn't provide much value.

### Testing
MockMvc has been used to test the application at an integration level and has been particularly useful for automated regression testing when implementing new features. MockMvc allows the tests to perform mock HTTP calls to the service and assert on the results returned.

I have used a H2 in-memory database for testing purposes which allows the tests to quickly set up test data and assert that data has been read, modified or created correctly.
