package network.retrofit;

import android.content.Context;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;

public class OkHttpProxy {
    public static OkHttpClient getClient(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(getSSLSocketFactory(context, certificates), new CustomTrustManager())
                .hostnameVerifier(getHostnameVerifier())
                .build();
        return okHttpClient;
    }

//    public static SSLSocketFactory getSSLSocketFactory() {
//        SSLSocketFactory ssfFactory = null;
//
//        try {
//            SSLContext sc = SSLContext.getInstance("TLS");
//            sc.init(null, new TrustManager[]{new CustomTrustManager()}, new SecureRandom());
//            ssfFactory = sc.getSocketFactory();
//        } catch (Exception e) {
//        }
//
//        return ssfFactory;
//    }

    public static class CustomTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }




//    public static int[] certificates = {R.raw.mycer}; // 公钥证书
    public static int[] certificates = {};
    protected static SSLSocketFactory getSSLSocketFactory(Context context, int[] certificates) {

        if (context == null) {
            throw new NullPointerException("context == null");
        }

        CertificateFactory certificateFactory;
        SSLContext sslContext = null;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                InputStream certificate = context.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(certificate));

                if (certificate != null) {
                    certificate.close();
                }
            }
            sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

        }catch (Exception e){
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();
    }

    public  static String urls[] = {"host1","host2"};

    public static HostnameVerifier getHostnameVerifier() {

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                boolean verifier = false;
                for (String host : urls) {
                    if (host.equalsIgnoreCase(hostname)) {
                        verifier = true;
                    }
                }
                return verifier;
            }
        };
        return hostnameVerifier;
    }


}
