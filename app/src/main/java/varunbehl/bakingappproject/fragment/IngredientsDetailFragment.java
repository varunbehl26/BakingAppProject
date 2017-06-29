package varunbehl.bakingappproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.activity.MainActivity;
import varunbehl.bakingappproject.adapter.IngredientsRecyclerViewAdapter;
import varunbehl.bakingappproject.pojo.BakingData;


public class IngredientsDetailFragment extends Fragment {

    public static final String INGREDIENT = "ingredient";
    private BakingData bakingData;

    public IngredientsDetailFragment() {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        bundle.putParcelable(INGREDIENT, bakingData);
        outState.putBundle(IngredientsDetailFragment.class.getName(), bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bakingdataingredients_list, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle;
        if (savedInstanceState == null) {
            bundle = this.getArguments();
        } else {
            bundle = savedInstanceState.getBundle(IngredientsDetailFragment.class.getName());
        }

        if (bundle != null) {
            bakingData = bundle.getParcelable(INGREDIENT);
        }

        getActivity().setTitle(bakingData.getName());

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if(bakingData!=null)
            recyclerView.setAdapter(new IngredientsRecyclerViewAdapter(context,bakingData.getIngredients()));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
