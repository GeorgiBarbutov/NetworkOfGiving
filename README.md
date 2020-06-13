# Network of Giving

A web application for creating and participating in charities.

It has the following funcionality:

  • Log in, Logout, Register users.
  • Create, View charity.
  • View all exsisting charities.
  • Delete and Edit your created charities.
  • Volunteer to and stop volunteering to charities.
  • Donate to charities.
  • Search for users, charities.
  • View you and other users profiles.

The project uses Spring Boot, Hibernate, Posgresql DB

Building the project requires: Maven and PosgreSql running on port 5432 with a user with the specified username/password in application.properties file.

Building and running the project:
  mvn org.codehaus.mojo:exec-maven-plugin:1.5.0:java -Dexec.mainClass="com.barbutov.network_of_giving.NetworkOfGivingApplication"
