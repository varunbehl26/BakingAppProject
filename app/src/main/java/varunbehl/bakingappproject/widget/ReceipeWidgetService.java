package varunbehl.bakingappproject.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;


public class ReceipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ReceipeWidgetFactory(getApplicationContext(), intent);
    }
}
