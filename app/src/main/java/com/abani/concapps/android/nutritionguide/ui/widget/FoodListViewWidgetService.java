package com.abani.concapps.android.nutritionguide.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.abani.concapps.android.nutritionguide.R;
import com.abani.concapps.android.nutritionguide.data.database.NutrientDao;
import com.abani.concapps.android.nutritionguide.data.models.Food;
import com.abani.concapps.android.nutritionguide.utilities.InjectorUtils;

import java.util.List;

public class FoodListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String LOG_TAG = ListRemoteViewsFactory.class.getSimpleName();
    private Context mContext;
    private List<Food> mPreferredFoods;

    public ListRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        NutrientDao nutrientDao = InjectorUtils.getLocalNutrientDao(mContext);
        mPreferredFoods = nutrientDao.loadAllPreferredFoodsForWidget();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mPreferredFoods == null) return 0;
        return mPreferredFoods.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (mPreferredFoods == null) return null;

        Log.d(LOG_TAG, "populating widget list");

        Food food = mPreferredFoods.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.food_widget_list_item);

        views.setTextViewText(R.id.text_food, food.getName());

        return views;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
