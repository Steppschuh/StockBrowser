package net.steppschuh.stockbrowser.shutterstock;

import android.util.Base64;
import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.steppschuh.stockbrowser.StockBrowser;

import java.io.IOException;
import java.security.InvalidParameterException;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class ShutterStockApi {

    public static final String TAG = StockBrowser.class.getSimpleName();

    public static final String API_BASE_URL = "https://api.shutterstock.com";
    public static final String API_CLIENT_ID = "b4047938138e9864c698";
    public static final String API_CLIENT_SECRET = "786ad86b618d44e78a8c82880ca4b74fa047dd06";

    Retrofit retrofit;
    ApiEndpointInterface endpoints;

    public ShutterStockApi() {
        Log.d(TAG, "Initializing " + TAG);

        // Create the HttpClient
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

    private OkHttpClient createHttpClient(String clientId, String clientSecret) throws InvalidParameterException {
        if (clientId == null || clientSecret == null) {
            throw new InvalidParameterException("Invalid authentication credentials");
        }

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new LoggingInterceptor());

        // Set basic authentication
        String credentials = clientId + ":" + clientSecret;
        final String basicAuthentication = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        // Define the interceptor, add authentication header
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", basicAuthentication)
                        .build();

                return chain.proceed(newRequest);
            }
        };
        client.interceptors().add(interceptor);

        return client;
    }

    public ApiEndpointInterface getEndpoints() {
        return endpoints;
    }
}
