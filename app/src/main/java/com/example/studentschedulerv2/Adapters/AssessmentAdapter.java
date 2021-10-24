package com.example.studentschedulerv2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentschedulerv2.Entities.Assessment;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.UI.AssessmentDetails;

import java.util.List;
import java.util.Locale;

public class AssessmentAdapter extends ListAdapter<Assessment, AssessmentAdapter.AssessmentViewHolder> {

    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentTitleView;
        private final TextView assessmentDatesView;
        private final TextView assessmentTypeView;

        private AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            assessmentTitleView = itemView.findViewById(R.id.assessment_title_textview);
            assessmentDatesView = itemView.findViewById(R.id.assessment_dates_textview);
            assessmentTypeView = itemView.findViewById(R.id.assessment_type_textview);

            // onClickListener here
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Assessment selectedAssessment = mAssessments.get(position);

                Intent intent = new Intent(context, AssessmentDetails.class);
                intent.putExtra("assessmentId", selectedAssessment.getId());
                intent.putExtra("title", selectedAssessment.getTitle());
                intent.putExtra("startDate", selectedAssessment.getStartDate());
                intent.putExtra("endDate", selectedAssessment.getEndDate());
                intent.putExtra("type", selectedAssessment.getType());
                intent.putExtra("courseId", selectedAssessment.getCourseId());
                context.startActivity(intent);
            });
        }
    }

    public static class AssessmentDiff extends DiffUtil.ItemCallback<Assessment> {

        @Override
        public boolean areItemsTheSame(@NonNull Assessment oldItem, @NonNull Assessment newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Assessment oldItem, @NonNull Assessment newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }

    private List<Assessment> mAssessments;
    private final Context context;

    public AssessmentAdapter(@NonNull DiffUtil.ItemCallback<Assessment> diffCallBack, Context context) {
        super(diffCallBack);
        this.context = context;
    }

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_list_item, parent, false);
        return new AssessmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentViewHolder holder, int position) {
        if (mAssessments != null) {
            Assessment current = mAssessments.get(position);

            String myFormat = "MM/dd/yy";
            String formattedStart = new SimpleDateFormat(myFormat, Locale.US).format(current.getStartDate());
            String formattedEnd = new SimpleDateFormat(myFormat, Locale.US).format(current.getEndDate());

            holder.assessmentTitleView.setText(current.getTitle());
            holder.assessmentDatesView.setText(new StringBuilder(formattedStart).append(" - ").append(formattedEnd));
            holder.assessmentTypeView.setText(current.getType().toString());
        } else {
            holder.assessmentTitleView.setText("No assessments.");
        }
    }

    public void setAssessments(List<Assessment> assessments) {
        mAssessments = assessments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mAssessments != null) {
            return mAssessments.size();
        } else return 0;
    }
}
