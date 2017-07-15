package varunbehl.bakingappproject.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.activity.MainActivity;
import varunbehl.bakingappproject.adapter.RecipeRecyclerViewAdapter;
import varunbehl.bakingappproject.pojo.BakingData;


public class RecipeFragment extends Fragment {

    public static final String Recipe = "RecipeFragment";
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.recipe_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private List<BakingData> bakingDataList;
    private SharedPreferences preferences;
    private RecipeRecyclerViewAdapter recyclerViewAdapter;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Bundle b = intent.getExtras();
            bakingDataList = b.getParcelableArrayList("RecipeData");
            if (bakingDataList != null && bakingDataList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            } else {
                SharedPreferences.Editor prefsEditor = preferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(bakingDataList);
                prefsEditor.putString("RecipeObject", json);
                prefsEditor.apply();

                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                recyclerViewAdapter = new RecipeRecyclerViewAdapter(bakingDataList, getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }
    };

    public RecipeFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle b = new Bundle();
        b.putParcelableArrayList("RecipeData", (ArrayList<? extends Parcelable>) bakingDataList);
        outState.putBundle("bundle", b);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mMessageReceiver);

    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mMessageReceiver, new IntentFilter("bcNewMessage"));
        if (bakingDataList == null) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_fragemnt, container, false);
        try {
            preferences = getActivity().getSharedPreferences("Recipe", Context.MODE_PRIVATE);
            ButterKnife.bind(this, view);
            progressBar.setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));
            recyclerViewAdapter = new RecipeRecyclerViewAdapter(bakingDataList, getContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(recyclerViewAdapter);
            if (savedInstanceState != null) {
                Bundle b = savedInstanceState.getBundle("bundle");
                if (b != null) {
                    bakingDataList = b.getParcelableArrayList("RecipeData");
                }
                if (bakingDataList == null) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
