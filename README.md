# Quota Management

A lightweight Spring Boot library for managing and enforcing resource quotas per tenant.

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-green.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## Features

- 🎯 **Annotation-based quota enforcement** - Simply add `@Quota` to your methods
- 🏢 **Multi-tenant support** - Define quotas per user, organization, or any custom tenant
- 📊 **Tier-based limits** - Different limits for free, pro, enterprise tiers
- 🔌 **Pluggable architecture** - Implement your own counters and resolvers
- ⚡ **Spring Boot auto-configuration** - Zero configuration to get started
- 🧪 **Easy to test** - Mockable interfaces for unit testing

## Installation

### Maven

```xml
<dependency>
    <groupId>com.github.tbcd</groupId>
    <artifactId>quota-management</artifactId>
    <version>1.0.0</version>
</dependency>