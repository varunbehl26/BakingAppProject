package varunbehl.bakingappproject.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.pojo.BakingData;

public class ReceipeWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private List<BakingData> bakingDataList;
    public  static String BAKINGDATA ="bakingData";

    public ReceipeWidgetFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        bakingDataList = new ArrayList<>();

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        String jsonLocation = loadJSONFromAsset();
        Type collectionType = new TypeToken<Collection<BakingData>>() {
        }.getType();
        Collection<BakingData> bakingData = new Gson().fromJson(jsonLocation, collectionType);
        bakingDataList = new ArrayList<>();
        assert bakingData != null;
        bakingDataList.addAll(bakingData);
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

    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = context.getAssets().open("baking.json");
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
