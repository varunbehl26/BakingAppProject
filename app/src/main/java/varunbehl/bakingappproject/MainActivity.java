package varunbehl.bakingappproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import varunbehl.bakingappproject.fragment.BakingDataFragment;
import varunbehl.bakingappproject.fragment.IngredientsDetailFragment;
import varunbehl.bakingappproject.fragment.ReceipeFragment;
import varunbehl.bakingappproject.fragment.StepsDetailFragment;
import varunbehl.bakingappproject.pojo.BakingData;
import varunbehl.bakingappproject.widget.ReceipeWidget;
import varunbehl.bakingappproject.widget.ReceipeWidgetFactory;

public class MainActivity extends AppCompatActivity implements BakingDataFragment.onIngredientClick, BakingDataFragment.onStepsClick {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        BakingData bakingData = intent.getParcelableExtra(ReceipeWidgetFactory.BAKINGDATA);

        boolean mTwoPlane = findViewById(R.id.android_me_linear_layout) != null;

        if (bakingData != null) {
            onIngredientClick(bakingData, true);
        } else if (savedInstanceState == null) {
            if (mTwoPlane) {
                loadReceipes(R.id.master_list_fragment);
            } else {
                loadReceipes(R.id.container);
            }
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

    private void loadReceipes(int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id, new ReceipeFragment());
        transaction.commit();
    }


    public void loadReceipeDetail(BakingData mItem) {
        BakingDataFragment bakingDataFragment = new BakingDataFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(BakingDataFragment.RECEIPE, mItem);
        bakingDataFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(BakingDataFragment.RECEIPE);
        transaction.replace(R.id.container, bakingDataFragment);
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


}
