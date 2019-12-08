package com.abani.concapps.android.nutritionguide.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.abani.concapps.android.nutritionguide.R;
import com.abani.concapps.android.nutritionguide.data.models.Food;
import com.abani.concapps.android.nutritionguide.data.models.Nutrient;
import com.abani.concapps.android.nutritionguide.data.sync.NutrientSyncUtils;
import com.abani.concapps.android.nutritionguide.ui.adapters.NutrientAdapter;
import com.abani.concapps.android.nutritionguide.utilities.Constants;
import com.abani.concapps.android.nutritionguide.utilities.FormatterUtils;
import com.abani.concapps.android.nutritionguide.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NutrientAdapter.NutrientClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_nutrient)
    RecyclerView rvNutrient;
    @BindView(R.id.textView)
    TextView textViewTitle;

    private NutrientAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Nutrient> nutrients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        rvNutrient.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        rvNutrient.setLayoutManager(mLayoutManager);

        mAdapter = new NutrientAdapter();
        mAdapter.setNutrientClickListener(this);
        mAdapter.setNutrients(nutrients);
        rvNutrient.setAdapter(mAdapter);

        //load nutrients data
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getNutrients().observe(this, new Observer<List<Nutrient>>() {
            @Override
            public void onChanged(@Nullable final List<Nutrient> nutrients) {
                Log.d(LOG_TAG, "Getting and updating data from Livedata in view model");
                MainActivity.this.nutrients = nutrients;
                mAdapter.setNutrients(nutrients);
                if (textViewTitle.getVisibility() != View.VISIBLE) {
                    textViewTitle.setVisibility(View.VISIBLE);
                }
                FormatterUtils.formatNutrientWithIdToMap(nutrients);

            }
        });

        //schedule sync with the server
        NutrientSyncUtils.scheduleNutrientsSync(this);

    }

    @Override
    public void onItemClicked(int position) {
        Intent foodIntent = new Intent(this, FoodsActivity.class);
        foodIntent.putParcelableArrayListExtra(Constants.FOOD_DATA, (ArrayList<Food>) nutrients.get(position).getFoods());
        foodIntent.putExtra(Constants.NUTRIENT_CLICKED, nutrients.get(position).getId());
        startActivity(foodIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_your_food:
                startActivity(new Intent(this, PreferredFoodActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
