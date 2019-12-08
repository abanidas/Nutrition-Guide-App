package com.abani.concapps.android.nutritionguide.ui;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.abani.concapps.android.nutritionguide.AppExecutors;
import com.abani.concapps.android.nutritionguide.R;
import com.abani.concapps.android.nutritionguide.data.database.AppDatabase;
import com.abani.concapps.android.nutritionguide.data.database.NutrientDao;
import com.abani.concapps.android.nutritionguide.data.models.Food;
import com.abani.concapps.android.nutritionguide.ui.widget.PreferedNutrientWidgetProvider;
import com.abani.concapps.android.nutritionguide.utilities.FormatterUtils;
import com.abani.concapps.android.nutritionguide.utilities.InjectorUtils;
import com.abani.concapps.android.nutritionguide.viewmodels.AddFoodViewModel;
import com.abani.concapps.android.nutritionguide.viewmodels.AddFoodViewModelFactory;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = FoodDetailsActivity.class.getSimpleName();

    @BindView(R.id.toolbar_food_details)
    Toolbar toolbarFood;
    @BindView(R.id.appBar)
    AppBarLayout appBarLayout;
    @BindView(R.id.btn_add_preferred)
    FloatingActionButton btnAddToPreferred;
    @BindView(R.id.img_food_item)
    ImageView imgFoodItem;

    @BindView(R.id.txt_food_description)
    TextView txtFoodDescription;
    @BindView(R.id.txt_food_sci_name)
    TextView txtSciName;
    @BindView(R.id.txt_nutrient_quantities)
    TextView txtNutrientQuantities;

    @BindView(R.id.card_know_more)
    CardView cardKnowMore;

    private Food mFood;
    private boolean isInYourFood = false;

    private NutrientDao mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbarFood);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //defining animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }

        Intent detailIntent = getIntent();
        if (detailIntent != null && detailIntent.hasExtra(FoodsActivity.DETAIL_CONTENT)){
            mFood = detailIntent.getParcelableExtra(FoodsActivity.DETAIL_CONTENT);

            populateViews();

        }

        mDao = InjectorUtils.getLocalNutrientDao(this.getApplicationContext());

        setupListener();

    }

    private void populateViews() {
        getSupportActionBar().setTitle(mFood.getName());

        txtFoodDescription.setText(mFood.getDescription());
        String scName = mFood.getScientificName();
        if (scName == null || scName.trim().equals("")){
            scName = "N/A";
        }
        txtSciName.setText(scName);
        txtNutrientQuantities.setText(FormatterUtils.formatNutrientQuantities(mFood.getQuantityOfNutrients()));

        if (!mFood.getImageUrl().isEmpty()) {
            Picasso.get()
                    .load(mFood.getImageUrl())
                    .placeholder(R.drawable.ic_theme_background)
                    .placeholder(R.drawable.ic_theme_background)
                    .into(imgFoodItem);
        }

        AddFoodViewModelFactory viewModelFactory = new AddFoodViewModelFactory(AppDatabase.getInstance(this.getApplicationContext()), mFood.getId());
        AddFoodViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddFoodViewModel.class);

        viewModel.getFoodId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer id) {

                if (mFood.getId().equals(id)){
                    isInYourFood = true;
                    btnAddToPreferred.setImageResource(R.drawable.ic_remove_food);
                } else {
                    isInYourFood = false;
                    btnAddToPreferred.setImageResource(R.drawable.ic_add_food);
                }
            }
        });
    }

    private void setupListener() {
        btnAddToPreferred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        if (!isInYourFood) {
                            mDao.insertPreferredFood(mFood);
                            Snackbar.make(v, "Added to your food list", Snackbar.LENGTH_SHORT).show();
                        } else {
                            mDao.deletePreferredFood(mFood);
                            Snackbar.make(v, "Removed from your food list", Snackbar.LENGTH_SHORT).show();
                        }


                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(FoodDetailsActivity.this);
                        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                                new ComponentName(FoodDetailsActivity.this, PreferedNutrientWidgetProvider.class));
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_widget);

                    }
                });
            }
        });

        //open url to know more
        cardKnowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW);
                openBrowserIntent.setData(Uri.parse(mFood.getWikiUrl()));
                startActivity(openBrowserIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
