package varunbehl.bakingappproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;

import butterknife.ButterKnife;
import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.fragment.BakingDataFragment;
import varunbehl.bakingappproject.fragment.IngredientsDetailFragment;
import varunbehl.bakingappproject.fragment.RecipeFragment;
import varunbehl.bakingappproject.fragment.StepsDetailFragment;
import varunbehl.bakingappproject.pojo.BakingData;
import varunbehl.bakingappproject.widget.RecipeDataService;
import varunbehl.bakingappproject.widget.RecipeWidgetFactory;

public class MainActivity extends AppCompatActivity implements BakingDataFragment.onIngredientClick, BakingDataFragment.onStepsClick {

    LinearLayout fragmentLayout;
    GridLayout gridMain;
    private boolean mTwoPlane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ButterKnife.bind(this);
            setContentView(R.layout.activity_main);
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setHomeButtonEnabled(true);

            Intent intent = getIntent();
            BakingData bakingData = intent.getParcelableExtra(RecipeWidgetFactory.BAKINGDATA);

            mTwoPlane = findViewById(R.id.android_me_linear_layout) != null;
            if (mTwoPlane) {
                fragmentLayout = (LinearLayout) findViewById(R.id.fragment_layout);
                gridMain = (GridLayout) findViewById(R.id.grid_main);
            }
            Fresco.initialize(this);

            if (bakingData != null) {
                onIngredientClick(bakingData, true);
            } else if (savedInstanceState == null) {
                loadRecipes();
            }
            Intent msgIntent = new Intent(this, RecipeDataService.class);
            startService(msgIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    private void loadRecipes() {
        int containerId;
        if (mTwoPlane) {
            containerId = (R.id.grid_main);
            fragmentLayout.setVisibility(View.GONE);
            gridMain.setVisibility(View.VISIBLE);
        } else {
            containerId = (R.id.container);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, new RecipeFragment());
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadRecipeDetail(BakingData mItem) {
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
        bundle.putParcelable(BakingDataFragment.Recipe, mItem);
        bakingDataFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(BakingDataFragment.Recipe);
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
        if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
