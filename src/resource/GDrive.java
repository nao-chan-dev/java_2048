package resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

public class GDrive {

    public static final String PUBLIC_URL = "https://drive.google.com/u/0/uc?confirm=t&id=";
    public static final String API_URL = "https://www.googleapis.com/drive/v3/files/";
    public static final String KEY = new GDriveKey().getValue();
    public static final String TOKEN = new GDriveToken().getToken();

    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";

    public static final List<String> FILE_IDS = List.of(
            "1DIFcssnOdwlTQ-qvqatEOwfUFldFIfJz",
            "1cUwrVbIGUTE-0tCnHNa0ZHIZQrtoPRMH",
            "143iGw2aVES7wbu82HAzNUK_1gmXnf0OA",
            "1J95p9kvU4QP8_A5npxZBfpL19SBqV7yv",
            "1XvD2c-c6t0O7JSqH79nQpprKmGn6MsgF",
            "1IUw0RBulg0wFmt6j77bY3KkV_Jcu1yKR",
            "1zNBrns6t60l5-Ojj3bO3mTo6s-URCNF9",
            "1UZOXtBz4AQSqTedMJvTr4iK3TxlObKgp",

            "1ocygwUWuFMsT-P1wChq0r5-7OavXgl7z",
            "1quJL0EEnm5Bg7pQNYyDrHADKlh4N6CBF",
            "1L_r26Cr8LeSKxVohDtluG0aa_zI2EUTp",
            "1D4SDvWTqXBdEJPuW8uy5CsgiI4AQT9_z",
            "1VE727uIZrV9xH_xobTXbf_YoGcgmZBNv",
            "1mjEmVUJ5iI7XtQl7KyOOh7A2Lh4EYjSx",
            "16dMV2JqDwD1xzuSHtrLBrR4aJ03tnPwj",
            "1XgvlC4TnAiqaopU80ENQN7v-RcE4-SCA",
            "1Q_ec6a4iactFDx1BivIsUbZ2DccCnnBW",
            "1VHXnmE9DaO2gwFAlQ4-DZBqcXs1fTBRR",
            "1M-GS1u8gJayCaKYUvp2FXiEdqrSZWBij",
            "1SgE9jBfcRf633Xg0_shAeGdKmyE1C4vq",
            "1G9dKE6-VEw7YvMjo8_HA3GdmJstGL5Ct",
            "1MF2IwmYxgeVRwx9O7iJcEelMIkDUM2Qh",

            "1j1zGxBKFTBKwjZg53ilgdiV_RGYAt9lT",
            "1qCajQuJpCmf2Qm0BevaOBMN-CFK1h7Ee",
            "10Yj2ggznUvt2cyLL85UmOfXiuHMHJgjv",
            "1vIqMOfVJDDJSqUao7KJc8kWsb724V-8l",
            "1TLdmtesOLEwLOs7xZnketCRPtdyagZKW",
            "1WhAY6jUeXPfmLGkTTsc-Sb9T9BOUWKfs",
            "1h-qGc80rWQQGqY-elMrNiEBpfV_SEWCV"
    );

    private static class GDriveKey implements Serializable {

        @Serial
        private static final long serialVersionUID = 5107015947468084659L;

        private final String value;

        private GDriveKey() {
            try {
                InputStream keyObject = GDrive.class.getClassLoader().getResourceAsStream("data/key.dat");
                ObjectInputStream ois = new ObjectInputStream(keyObject);
                GDriveKey key = (GDriveKey) ois.readObject();
                value = key.value;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        public String getValue() {
            return value;
        }
    }

    private static class GDriveToken implements Serializable {

        @Serial
        private static final long serialVersionUID = 4475699951180022315L;

        private final String privateToken;
        private final String accountId;

        private GDriveToken() {
            try {
                InputStream keyObject = GDrive.class.getClassLoader().getResourceAsStream("data/token.dat");
                ObjectInputStream ois = new ObjectInputStream(keyObject);
                GDriveToken token = (GDriveToken) ois.readObject();
                privateToken = token.privateToken;
                accountId = token.accountId;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        public String getToken() {

            String header = "{\"alg\":\"RS256\",\"typ\":\"JWT\"}";
            String headerEncoded = Base64.getEncoder().encodeToString(header.getBytes());

            long currentUnixTime = Instant.now().getEpochSecond();
            String payload = "{" +
                    "\"iss\":\""+accountId+"\"," +
                    "\"aud\":\""+TOKEN_URL+"\"," +
                    "\"scope\":\"https://www.googleapis.com/auth/drive.readonly\"," +
                    "\"iat\":"+currentUnixTime+"," +
                    "\"exp\":"+(currentUnixTime+3600) +
                    "}";
            String payloadEncoded = Base64.getEncoder().encodeToString(payload.getBytes());
            String headerPayloadEncoded = headerEncoded + "." +payloadEncoded;

            try {
                Signature sig = Signature.getInstance("SHA256withRSA");
                PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateToken));
                sig.initSign(KeyFactory.getInstance("RSA").generatePrivate(pkcs8KeySpec));
                sig.update(headerPayloadEncoded.getBytes());
                byte[] signatureBytes = sig.sign();
                String signatureEncoded = Base64.getEncoder().encodeToString(signatureBytes);
                String assertion = headerPayloadEncoded + "." + signatureEncoded;

                HttpURLConnection http = (HttpURLConnection)new URL(TOKEN_URL).openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.connect();
                http.getOutputStream().write(("{\"grant_type\":\"urn:ietf:params:oauth:grant-type:jwt-bearer\",\"assertion\":\""+assertion+"\"}").getBytes());
                String response = new String(http.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                return response.substring(response.indexOf(":")+2, response.indexOf(",")-1);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | SignatureException | InvalidKeyException |
                     IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
