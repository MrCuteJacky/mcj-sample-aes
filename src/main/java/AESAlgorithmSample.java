import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AESAlgorithmTest
 *
 * @author mrcutejacky
 * @version 1.0
 */
public class AESAlgorithmSample {

    public static void main(String[] args) {

        String secretKey = "C03458ECE748123456789ABCDEFA8A4C";
        String ivParameter = "Dt11y88o17X/XkaaQvAbuA==";
        String origin = "AA123456781234";
        String encrypt, decrypt;

        AESAlgorithm aesAlgorithm = new AESAlgorithm(AESAlgorithm.toByteArray(secretKey), Base64.getDecoder().decode(ivParameter));
        encrypt = Base64.getEncoder().encodeToString(aesAlgorithm.encrypt(origin.getBytes(StandardCharsets.UTF_8)));
        decrypt = new String(aesAlgorithm.decrypt(Base64.getDecoder().decode(encrypt)));

        System.out.printf(" origin: %s\nencrypt: %s\ndecrypt: %s\n", origin, encrypt, decrypt);
    }
}
