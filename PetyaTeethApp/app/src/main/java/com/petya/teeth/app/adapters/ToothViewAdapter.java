package com.petya.teeth.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.petya.teeth.app.R;
import com.petya.teeth.app.ToothDetailsActivity;
import com.petya.teeth.app.models.ToothModel;

import java.util.List;

public abstract class ToothViewAdapter extends RecyclerView.Adapter<ToothViewAdapter.ToothViewHolder> {

    private final Context context;
    private final List<ToothModel> teethList;

    public ToothViewAdapter(Context context, List<ToothModel> teethList) {
        this.context = context;
        this.teethList = teethList;
    }

    @Override
    public void onBindViewHolder(@NonNull ToothViewHolder holder, int position) {
        ToothModel toothModel = teethList.get(position);
        holder.bindToothModel(toothModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ToothDetailsActivity.class);
                intent.putExtra("clickedItemData", toothModel); // pass the clicked item data
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teethList.size();
    }

    public class ToothViewHolder extends RecyclerView.ViewHolder {
        private ImageView toothImageView;
        private TextView toothTextView;

        public ToothViewHolder(@NonNull View itemView) {
            super(itemView);
            toothImageView = itemView.findViewById(R.id.toothImageView);
            toothTextView = itemView.findViewById(R.id.toothTextView);
        }

        public void bindToothModel(ToothModel toothModel) {
            toothImageView.setImageResource(toothModel.getToothImage());
            int toothNumber = toothModel.getToothPosition().getNumber();
            String toothState = context.getString(toothModel.getToothState().getStringResId());
            toothTextView.setText("# " + toothNumber + ": " + toothState);
            int color = ContextCompat.getColor(itemView.getContext(), determineBackground(toothModel));
            itemView.setBackgroundColor(color);
        }

        private int determineBackground(ToothModel toothModel) {
            switch (toothModel.getToothState()){
                case HEALTHY:
                    return R.color.healthy;
                case CROWN:
                    return R.color.crown;
                case FACET:
                    return R.color.facet;
                case BRIDGE:
                    return R.color.bridge;
                case BONDING:
                    return R.color.bonding;
                case FILLING:
                    return R.color.filling;
                case IMPLANT:
                    return R.color.implant;
                case MISSING:
                    return R.color.missing;
                case ROOT_FILLING:
                    return R.color.root_filling;
            }
            return 0;
        }
    }
}
