# Distributed system with Kafka
This app is a demonstration of distributed system consisting of microservices communicating via kafka cluster. The system performs simple math calculations (which mock resource intensive operations). It consists of:

* controller – creates tasks and collects results
* scheduler – distributes tasks among workers and controls the load. Least efficient workers are supported by “superworker”. Scheduler dynamically assign tasks to workers or superworker depending on the load
* regular workers – each of them can perform only one type of operation
* superworker – can perform any type of operation – supports least effective workers
* kafka cluster – handles all communication between microservices.

Flexible configuration allows setting of arbitrary nr of workers instances and configuring task time consumption factor to test different set-ups.
![kafka_description](https://github.com/mpdgr/distributed-computing-kafka/assets/95987591/1e94bedd-68ad-4aa7-990c-a30352afb7ce)
