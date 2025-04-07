## Used Technologies
- Java 17
- Spring Boot 3
- Spring Data JPA
- MongoDB & PostgreSQL
- Flyway
- Maven
- Thymeleaf
- Kafka
- REST
- Docker

## About the Project
The project aims to simulate ecommerce platform that allows customers to browse products, place orders, process payments and send notifications. Every service is implemented as microservice using http requests and kafka to communicate. Services fetch configuration from config-server and use Eureka server for discovery. The main goal of this project was to learn and practice technologies used during development of applications based on microservice architecture.

Currently tests are only implemented for customer microservice (work in progress)

## How it works
- All microservices fetch configuration information from config server
- Services register in Eureka server for discovery
- Order microservice fetches information from Customer and Product services (http requests)
- After making order the Order service sends request to Payment service (http request) and also sends OrderConfirmation to Kafka
- Payment service receives request from Order service, creates payment and sends PaymentConfirmation to Kafka
- Notification service consumes confirmations from Kafka and sends email notification to users regarding their order
