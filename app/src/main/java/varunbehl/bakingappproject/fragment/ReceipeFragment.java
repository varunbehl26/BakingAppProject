package varunbehl.bakingappproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.adapter.ReceipeRecyclerViewAdapter;
import varunbehl.bakingappproject.pojo.BakingData;


public class ReceipeFragment extends Fragment {

    private ArrayList<BakingData> bakingDataList;
    private static final String  RECEIPE = " Receipe";
    private static final String  BAKING_JSON_FILE = "baking.json";

    public ReceipeFragment() {
        // Required empty public constructor
        setRetainInstance(true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(RECEIPE, bakingDataList);
        outState.putBundle(ReceipeFragment.class.getName(), bundle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipe_fragemnt, container, false);

        String jsonLocation = loadJSONFromAsset();


        Type collectionType = new TypeToken<Collection<BakingData>>() {
        }.getType();
        Collection<BakingData> bakingDataCollection = new Gson().fromJson(jsonLocation, collectionType);
        bakingDataList = new ArrayList<>();
        assert bakingDataCollection != null;
        bakingDataList.addAll(bakingDataCollection);

        Bundle bundle;
        if (savedInstanceState == null) {
            bundle = this.getArguments();
        } else {
            bundle = savedInstanceState.getBundle(ReceipeFragment.class.getName());
        }

        if (bundle != null) {
            bakingDataList = bundle.getParcelableArrayList(RECEIPE);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recipe_recycler_view);
        ReceipeRecyclerViewAdapter receipeAdapter = new ReceipeRecyclerViewAdapter(bakingDataList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(receipeAdapter);
        receipeAdapter.notifyDataSetChanged();
        return view;
    }

    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getActivity().getAssets().open(BAKING_JSON_FILE);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}
