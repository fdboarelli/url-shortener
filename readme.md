# URL Shortener Service

A simple URL shortener service built with Spring Boot 3 (Java 21), PostgreSQL, and Redis for caching.

Virtual threads are used to boost scaling performances.

**IMPORTANT NOTE**: This project is made for hobby and it is not *production ready*. No auth, BFA protections or any other security feature is present (for now at least ;) )

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Possible improvements](#improvements)
- [License](#license)

## Introduction

This project is a crud URL shortener service that allows you to create short aliases for long URLs. 

Written in Java, it uses Spring Boot 3 as main framework while Postgresql is used as database.
There is also a cache layer powered by Redis to improve performance for high-load traffic.

## Features

- Create shorter URL for long URLs using sha256-v3 hashing. 
- Get URL details by id.
- Get all present URLS.
- Allows deletions of URLs by id

Update of URLs has not been implemented as changing a short URL to another one of same format doesn't seem a useful operation.

**Note** The produced hash is then encoded in base64 and only
the first 9 digits are used (git-like approach). To reduce hash collisions due to poor randomness of the input,
we're concatenating to the input the timestamp in milliseconds at the time of computation.
With this solution, we have 18 quadrillions possible combination for our nine chars length short-urls.

## Prerequisites

Following stack have been used:

- Spring Boot 3.2.2 with Java 21 (Maven project)
- PostgreSQL as datasource for persisting URLs.
- Redis server as caching system.

To make local deployment as easy as possible, the project use Spring Boot's `docker-compose` plugin: you can find the
`docker-compose.yml` file in the `src/main/resources` folder, all needed dependencies are already configured there.

It should work out of the box.

## Getting Started

Getting started with the project needs few basic steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/fdboarelli/url-shortener.git
   cd url-shortener

2. Build the project using Maven:
    ```bash
    mvn clean install
    ```
   
3. Run the application (active Docker instance is needed):

   ```bash
   mvn spring-boot:run -Pdocker-compose
   ```
## Configuration

Default values are used for PostgreSQL and Redis, but if you can configure the application by modifying the `application.properties` file in the `src/main/resources` directory. 

Following properties are defined for PostgreSQL connection:

```
spring.datasource.url: Configure your PostgreSQL database connection URL.
spring.datasource.username: Specify the PostgreSQL username.
spring.datasource.password: Specify the PostgreSQL password.
```

Similarly we have following properties for Redis connection:

```
spring.redis.host: Configure the Redis server host.
spring.redis.port: Configure the Redis server port.
```

There are also few Spring's related properties for `docker-compose` to run the project with previously mentioned command, no need to change those.

## Usage

After launching the service, you can use the [postman collection](assets/url-shortener.postman_collection.json) that also contains documentation for each endpoint.

## Endpoints

Here's a short description of the available CRUD endpoints:

Create short URL:
- Endpoint: /v1/urls
- Method: POST
- Response http status: 201
- Response body: JSON with `url` property corresponding to shortened URL

Get original URL by id:
- Endpoint: /v1/urls/{url_id}
- Method: GET
- Response http status: 200
- Response body: URL object with all details

Get all URLs:
- Endpoint: /v1/urls
- Method: GET
- Response http status: 200
- Response body: List of all URL objects with all details

Delete original URL by id:
- Endpoint: /v1/urls/{url_id}
- Method: DELETE
- Response http status: 204
- Response body: empty

It is not possible to update an existing URLs

## Improvements

Here's a list of possible improvements that could be implemented to improve the service:
- Add JMeter stress tests to evaluate global performances.
- Implement auth and BFA protections.

## License

This project is licensed under the MIT [LICENSE](LICENSE.txt) - see the LICENSE file for details.