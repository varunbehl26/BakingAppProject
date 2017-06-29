package varunbehl.bakingappproject.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;


public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeWidgetFactory(getApplicationContext(), intent);
    }
}
