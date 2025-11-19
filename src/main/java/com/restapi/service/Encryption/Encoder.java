package com.restapi.service.Encryption;

import org.springframework.stereotype.Component;

import static com.mysql.cj.util.StringUtils.toAsciiString;

@Component
public class Encoder {
    public void encoder(String message) {
        RSAEncryptor rsa = new RSAEncryptor(61, 53, 7);
        rsa.setP(61);
        rsa.setQ(53);

//        RSAEncryptor rsa = new RSAEncryptor(p, q, e);
//        rsa.setP(p);
//        rsa.setQ(q);

        String asciiString = toAsciiString(message);
        String ciphertext = rsa.encrypt(asciiString);
        String decryptedAsciiString = rsa.decrypt(ciphertext);

        System.out.println("plaintext: " + message);
        System.out.println("Ciphertext: " + ciphertext);
        System.out.println("Decrypted ascii message: "+decryptedAsciiString);
//        System.out.println("Decrypted string message: " + rsa.convertASCIIStringToString(ciphertext));
    }

    private static String toAsciiString(String message) {
        StringBuilder sb = new StringBuilder();
        for (char c : message.toCharArray()) {
            int asciiCode = (int) c;
            sb.append(asciiCode);
        }
        return sb.toString();
    }
}
