package varunbehl.bakingappproject.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import varunbehl.bakingappproject.R;
import varunbehl.bakingappproject.pojo.BakingData;

public class RecipeWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    public static String BAKINGDATA = "bakingData";
    private Context context = null;
    private List<BakingData> bakingDataList;
    private Call call;

    public RecipeWidgetFactory() {
    }


    public RecipeWidgetFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        bakingDataList = new ArrayList<>();

    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        try {
            new FetchBakingDatasTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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


    public class FetchBakingDatasTask extends AsyncTask<Void, Void, ArrayList<BakingData>> {

        @Override
        protected ArrayList<BakingData> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                Uri builtUri = Uri.parse(context.getString(R.string.URL))
                        .buildUpon()
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                JSONArray movieArray = new JSONArray(buffer.toString());
                bakingDataList = new ArrayList<>();
                for (int i = 0; i < movieArray.length(); i++) {
                    bakingDataList.add(new BakingData(movieArray.getJSONObject(i)));
                    Log.e("name: ", bakingDataList.get(i).getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return (ArrayList<BakingData>) bakingDataList;
            }
        }
    }
}


