# Directory Tree Application

This is a Spring Boot application that implements a directory tree structure with a persistent H2 database. The application allows users to create, move, delete, and list directories through a RESTful API.

## Project Structure

The project is organized as follows:

```
/data
    *.db
    .gitkeep
/src/main/java/com/example/directorytree
    /controller
        DirectoryTreeController.java
    /exception
        DirectoryAlreadyExistsException.java
    /model
        Directory.java
    /service
        DirectoryTreeService.java
/src/main/resources
    application.properties
mvnw
mvnw.cmd
pom.xml
README.md
.gitignore
```

## Prerequisites

### For Building

- Java 17
- Maven
- Git

### For running .jar

- Java 17

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/jussaw/directory-tree-service.git
   cd directory-tree-service
   ```

2. Build the project:

   ```bash
   mvn clean install
   ```

3. Run the application:

   ```bash
   mvn spring-boot:run
   ```

4. Alternatively, you can run the jar file:

   ```bash
   java -jar directory-tree-service-0.0.1-SNAPSHOT.jar
   ```

   **Note:** Running the jar file will create a `data` directory in the current directory where the jar is run. This directory contains the H2 database file.

## API Endpoints

- **Create Directory**

  ```http
  POST /directories/create?path={path}
  ```

- **Move Directory**

  ```http
  POST /directories/move?sourcePath={sourcePath}&targetPath={targetPath}
  ```

- **Delete Directory**

  ```http
  DELETE /directories/delete?path={path}
  ```

- **List Directories**

  ```http
  GET /directories/list
  ```

## Example Usage

1. Create directories:

   ```bash
   curl -X POST "http://localhost:8080/directories/create?path=fruits"
   curl -X POST "http://localhost:8080/directories/create?path=fruits/apples"
   ```

2. Move a directory:

   ```bash
   curl -X POST "http://localhost:8080/directories/move?sourcePath=fruits/apples&targetPath=vegetables"
   ```

3. Delete a directory:

   ```bash
   curl -X DELETE "http://localhost:8080/directories/delete?path=fruits/apples"
   ```

4. List directories:

   ```bash
   curl -X GET "http://localhost:8080/directories/list"
   ```
