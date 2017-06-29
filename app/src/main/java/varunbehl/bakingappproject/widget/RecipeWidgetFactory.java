package varunbehl.bakingappproject.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.pojo.BakingData;

public class RecipeWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    public static String BAKINGDATA = "bakingData";
    private Context context = null;
    private List<BakingData> bakingDataList;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Bundle b = intent.getExtras();
            bakingDataList = b.getParcelableArrayList("RecipeData");
        }
    };

    public RecipeWidgetFactory() {
    }


    public RecipeWidgetFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        bakingDataList = new ArrayList<>();
        context.registerReceiver(mMessageReceiver, new IntentFilter());
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        context.registerReceiver(
                mMessageReceiver, new IntentFilter("bcNewMessage"));
        Intent msgIntent = new Intent(context, RecipeDataService.class);
        context.startService(msgIntent);
    }

    @Override
    public void onDestroy() {
        bakingDataList.clear();
    }

    @Override
    public int getCount() {
        return bakingDataList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_bakingdataingredients);
        final BakingData bakingData = bakingDataList.get(position);
        rv.setTextViewText(R.id.ingredient, bakingData.getName());

        try {
            rv.setImageViewBitmap(R.id.icon, BitmapFactory.decodeStream(new URL(bakingDataList.get(position).getImage()).openConnection().getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        rv.setTextViewText(R.id.name, bakingDataList.get(position).getName());
        rv.setTextViewText(R.id.servings, context.getString(R.string.servings) + " " + bakingDataList.get(position).getServings());
        for (int i = 0; i < bakingDataList.get(position).getIngredients().size(); i++) {
            RemoteViews ing = new RemoteViews(context.getPackageName(), R.layout.fragment_bakingdataingredients);
            ing.setTextViewText(R.id.ingredient, bakingDataList.get(position).getIngredients().get(i).getIngredient());
            ing.setTextViewText(R.id.measure, bakingDataList.get(position).getIngredients().get(i).getMeasure());
            ing.setTextViewText(R.id.quantity, bakingDataList.get(position).getIngredients().get(i).getQuantity() + "");
            rv.addView(R.id.ingerdient_list, ing);
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(BAKINGDATA, bakingData);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(bundle);
        rv.setOnClickFillInIntent(R.id.ingredient, fillInIntent);


        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }


}
