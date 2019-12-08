package com.abani.concapps.android.nutritionguide.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abani.concapps.android.nutritionguide.R;
import com.abani.concapps.android.nutritionguide.data.models.Food;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<Food> foods;
    private FoodClickListener foodClickListener;

    public interface FoodClickListener {
        void onFoodItemClicked(int position);
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_food_item)
        TextView tvFoodItem;

        public FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, parent, false);
        FoodViewHolder viewHolder = new FoodViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder holder, int position) {
        holder.tvFoodItem.setText(foods.get(position).getName());
        holder.tvFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodClickListener.onFoodItemClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }

    public void setFoodClickListener(FoodClickListener foodClickListener) {
        this.foodClickListener = foodClickListener;
    }
}
