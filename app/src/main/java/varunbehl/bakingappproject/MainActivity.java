package varunbehl.bakingappproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import varunbehl.bakingappproject.fragment.BakingDataFragment;
import varunbehl.bakingappproject.fragment.IngredientsDetailFragment;
import varunbehl.bakingappproject.fragment.ReceipeFragment;
import varunbehl.bakingappproject.fragment.StepsDetailFragment;
import varunbehl.bakingappproject.pojo.BakingData;
import varunbehl.bakingappproject.widget.ReceipeWidgetFactory;

public class MainActivity extends AppCompatActivity implements BakingDataFragment.onIngredientClick, BakingDataFragment.onStepsClick {


    LinearLayout fragmentLayout;
    GridLayout gridMain;
    private boolean mTwoPlane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();

        BakingData bakingData = intent.getParcelableExtra(ReceipeWidgetFactory.BAKINGDATA);

        mTwoPlane = findViewById(R.id.android_me_linear_layout) != null;
        if(mTwoPlane){
            fragmentLayout= (LinearLayout) findViewById(R.id.fragment_layout);
            gridMain= (GridLayout) findViewById(R.id.grid_main);
        }

        if (bakingData != null) {
            onIngredientClick(bakingData, true);
        } else if (savedInstanceState == null) {
            loadReceipes();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setTitle(getString(R.string.app_name));
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    private void loadReceipes() {
        int containerId;
        if (mTwoPlane) {
            containerId = (R.id.grid_main);
            fragmentLayout.setVisibility(View.GONE);
            gridMain.setVisibility(View.VISIBLE);
        } else {
            containerId = (R.id.container);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, new ReceipeFragment());
        transaction.commit();
    }


    public void loadReceipeDetail(BakingData mItem) {
        int containerId;
        if (mTwoPlane) {
            containerId = (R.id.master_list_fragment);
            fragmentLayout.setVisibility(View.VISIBLE);
            gridMain.setVisibility(View.GONE);
        } else {
            containerId = (R.id.container);
        }
        BakingDataFragment bakingDataFragment = new BakingDataFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(BakingDataFragment.RECEIPE, mItem);
        bakingDataFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(BakingDataFragment.RECEIPE);
        transaction.replace(containerId, bakingDataFragment);
        transaction.commit();
    }


    @Override
    public void onIngredientClick(BakingData item, boolean fromWidget) {
        try {
            IngredientsDetailFragment ingredientsFragment = new IngredientsDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(IngredientsDetailFragment.INGREDIENT, item);
            ingredientsFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!fromWidget) {
                transaction.addToBackStack(IngredientsDetailFragment.INGREDIENT);
            }
            transaction.replace(R.id.container, ingredientsFragment);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStepsClick(BakingData item, int position) {
        try {
            StepsDetailFragment stepsDetailFragment = new StepsDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(StepsDetailFragment.STEPS, item);
            bundle.putInt(StepsDetailFragment.POSITION, position);
            stepsDetailFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(StepsDetailFragment.STEPS);
            transaction.replace(R.id.container, stepsDetailFragment);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            loadReceipes();
            super.onBackPressed();
        }
    }
}
