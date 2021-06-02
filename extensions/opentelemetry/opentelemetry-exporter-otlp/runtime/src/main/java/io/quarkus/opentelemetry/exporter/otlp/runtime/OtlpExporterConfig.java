package io.quarkus.opentelemetry.exporter.otlp.runtime;

import java.time.Duration;
import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

public class OtlpExporterConfig {
    @ConfigRoot(name = "opentelemetry.tracer.exporter.otlp", phase = ConfigPhase.BUILD_AND_RUN_TIME_FIXED)
    public static class OtlpExporterBuildConfig {
        /**
         * Otlp SpanExporter support.
         * <p>
         * Otlp SpanExporter support is enabled by default.
         */
        @ConfigItem(defaultValue = "true")
        public Boolean enabled;
    }

    @ConfigRoot(name = "opentelemetry.tracer.exporter.otlp", phase = ConfigPhase.RUN_TIME)
    public static class OtlpExporterRuntimeConfig {
        /**
         * The Otlp endpoint to connect to. The endpoint must start with either http:// or https://.
         */
        @ConfigItem()
        public Optional<String> endpoint;

        /**
         * The maximum amount of time to wait for the collector to process exported spans before an exception is thrown.
         * A value of `0` will disable the timeout: the exporter will continue waiting until either exported spans are
         * processed,
         * or the connection fails, or is closed for some other reason.
         */
        @ConfigItem(defaultValue = "10S")
        public Duration exportTimeout;
    }
}
