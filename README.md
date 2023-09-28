# mutual-tls-plus-oauth-java-test

## this is a simple test of mtls and oauth
#### test-mtls-server1 has several endpoints, some unprotected, some protected with mtls and some with oauth. The purpose test-mtls-server2 is only to call the mtls protected endpoint. You will have to configure your own oauth provider to test the oauth part.

### Will add more info and process to create certs in the future. 
#### Dont think I need to say this, but the passwords should't be hardcoded. This is just for testing purposes. 

### Any question just message me
### Also sugestions are very much welcomed! 
#### Reference articles: 
https://medium.com/@salarai.de/how-to-enable-mutual-tls-in-a-sprint-boot-application-77144047940f

https://connect2id.com/products/nimbus-oauth-openid-connect-sdk/examples/utils/custom-trust-store

# How to create certificates: 

## Generate root certificates: 
### Generate root key:
 openssl genrsa -des3 -out rootCA.key 2048
		
### Generate a certificate using the root key:
 openssl req -x509 -new -nodes -key rootCA.key -sha256 -days 1825 -out rootCA.pem
		
## Generate server certificate:
### Generate server private key:
 openssl genrsa -des3 -out server.key 2048
	
### Generate a CSR(Certificate Signing Request):
 openssl req -new -sha256 -key server.key -out server.csr
	
### Sign(generate a server certificate with the CSR: 
 openssl x509 -req -in server.csr -CA rootCA.pem -CAkey rootCA.key -CAcreateserial -out server.pem -days 365 -sha256
		
## Generate client key: 
### Generate client private key:
 openssl genrsa -des3 -out client.key 2048
		
### Generate a CSR(Certificate Signing Request):
 openssl req -new -sha256 -key client.key -out client.csr
		
### Sign(generate) a client certificate with the CSR: 
 openssl x509 -req -in client.csr -CA rootCA.pem -CAkey rootCA.key -CAcreateserial -out client.pem -days 365 -sha256
	
## Generate server keystore: 
 openssl pkcs12 -export -in server.pem -out serverkeystore.p12 -name server -nodes -inkey server.key

## Generate client keystore: 
 openssl pkcs12 -export -in client.pem -out clientkeystore.p12 -name server -nodes -inkey client.key

## Generate a trust store where you will put the root CA (this can be used by both server and client as both certificates are signed by the root CA):
 keytool -import -file rootCA.pem -alias rootCA -keystore truststore.p12
	
	