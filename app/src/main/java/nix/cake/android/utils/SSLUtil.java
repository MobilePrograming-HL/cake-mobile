package nix.cake.android.utils;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class SSLUtil {
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Tạo một TrustManager mà không xác thực chứng chỉ
            TrustManager[] trustAllCertificates = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0]; // Trả về mảng trống thay vì null
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Cài đặt một SSLContext không xác thực
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

            // Cài đặt OkHttpClient với SSLContext tùy chỉnh
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCertificates[0]);
            builder.hostnameVerifier((hostname, session) -> true); // Bỏ qua xác thực hostname

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
