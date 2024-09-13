package io.quarkus.smallrye.graphql.client.runtime;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jboss.logging.Logger;

import io.quarkus.tls.CertificateUpdatedEvent;
import io.quarkus.tls.TlsConfiguration;
import io.smallrye.graphql.client.impl.GraphQLClientsConfiguration;

@Singleton
public class GraphQLClientCertificateUpdateEventListener {

    private final static Logger LOG = Logger.getLogger(GraphQLClientCertificateUpdateEventListener.class);

    @Inject
    GraphQLClientsConfig graphQLClientsConfig;

    public void onCertificateUpdate(@Observes CertificateUpdatedEvent event) {
        String updatedTlsBucketName = event.name();
        TlsConfiguration updatedTlsConfiguration = event.tlsConfiguration();

        graphQLClientsConfig.clients
                .forEach((configKey, clientConfig) -> clientConfig.tlsBucketName.ifPresent(tlsBucketName -> {
                    GraphQLClientsConfiguration graphQLClientsConfiguration = GraphQLClientsConfiguration.getInstance();
                    if (tlsBucketName.equals(updatedTlsBucketName)) {
                        LOG.infof("Certificate reloaded for the client '%s' using tls-bucket-name '%s'", configKey,
                                tlsBucketName);
                        graphQLClientsConfiguration.getClient(configKey).setSslOptions(updatedTlsConfiguration.getSSLOptions());
                        graphQLClientsConfiguration.getClient(configKey)
                                .setTlsKeyStoreOptions(updatedTlsConfiguration.getKeyStoreOptions());
                        graphQLClientsConfiguration.getClient(configKey)
                                .setTlsTrustStoreOptions(updatedTlsConfiguration.getTrustStoreOptions());
                        graphQLClientsConfiguration.getClient(configKey)
                                .setUsesSni(Boolean.valueOf(updatedTlsConfiguration.usesSni()));
                    }
                }));
    }
}
