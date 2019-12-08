package com.abani.concapps.android.nutritionguide.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.abani.concapps.android.nutritionguide.R;
import com.abani.concapps.android.nutritionguide.data.models.Food;
import com.abani.concapps.android.nutritionguide.ui.adapters.FoodAdapter;
import com.abani.concapps.android.nutritionguide.utilities.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodsActivity extends AppCompatActivity implements FoodAdapter.FoodClickListener {

    public static final String DETAIL_CONTENT = "food_detail_key";

    @BindView(R.id.rv_food)
    RecyclerView rvFood;

    private RecyclerView.LayoutManager mLayoutManager;
    private FoodAdapter mAdapter;

    private List<Food> foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foods);
        ButterKnife.bind(this);

        rvFood.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rvFood.setLayoutManager(mLayoutManager);
        mAdapter = new FoodAdapter();
        mAdapter.setFoodClickListener(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.FOOD_DATA) && intent.hasExtra(Constants.NUTRIENT_CLICKED)){
            foods = intent.getParcelableArrayListExtra(Constants.FOOD_DATA);
            mAdapter.setFoods(foods);
            rvFood.setAdapter(mAdapter);

            getSupportActionBar().setTitle(Constants.currentNutrientsMap.get(new Integer(intent.getIntExtra(Constants.NUTRIENT_CLICKED,0))).getName());
        }

    }

    @Override
    public void onFoodItemClicked(int position) {

        Intent intent = new Intent(this, FoodDetailsActivity.class);
        intent.putExtra(DETAIL_CONTENT, foods.get(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        } else {
            startActivity(intent);
        }
    }

}
