package varunbehl.bakingappproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.adapter.ReceipeRecyclerViewAdapter;
import varunbehl.bakingappproject.network.MessageEvent;
import varunbehl.bakingappproject.network.RetrofitManager;
import varunbehl.bakingappproject.pojo.BakingData;


public class ReceipeFragment extends Fragment {

    private List<BakingData> bakingDataList;
    private EventBus eventbus;
    private RetrofitManager retrofitManager;
    private RecyclerView recyclerView;

    public ReceipeFragment() {
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipe_fragemnt, container, false);
        Fresco.initialize(getActivity());
        retrofitManager = RetrofitManager.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.recipe_recycler_view);
        eventbus = EventBus.getDefault();
        new LoadDataThread().start();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        eventbus.register(this);
    }

    @Override
    public void onStop() {
        eventbus.unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        ReceipeRecyclerViewAdapter receipeAdapter = new ReceipeRecyclerViewAdapter(bakingDataList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(receipeAdapter);
        receipeAdapter.notifyDataSetChanged();
    }


    private class LoadDataThread extends Thread {

        public LoadDataThread() {
        }

        @Override
        public void run() {
            super.run();

            Observable<List<BakingData>> bakingDataObservable = retrofitManager.getBakingData();
            bakingDataObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new rx.Subscriber<List<BakingData>>() {
                                   @Override
                                   public void onCompleted() {
                                       if (bakingDataList != null) {
                                           eventbus.post(new MessageEvent(1));
                                       }
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       Log.v("Exception", e.toString());
                                   }

                                   @Override
                                   public void onNext(List<BakingData> bakingDatas) {
                                       bakingDataList = bakingDatas;
                                   }
                               }
                    );
        }
    }
}
