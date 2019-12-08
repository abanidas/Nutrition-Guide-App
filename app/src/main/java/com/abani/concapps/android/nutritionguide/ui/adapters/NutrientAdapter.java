package com.abani.concapps.android.nutritionguide.ui.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abani.concapps.android.nutritionguide.R;
import com.abani.concapps.android.nutritionguide.data.models.Nutrient;
import com.abani.concapps.android.nutritionguide.utilities.InjectorUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NutrientAdapter extends RecyclerView.Adapter<NutrientAdapter.NutrientViewHolder> {

    private List<Nutrient> nutrients = new ArrayList<>();
    private NutrientClickListener nutrientClickListener;

    public interface NutrientClickListener{
        void onItemClicked(int position);
    }

    public static class NutrientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_nutrient_name)
        TextView tvNutrientName;
        @BindView(R.id.nutrient_card)
        CardView nutrientCard;
        @BindView(R.id.ll_nutrient_bg)
        LinearLayout llNutrientBg;
        @BindView(R.id.image_nutrient)
        ImageView imgNutrient;

        public NutrientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public NutrientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutrient_list_item, parent, false);
        return new NutrientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NutrientViewHolder holder, int position) {
        holder.tvNutrientName.setText(nutrients.get(position).getName());

        int drawableR = InjectorUtils.getRandomDrawableResource();
        if (drawableR == R.drawable.ic_gradient_3 || drawableR == R.drawable.ic_gradient_4){
            holder.tvNutrientName.setTextColor(Color.WHITE);
        }
        holder.llNutrientBg.setBackgroundResource(drawableR);
        holder.imgNutrient.setImageResource(InjectorUtils.getImageResourceByNutrient(nutrients.get(position)));
        holder.nutrientCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nutrientClickListener.onItemClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return nutrients.size();
    }

    public void setNutrients(List<Nutrient> nutrients) {
        this.nutrients = nutrients;
        notifyDataSetChanged();
    }

    public void setNutrientClickListener(NutrientClickListener nutrientClickListener) {
        this.nutrientClickListener = nutrientClickListener;
    }
}
