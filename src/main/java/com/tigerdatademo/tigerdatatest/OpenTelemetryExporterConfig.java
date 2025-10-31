package com.tigerdatademo.tigerdatatest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;

@Configuration
public class OpenTelemetryExporterConfig {

	
	//@Bean
    public OtlpGrpcSpanExporter otlpGrpcSpanExporter(
            @Value("${opentelemetry.exporter.otlp.endpoint}") String url) {
		
        return OtlpGrpcSpanExporter.builder().setEndpoint(url).build();
    }
}
