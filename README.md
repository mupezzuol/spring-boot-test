# Spring Boot Test

- This project was created through the `Software Testing` course, to access it click [here](https://amigoscode.com/courses/software-testing).
- This repository has some changes and improvements to what is presented in the course.

![banner-course](src/main/resources/prints/software-testing-banner.png)

## Index :pushpin:
- [About](#about) 
- [Architecture Diagram](#architecture)
- [Coverage Examples](#coverage)
- [Show me the code](#code)

## About <a name="about"></a> :bulb:

Software Testing is a skill that you must fully grasp as a software engineer. It ensures that any code you write to production is more likely to contain less bugs.

In this repository you will see everything about some types of tests implemented that were presented in the [course](https://amigoscode.com/courses/software-testing):
- Unit Testing
- Integration Testing
- Testing External Services
- Mocking with Mockito
- Test Driven Development -`TDD`

## Architecture Diagram <a name="architecture"></a> :pencil2:

- This diagram shows how the system and its layers were separated to understand each part of the tests.
![architecture](src/main/resources/prints/architecture-diagram.png)

## Coverage Examples <a name="coverage"></a> :white_check_mark:

#### SonarQube
![sonarqube](src/main/resources/prints/sonarqube-report.png)

#### IntelliJ IDEA
![sonarqube](src/main/resources/prints/green-test-coverage-line.png)

## Show me the code <a name="code"></a> :computer:

- Some tests in the repository.

```java
@Test
void itShouldSelectCustomerByPhoneNumberExists() {
    // Given
    UUID id = UUID.randomUUID();
    String phoneNumber = "0000";
    Customer customer = new Customer(id, "Murillo", phoneNumber);

    // When
    underTest.save(customer);
    Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);

    // Then
    assertThat(optionalCustomer)
            .isPresent()
            .hasValueSatisfying(c -> {
                assertThat(c).isEqualToComparingFieldByField(customer);
            });
}
```