package com.k2udacity.baking.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.k2udacity.baking.R;
import com.k2udacity.baking.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private Context context;
    private List<Step> steps;
    private StepOnClickHandler clickHandler;
    private int selectedRowIndex = 0;

    public StepAdapter(Context context, List<Step> steps, StepOnClickHandler clickHandler) {
        this.context = context;
        this.steps = steps;
        this.clickHandler = clickHandler;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_step_item,
                parent,
                false
        );
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StepViewHolder holder, int position) {
        if (steps == null) {
            return;
        }

        Step step = steps.get(position);
        String shortDescriptionFormatted = String.format("%-3s", String.valueOf(position + 1)) + step.getShortDescription();
        holder.textViewStep.setText(shortDescriptionFormatted);
    }

    @Override
    public int getItemCount() {
        if (steps == null) {
            return 0;
        }
        return steps.size();
    }

    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }

    public void setSelectedRowIndex(int selectedRowIndex) {
        this.selectedRowIndex = selectedRowIndex;
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.layout_step)
        ConstraintLayout layoutStep;

        @BindView(R.id.textview_step)
        TextView textViewStep;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickHandler.onClick(getAdapterPosition());
        }
    }

    public interface StepOnClickHandler {
        void onClick(int position);
    }
}
