# Devices-API

> This project is a simple Java application that implements a REST-API that can register devices in a network deployment represented in a tree structure.


---
# Table of Contents

* [Overview](#overview)
* [Architectural Style](#architectural-style)
* [Aggregate Design](#aggregate-design)
* [Repository Pattern](#repository-pattern)
* [Use Cases](#use-cases)
* [DTO Mapping](#dto-mapping)
* [Concurrency](#concurrency)
* [Testing Strategy](#testing-strategy)

    * [Unit Tests](#unit-tests)
    * [Integration Tests](#integration-tests)
    * [Concurrency Tests](#concurrency-tests)
* [Error Handling](#error-handling)
* [Design Trade-offs](#design-trade-offs)

    * [In-memory Persistence](#in-memory-persistence)
    * [Lightweight DDD](#lightweight-ddd)
    * [Hexagonal Architecture](#hexagonal-architecture)
* [Future Improvements](#future-improvements)
* [Installation](#installation)
---

# Architecture & Design Decisions

## Overview

This project implements a REST API for managing a network topology of connected devices. The primary design goals were:

* Maintain a consistent network topology at all times.
* Keep the domain model independent from infrastructure concerns.
* Separate application orchestration from business logic.
* Produce a design that is easy to extend with a persistent storage implementation.
* Keep the solution intentionally lightweight without introducing unnecessary architectural complexity.

---

## Architectural Style

The project adopts a lightweight combination of **Domain-Driven Design (DDD)** and **Hexagonal Architecture (Ports & Adapters)**.

The application is organized into four primary layers:

```
adapter
├── in
│   └── web
└── out
    └── persistence

application
├── usecase
└── port

domain

configuration
```

Responsibilities are intentionally separated:

* **Domain** contains the business model and all business rules.
* **Application** coordinates use cases without implementing business logic.
* **Ports** define abstractions used by the application.
* **Adapters** provide concrete implementations (REST controllers and in-memory persistence).

This dependency direction ensures that the domain layer has no knowledge of Spring Boot or infrastructure.

---

## Aggregate Design

`DevicesNetwork` is the Aggregate Root.

All modifications to the network topology are performed through the aggregate.

Business invariants enforced by the aggregate include:

* MAC addresses are unique.
* Devices are connected according to the defined topology rules.
* Cyclic connections are not allowed.
* The topology remains internally consistent after every successful registration.

The rest of the application never manipulates device relationships directly.

---

## Repository Pattern

The application defines a repository abstraction:

```
DevicesNetworkRepository
```

which is implemented by:

```
InMemoryDevicesNetworkRepository
```

The application layer depends only on the repository interface.

Although the current implementation stores data in memory, another persistence implementation (JPA, MongoDB, Redis, etc.) can be introduced without changing the domain or application layers.

---

## Use Cases

Business operations are modeled explicitly as application use cases.

Examples:

* Register Device 
* Query Network Topology

The responsibilities of a use case are intentionally limited to:

1. Load aggregate.
2. Invoke domain behavior.
3. Persist updated aggregate.
4. Return the result.

Business rules remain inside the aggregate.

---

## DTO Mapping

The REST API does not expose domain objects directly.

Separate DTOs are used for:

* requests;
* responses;
* topology representation.

This prevents transport concerns from leaking into the domain model.

---

## Concurrency (Development in progress)

The application is designed so that device registration can safely support concurrent requests.

The repository is responsible for protecting the aggregate against concurrent modifications.

The aggregate itself remains free from synchronization concerns.

This separation allows the concurrency strategy to evolve independently from the domain model.

---

# Testing Strategy

The project contains three categories of automated tests.

### Unit Tests

Verify business rules in isolation.

Focus areas include:

* duplicate registration;
* topology construction;
* cycle detection;
* aggregate behavior.

### Integration Tests

Verify the REST API and Spring configuration.

### Concurrency Tests (Development in progress)

Dedicated integration tests verify that multiple concurrent registration requests cannot corrupt the network topology.

The tests validate correctness rather than performance.

---

## Error Handling

Business rule violations are represented as domain exceptions.

REST controllers translate those exceptions into appropriate HTTP responses.

This keeps HTTP concerns outside the domain model.

---

## Design Trade-offs

Several intentional design decisions were made during implementation.

### In-memory persistence

The assignment does not require persistent storage.

An in-memory repository was chosen to keep the implementation focused on domain modeling rather than infrastructure.

The repository abstraction allows a database-backed implementation to be added later without affecting the rest of the application.

---

### Lightweight DDD

The project intentionally applies DDD selectively.

Patterns such as CQRS, Event Sourcing, or Domain Events were deliberately omitted because they would add complexity without providing meaningful value for the problem domain.

The goal was to produce a simple, maintainable solution rather than demonstrate every available architectural pattern.

---

### Hexagonal Architecture

Hexagonal Architecture is used primarily to isolate business logic from external concerns.

Adapters may change over time, while the domain model remains stable.

This makes the application easier to test and easier to evolve.

---

## Future Improvements

Potential future enhancements include:

* database persistence (JPA or MongoDB);
* optimistic locking for concurrent updates;
* immutable aggregate snapshots;
* richer value objects;
* additional domain services for topology analysis;
* support for device removal and topology reconfiguration.

The current architecture was intentionally designed so these features can be introduced with minimal changes to the domain model.

---

# Installation
## Clone the repository

```bash
git clone https://github.com/mario4/device-service.git

cd device-service

```

## Build 

```bash
./gradlew build
```

## Run the application

### Prerequisites

- Java 17+  
- Gradle  
- Browser to access Swagger UI

```bash
./gradlew bootRun
```

alternatively, download the binaries from the latest release, unzip them and run the application by executing the command below:

```bash

cd <unzipped binary folder>

 ./bin/app --server-port=8081 
 
```

If the default port 8080 is already assigned to another service, use another port like so:

```bash
 ./bin/app --server-port=8081 
```


### Testing

`./gradlew test`

### Usage 

After running the application, head over to http://localhost:8080/swagger-ui/index.html#/ in the browser.

The swagger-ui is intuitive enough. There all api endpoints are available, including request and response schemas.