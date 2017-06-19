package varunbehl.bakingappproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import varunbehl.bakingappproject.MainActivity;
import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.adapter.StepsRecyclerViewAdapter;
import varunbehl.bakingappproject.pojo.BakingData;

public class BakingDataFragment extends Fragment {


    public static final String RECEIPE = "Receipe_Detail";
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
        bundle.putParcelable(RECEIPE, bakingData);
        outState.putBundle(BakingDataFragment.class.getName(), bundle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bakingdata_list, container, false);
        TextView receipeName = (TextView) view.findViewById(R.id.receipe_name);
        TextView servings = (TextView) view.findViewById(R.id.servings_name);
        CardView ingCardView = (CardView) view.findViewById(R.id.ing_card);

        RecyclerView reipe_step_recycle = (RecyclerView) view.findViewById(R.id.reipe_step_recycle);

        Bundle bundle;
        if (savedInstanceState == null) {
            bundle = this.getArguments();
        } else {
            bundle = savedInstanceState.getBundle(BakingDataFragment.class.getName());
        }


        if (bundle != null) {
            bakingData = bundle.getParcelable(RECEIPE);
        }


        if (bakingData != null && bakingData.getName() != null && !bakingData.getName().equals("")) {
            receipeName.setText(getString(R.string.receipe) + bakingData.getName());
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
