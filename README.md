# camel-quarkus kafka with schema-registry example

This example shows how camel-quarkus-kafka can be used in combination with confluent schema-registry.

## Preconditions
* You need to have a local kafka cluster running. You can use the provided `docker-compose.yml`.
* You need access to confluent.io maven repo: https://packages.confluent.io/maven/. You might need to add it to your `settings.xml`

## Starting the sample

The example covers twice the same use case:
* Invoke a local http endpoint with a json message. 
* The message will be written by a camel-route to kafka. 
* Another route will receive the message and print the body

The example is twice implemented. Once with a simple string serialization and once using the avro format and the schema-registry from confluent. 

```
# Invoking endoind for string message
curl -H "Content-Type: application/json" -X POST -d '{"name":"stef","message":"my string"}' http://localhost:8080/string

# Invoking endpoint for avro message
curl -H "Content-Type: application/json" -X POST -d '{"name":"andy","message":"my avro"}' http://localhost:8080/avro
```

Note that the producer registers the avro schema automatically at the schema-registry. Check the availables schemas with the schema-registry API (after avro endpoint has been invoked)

```
curl localhost:8081/subjects
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `code-with-quarkus-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/code-with-quarkus-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/code-with-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

# RESTEasy JAX-RS

<p>A Hello World RESTEasy resource</p>

Guide: https://quarkus.io/guides/rest-json
