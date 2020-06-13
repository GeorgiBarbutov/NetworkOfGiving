# Network of Giving

A web application for creating and participating in charities.

## Functionality
- Log in, Logout, Register users.
- Create, View charity.
- View all exsisting charities.
- Delete and Edit your created charities.
- Volunteer to and stop volunteering to charities.
- Donate to charities.
- Search for users, charities.
- View you and other users profiles.

## Bulid
Navigate to the project directory and run the command:
  
  ```mvn org.codehaus.mojo:exec-maven-plugin:1.5.0:java -Dexec.mainClass="com.barbutov.network_of_giving.NetworkOfGivingApplication"```
  
Building the project requires Maven and PosgreSql running on port 5432 with a user with the specified username/password in application.properties file. The project uses Spring Boot, Hibernate, Posgresql DB, Bootstrap
