package com.example.softwareloggingapp;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.zipkin.ZipkinSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenTelemetry integration.
 * This class sets up tracing for the application using OpenTelemetry SDK
 * and configures it to export tracing data to a Zipkin endpoint.
 */
@Configuration
public class OpenTelemetryConfig {

    /**
     * Configures and provides an instance of OpenTelemetry.
     * Sets up a Zipkin exporter, associates the tracing data with a resource name,
     * and configures the SDK tracer provider.
     *
     * @return the configured OpenTelemetry instance.
     */
    @Bean
    public OpenTelemetry openTelemetry() {
        // Configure the Zipkin exporter with the specified endpoint
        ZipkinSpanExporter zipkinExporter = ZipkinSpanExporter.builder()
                .setEndpoint("http://localhost:9414/api/v2/spans")
                .build();

        // Define the resource with a service name for trace attribution
        Resource serviceResource = Resource.builder()
                .put(ResourceAttributes.SERVICE_NAME, "ProfilingService") // Service name for trace identification
                .build();

        // Configure the SDK tracer provider with the batch span processor and service resource
        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(zipkinExporter).build()) // Batch span processor for efficient export
                .setResource(serviceResource) // Associate traces with the defined service resource
                .build();

        // Build and return the OpenTelemetry SDK instance
        return OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider) // Attach the configured tracer provider
                .build();
    }

    /**
     * Provides a Tracer bean for the application.
     * The tracer is used to create and manage tracing spans for instrumentation.
     *
     * @param openTelemetry the OpenTelemetry instance to retrieve the tracer from.
     * @return the configured Tracer instance.
     */
    @Bean
    public Tracer tracer(OpenTelemetry openTelemetry) {
        // Retrieve and return a tracer with a custom name
        return openTelemetry.getTracer("backend-tracer"); // Identifier for tracing spans in the application
    }
}
