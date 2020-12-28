package org.acme;

import javax.ws.rs.POST;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.acme.avro.AvroMessage;

public class KafkaRoute extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {

        /**
         * Route to/from Kafka using string deserialzer/serializer
         */
        from(platformHttp("/string").httpMethodRestrict("POST"))  
            .to(kafka("{{kafka.topic.string}}")
                .brokers("{{kafka.bootstrap.servers}}"));

         from(kafka("{{kafka.topic.string}}")
            .brokers("{{kafka.bootstrap.servers}}"))
            .log("String message received: ${body}");



        /**
         * Route to/from Kafka using avro deserialzer/serializer
         */
        from(platformHttp("/avro").httpMethodRestrict("POST"))
            // unmashal json to pojo (based on generated code from message.avsc)
            .unmarshal().json(JsonLibrary.Jackson, AvroMessage.class)
            
            // Write pojo in avro format to kafka. Avro schema gets registered at schema-registry.
            .to(kafka("{{kafka.topic.avro}}")
                .brokers("{{kafka.bootstrap.servers}}")
                .schemaRegistryURL("{{schema.registry.url}}")
                .serializerClass("{{kafka.value.serializer}}"))

            .setBody(constant("done"));

        // Deserializes messages based on avro schema from schema-registry
        from(kafka("{{kafka.topic.avro}}")
            .brokers("{{kafka.bootstrap.servers}}")
            .schemaRegistryURL("{{schema.registry.url}}")
            .specificAvroReader(true)
            .valueDeserializer("{{kafka.value.deserializer}}"))

            .log("Avro message received: ${body}");
    }
}
