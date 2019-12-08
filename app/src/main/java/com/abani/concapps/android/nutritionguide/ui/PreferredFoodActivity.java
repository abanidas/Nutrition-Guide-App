package com.abani.concapps.android.nutritionguide.ui;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.abani.concapps.android.nutritionguide.R;
import com.abani.concapps.android.nutritionguide.data.models.Food;
import com.abani.concapps.android.nutritionguide.ui.adapters.FoodAdapter;
import com.abani.concapps.android.nutritionguide.utilities.InjectorUtils;
import com.abani.concapps.android.nutritionguide.viewmodels.PreferredFoodListViewModel;
import com.abani.concapps.android.nutritionguide.viewmodels.PreferredFoodListViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.abani.concapps.android.nutritionguide.ui.FoodsActivity.DETAIL_CONTENT;

public class PreferredFoodActivity extends AppCompatActivity implements FoodAdapter.FoodClickListener {

    private static final String LOG_TAG = PreferredFoodActivity.class.getSimpleName();

    @BindView(R.id.rv_preferred_food)
    RecyclerView rvPreferredFood;

    private RecyclerView.LayoutManager mLayoutManager;
    private FoodAdapter mAdapter;

    private List<Food> preferredFoods;

    private PreferredFoodListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_food);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(R.string.your_foods_title);

        rvPreferredFood.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rvPreferredFood.setLayoutManager(mLayoutManager);
        mAdapter = new FoodAdapter();
        preferredFoods = new ArrayList<>();
        mAdapter.setFoods(preferredFoods);
        rvPreferredFood.setAdapter(mAdapter);
        mAdapter.setFoodClickListener(this);

        //lood preferred foods
        PreferredFoodListViewModelFactory viewModelFactory = InjectorUtils.getPreferredFoodViewModelFactory(this.getApplicationContext());
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(PreferredFoodListViewModel.class);

        mViewModel.getPreferredFoods().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable List<Food> foods) {
                preferredFoods.clear();
                preferredFoods.addAll(foods);
                mAdapter.setFoods(foods);
            }
        });
    }

    @Override
    public void onFoodItemClicked(int position) {
        Intent intent = new Intent(this, FoodDetailsActivity.class);
        intent.putExtra(DETAIL_CONTENT, preferredFoods.get(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }
}
