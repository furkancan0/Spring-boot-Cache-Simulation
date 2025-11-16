# Spring Boot In memory caching service
Spring Boot caching service that implements:  

- In-memory TTL (Time-To-Live) cache  

- LRU (Least Recently Used) eviction  

- Size limit for cache entries  

- Prometheus Grafana metrics (Micrometer)  

- Grafana-ready structured JSON logs  

This repository demonstrates how to build a production-style caching layer without external dependencies (like Redis), suitable for learning GC behavior, eviction strategies, and observability.
