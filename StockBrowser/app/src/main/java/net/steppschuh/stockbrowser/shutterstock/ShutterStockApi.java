package net.steppschuh.stockbrowser.shutterstock;

import android.util.Base64;
import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.steppschuh.stockbrowser.StockBrowser;

import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidParameterException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class ShutterStockApi {

    public static final String TAG = StockBrowser.TAG + "." + ShutterStockApi.class.getSimpleName();

    public static final String API_BASE_URL = "https://api.shutterstock.com";

    // please don't blame me for making this public
    public static final String API_CLIENT_ID = "b4047938138e9864c698";
    public static final String API_CLIENT_SECRET = "786ad86b618d44e78a8c82880ca4b74fa047dd06";

    Retrofit retrofit;
    ApiEndpointInterface endpoints;

    public ShutterStockApi() {
        Log.d(TAG, "Initializing " + TAG);

        // Create the HTTP client
        OkHttpClient client = createHttpClient(API_CLIENT_ID, API_CLIENT_SECRET);

        // Build a retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        // Create an API representation
        endpoints = retrofit.create(ApiEndpointInterface.class);
    }

    public static OkHttpClient createHttpClient(final String clientId, final String clientSecret) throws InvalidParameterException {
        if (clientId == null || clientSecret == null) {
            throw new InvalidParameterException("Invalid authentication credentials");
        }

        // Define the interceptor, add authentication header
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", generateBasicAuthHeader(clientId, clientSecret))
                        .build();

                return chain.proceed(newRequest);
            }
        };

        // Create the client
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(interceptor);

        return client;
    }

    /**
     * Encodes credentials for basic authentication
     */
    public static String generateBasicAuthHeader(String username, String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

    /**
     * Checks if the API is reachable. May returns false if ShutterStock is down
     * or the device is not connected to the internet.
     */
    public static boolean isReachable() {
        try {
            InetAddress ipAddr = InetAddress.getByName(API_BASE_URL);
            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public ApiEndpointInterface getEndpoints() {
        return endpoints;
    }

}
