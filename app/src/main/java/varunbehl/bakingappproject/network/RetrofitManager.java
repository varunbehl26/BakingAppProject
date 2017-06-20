package varunbehl.bakingappproject.network;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import varunbehl.bakingappproject.pojo.BakingData;


public class RetrofitManager {

    private static DataInterface dataInterface = null;
    private static String API_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private static RetrofitManager retrofitManager;

    private RetrofitManager() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        dataInterface = retrofit.create(DataInterface.class);
    }


    public static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager();
        }
        return retrofitManager;
    }


    public Observable<List<BakingData>> getBakingData() {
        return dataInterface.getBakingData();
    }


}


