package varunbehl.bakingappproject.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import varunbehl.bakingappproject.network.RetrofitManager;
import varunbehl.bakingappproject.pojo.BakingData;
import varunbehl.bakingappproject.util.NetworkCommon;

public class RecipeDataService extends IntentService {

    private final RetrofitManager retrofitManager;
    private final Context context;
    private ArrayList<BakingData> bakingDataList;

    public RecipeDataService() {
        super("RecipeDataService");
        retrofitManager = RetrofitManager.getInstance();
        context = this;
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        if (NetworkCommon.isConnected(context)) {
            Observable<List<BakingData>> bakingDataObservable = retrofitManager.getBakingData();
            bakingDataObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new rx.Subscriber<List<BakingData>>() {
                                   @Override
                                   public void onCompleted() {
                                       if (bakingDataList != null) {
                                           Intent intent = new Intent();
                                           Bundle b = new Bundle();
                                           b.putParcelableArrayList("RecipeData", bakingDataList);
                                           intent.putExtras(b);
                                           context.sendBroadcast(intent.setAction("bcNewMessage"));

                                       }
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       e.printStackTrace();
                                   }

                                   @Override
                                   public void onNext(List<BakingData> bakingDatas) {
                                       bakingDataList = (ArrayList<BakingData>) bakingDatas;
                                   }
                               }
                    );


        }
    }

}
