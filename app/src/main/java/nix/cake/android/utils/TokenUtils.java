package nix.cake.android.utils;

import android.util.Base64;

import org.json.JSONObject;

import java.util.Date;

public class TokenUtils {

    public static String decodeJWT(String jwt) {
        String[] parts = jwt.split("\\.");
        String payload = parts[1];

        String decodedPayload = new String(Base64.decode(payload, Base64.URL_SAFE));

        return decodedPayload;
    }

    public static boolean isTokenExpired(String token) {
        try {
            String decodedPayload = decodeJWT(token);

            JSONObject payloadObj = new JSONObject(decodedPayload);
            long expTime = payloadObj.getLong("exp");

            Date expirationDate = new Date(expTime * 1000);
            return expirationDate.before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}

