package com.petya.teeth.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.petya.teeth.app.R;
import com.petya.teeth.app.models.ToothModel;

import java.util.List;

public class UpperToothViewAdapter extends ToothViewAdapter{
    public UpperToothViewAdapter(Context context, List<ToothModel> teethList) {
        super(context, teethList);
    }

    @NonNull
    @Override
    public ToothViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tooth_upper, parent, false);
        return new ToothViewHolder(view);
    }
}
