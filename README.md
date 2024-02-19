# Cloud Native Web App

## Overview

This is a cloud-native web application built with Spring Boot and PostgreSQL.

## Prerequisites

To install and run the app locally, you need to have the following installed:

- JDK 17 or later
- Maven 3.2 or later
- PostgreSQL


## Installation

- Clone the repository to your local machine:

   ```sh
   git clone git@github.com:csye-6225-cloud-native/webapp.git
   ```

- Navigate to the project directory:
   
    ```sh
   cd webapp
   ```

- Build the project using Maven:

    ```sh
   mvn clean install -DskipTests
   ```

- Manually configure the following properties in the application.yml file under src/main/resources:
    ```yml
    spring:
        datasource:
            url: "Your jdbc url"
            username: "Your db username"
            password: "Your db password"
    ```

    OR

    Configure the following environment variables (recommended):
    ```sh
    export POSTGRES_USER=test
    export POSTGRES_PASSWORD=test
    ```
    

- Run the application:
   ```sh
   mvn spring-boot:run
   ```
    > The application should be running on `http://localhost:8080`

## Testing

- To run the tests:

    ```sh
    mvn test
    ```

_________
## Author

[Pritesh Nimje](mailto:nimje.p@northeastern.edu)
