import org.junit.Assert;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

/**
 * AESAlgorithmTest
 *
 * @author mrcutejacky
 * @version 1.0
 */
public class AESAlgorithmTest {

    final Cipher encryptCipher;
    final Cipher decryptCipher;

    final AESAlgorithm aesAlgorithm;

    public AESAlgorithmTest() throws Exception {

        String specKey = "Dt11y88o17X/XkaaQvAbuA==";
        String key = "C03458ECE748123456789ABCDEFA8A4C";

        encryptCipher = cipher(Cipher.ENCRYPT_MODE, specKey, key);
        decryptCipher = cipher(Cipher.DECRYPT_MODE, specKey, key);

        aesAlgorithm = new AESAlgorithm(AESAlgorithm.toByteArray(key), Base64.getDecoder().decode(specKey));
    }

    public static Cipher cipher(int mode, String specKey, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(AESAlgorithm.toByteArray(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(specKey));
        cipher.init(mode, secretKeySpec, ivParameterSpec);

        return cipher;
    }

    public String encrypt(String text) throws Exception {

        byte[] encrypted = encryptCipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String text) throws Exception {

        byte[] original = decryptCipher.doFinal(Base64.getDecoder().decode(text));
        return new String(original, StandardCharsets.UTF_8);
    }

    public String encryptByAESAlgorithm(String text) {
        byte[] encrypted = aesAlgorithm.encrypt(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decryptByAESAlgorithm(String text) {

        byte[] original = aesAlgorithm.decrypt(Base64.getDecoder().decode(text));
        return new String(original, StandardCharsets.UTF_8);
    }

    @Test
    public void testCipher() throws Exception {

        String text = "AA123456781234";
        String encryptText = encrypt(text);
        String decryptText = decrypt(encryptText);

        Assert.assertEquals(text, decryptText);
    }

    @Test
    public void testAESAlgorithm() {

        String text = "AA123456781234";
        String encryptText = encryptByAESAlgorithm(text);
        String decryptText = decryptByAESAlgorithm(encryptText);

        Assert.assertEquals(text, decryptText);
    }

    @Test
    public void test() throws Exception {

        int testCount = 100000;

        int sCount = 0;
        int eCount = 0;

        Random random = new Random();
        for (int i = 0; i < testCount; i++) {

            StringBuilder randomString = new StringBuilder();
            randomString.append((char) (random.nextInt(26) + 'A'));
            randomString.append((char) (random.nextInt(26) + 'A'));
            randomString.append(String.format("%08d", random.nextInt(100000000)));
            randomString.append(String.format("%04d", random.nextInt(10000)));

            if (encrypt(randomString.toString()).equals(encryptByAESAlgorithm(randomString.toString()))) {
                sCount++;
            } else {
                System.out.println(randomString + ": false.");
                eCount++;
            }
        }

        Assert.assertEquals(sCount + eCount, testCount);
        Assert.assertEquals(sCount, testCount);
    }
}
