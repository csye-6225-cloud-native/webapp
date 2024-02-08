# Cloud Native Web App

### Prerequisites

What things you need to install the software and how to install them:

- JDK 17 or later
- Maven 3.2+
- PostgreSQL

### Installing

1. Clone the repository to your local machine: git clone <"Repo URL">

2. Navigate to the project directory: cd webapp

3. Build the project using Maven: mvn clean install -DskipTests


4. Manually configure the following properties in application.yml file under src/main/resources as follows:
```
    spring:
        datasource:
            url: "jdbc db url"
            username: "Your db username"
            password: "Your db password"
```

OR

Configure the following ENV variables:

```
    POSTGRES_DB
    POSTGRES_USER
    POSTGRES_PASSWORD
```

5. Run the application: `mvn spring-boot:run`

OR

Run the application with ENV variables: `POSTGRES_DB=test POSTGRES_USER=test POSTGRES_PASSWORD=test mvn spring-boot:run`

The application should now be running on http://localhost:8080.


# Authors

- *Pritesh Nimje* 