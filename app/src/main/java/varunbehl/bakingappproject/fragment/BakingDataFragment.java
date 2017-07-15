package varunbehl.bakingappproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.activity.MainActivity;
import varunbehl.bakingappproject.adapter.StepsRecyclerViewAdapter;
import varunbehl.bakingappproject.pojo.BakingData;

public class BakingDataFragment extends Fragment {


    public static final String Recipe = "Recipe_Detail";
    @BindView(R.id.Recipe_name)
    TextView RecipeName;
    @BindView(R.id.servings_name)
    TextView servings;
    @BindView(R.id.ing_card)
    CardView ingCardView;
    @BindView(R.id.reipe_step_recycle)
    RecyclerView reipe_step_recycle;
    private BakingData bakingData;

    public BakingDataFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Recipe, bakingData);
        outState.putBundle(BakingDataFragment.class.getName(), bundle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bakingdata_list, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this, view);

        Bundle bundle;
        if (savedInstanceState == null) {
            bundle = this.getArguments();
        } else {
            bundle = savedInstanceState.getBundle(BakingDataFragment.class.getName());
        }


        if (bundle != null) {
            bakingData = bundle.getParcelable(Recipe);
        }


        if (bakingData != null && TextUtils.isEmpty(bakingData.getName()) && TextUtils.isEmpty(bakingData.getServings())) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(bakingData.getName());
            RecipeName.setText(getString(R.string.Recipe) + bakingData.getName());
            servings.setText(getString(R.string.servings) + bakingData.getServings());
        }
        getActivity().setTitle(bakingData.getName());


        reipe_step_recycle.setLayoutManager(new LinearLayoutManager(getContext()));

        assert bakingData != null;
        reipe_step_recycle.setAdapter(new StepsRecyclerViewAdapter(bakingData, getContext()));


        final BakingData finalBakingData = bakingData;
        ingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onIngredientClick(finalBakingData, false);
            }
        });


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

    public interface onIngredientClick {
        void onIngredientClick(BakingData item, boolean fromWidget);
    }

    public interface onStepsClick {
        void onStepsClick(BakingData item, int position);
    }
}
