package com.test.testmtlsserver1.configuration.security.auth.provider.builder;



import com.test.testmtlsserver1.configuration.security.auth.properties.OauthProviderProperty;
import com.test.testmtlsserver1.configuration.security.auth.properties.OauthProvidersProperty;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.SupplierJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;


public abstract class AbstractAuthProviderBuilder implements JwtAuthenticationProviderBuilder {

    protected OauthProviderProperty providerProperty;
    /*
    @Value("${proxy.host}")
    private String proxyHost;
    @Value("${proxy.port}")
    private Integer proxyPort;
    @Value("${proxy.username}")
    private String proxyUser;
    @Value("${proxy.password}")
    private String proxyPassword;*/

    public AbstractAuthProviderBuilder(OauthProvidersProperty providersProperty) {
        providerProperty = providersProperty.getProviders().get(getProviderKey());
    }

    @Override
    public JwtAuthenticationProvider getProvider() {
        JwtDecoder decoder = new SupplierJwtDecoder(
                () -> JwtDecoders.fromIssuerLocation(providerProperty.getIssuerUri()));

        JwtAuthenticationProvider provider;
        /*if (proxyHost != null && !proxyHost.isEmpty()) {
            decoder = getJwtDecoder(providerProperty.getJwkSetUri());
        }*/
        provider = new JwtAuthenticationProvider(decoder);
        provider.setJwtAuthenticationConverter(customJwtAuthenticationConverter());
        return provider;
    }

    protected abstract JwtAuthenticationConverter customJwtAuthenticationConverter();

    protected abstract String getProviderKey();

/*
    private JwtDecoder getJwtDecoder(String uri) {

        RestTemplate restTemplate = new RestTemplate();
        if (proxyHost != null) {
            restTemplate = restTemplateBuilder().build();
        }
        return NimbusJwtDecoder.withJwkSetUri(uri).restOperations(restTemplate).build();
    }

    private void addProxy(RestTemplate template) {
        template.setRequestFactory(proxyHttpRequestFactory());
    }

    private HttpComponentsClientHttpRequestFactory proxyHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(proxyHttpClient());
        return clientHttpRequestFactory;
    }

    private HttpClient proxyHttpClient() {
        return HttpClientBuilder.create()
                .setProxy(proxyHost())
                .setDefaultCredentialsProvider(proxyCredentialsProvider())
                .build();
    }

    private HttpHost proxyHost() {
        return new HttpHost(proxyHost, proxyPort);
    }

    private BasicCredentialsProvider proxyCredentialsProvider() {
        BasicCredentialsProvider result = new BasicCredentialsProvider();
        result.setCredentials(new AuthScope(proxyHost()),
                new UsernamePasswordCredentials(this.proxyUser, this.proxyPassword));

        return result;
    }

    private RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder(this::addProxy);
    }*/
}
