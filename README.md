Generate RSA keypair (public, private + pkcs8) using openssl command

openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048

# Public key

openssl rsa -pubout -in private.pem -out public_key.pem

# Private key in pkcs8 format (for Java maybe :D)

openssl pkcs8 -topk8 -in private.pem -out private_key.pem

## nocrypt (Private key does have no password)

openssl pkcs8 -topk8 -in private.pem -nocrypt -out private_key.pem

---

Generating a Self-Signed Certificate

keytool -genkey -alias am -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650

keytool -genkey -alias am -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore-prod.p12 -validity 3650

