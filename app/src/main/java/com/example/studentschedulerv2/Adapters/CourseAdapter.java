package com.example.studentschedulerv2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentschedulerv2.Entities.Course;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.UI.CourseDetails;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CourseAdapter extends ListAdapter<Course, CourseAdapter.CourseViewHolder> {

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseTitleView;
        private final TextView courseDatesView;
        private final TextView courseStatusView;
        private final TextView courseMentorView;

        private CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTitleView = itemView.findViewById(R.id.course_title_text_view);
            courseDatesView = itemView.findViewById(R.id.course_dates_text_view);
            courseStatusView = itemView.findViewById(R.id.course_status_text_view);
            courseMentorView = itemView.findViewById(R.id.course_mentor_text_view);

            // onClickListener goes below
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Course selectedCourse = mCourses.get(position);

                Intent intent = new Intent(context, CourseDetails.class);
                intent.putExtra("courseId", selectedCourse.getId());
                intent.putExtra("title", selectedCourse.getTitle());
                intent.putExtra("startDate", selectedCourse.getStartDate());
                intent.putExtra("endDate", selectedCourse.getEndDate());
                intent.putExtra("status", selectedCourse.getStatus());
                intent.putExtra("mentor", selectedCourse.getMentor());
                intent.putExtra("termId", selectedCourse.getTermId());
                context.startActivity(intent);
            });
        }
    }

    public static class CourseDiff extends DiffUtil.ItemCallback<Course> {

        @Override
        public boolean areItemsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Course oldItem, @NonNull Course newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }

    private List<Course> mCourses;
    private final Context context;

    public CourseAdapter(@NonNull DiffUtil.ItemCallback<Course> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_list_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        if (mCourses != null) {
            Course current = mCourses.get(position);

            String myFormat = "MM/dd/yy";
            String formattedStart = new SimpleDateFormat(myFormat, Locale.US).format(current.getStartDate());
            String formattedEnd = new SimpleDateFormat(myFormat, Locale.US).format(current.getEndDate());

            holder.courseTitleView.setText(current.getTitle());
            holder.courseDatesView.setText(new StringBuilder(formattedStart).append(" - ").append(formattedEnd));
            holder.courseStatusView.setText(current.getStatus().toString());
            holder.courseMentorView.setText(current.getMentor().toString());

        } else {
            holder.courseTitleView.setText("No course provided.");
        }
    }

    public void setCourses(List<Course> courses) {
        mCourses = courses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCourses != null)
            return mCourses.size();
        else return 0;
    }
}
