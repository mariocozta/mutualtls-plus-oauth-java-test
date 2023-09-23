package com.test.testmtlsserver2.service;

import org.springframework.stereotype.Service;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.*;
import java.util.Enumeration;

@Service
public class TestConnectionWithTLSService {

    public String testSendRequestWithTLS() throws Exception {
        // Load your client certificate and private key from the keystore
        KeyStore clientKeyStore = KeyStore.getInstance("PKCS12");
        clientKeyStore.load(TestConnectionWithTLSService.class.getResourceAsStream("/keystore.p12"), "changeit".toCharArray());

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(TestConnectionWithTLSService.class.getResourceAsStream("/truststore.p12"), "changeit".toCharArray());


        Enumeration<String> aliases = clientKeyStore.aliases();
        String alias = null;
        while (aliases.hasMoreElements()) {
            alias = aliases.nextElement();
        }

        // Build the HttpClient with client certificates
        HttpClient client = HttpClient.newBuilder()
                .sslContext(createSSLContext(clientKeyStore, "changeit",trustStore))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://localhost:5010/api/testMtls/protected")) // Use "https" for secure communication
                .header("Content-Type", "application/json")
                .GET()
                .build();

        // Send the HTTP request
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Handle the response
        System.out.println("Response Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());

        return  response.body();
    }

    private static SSLContext createSSLContext(KeyStore keyStore, String keyPassword,KeyStore trustStore ) throws Exception {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyPassword.toCharArray());


        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return sslContext;
    }

}
