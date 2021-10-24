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

import com.example.studentschedulerv2.Entities.Term;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.UI.TermDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TermAdapter extends ListAdapter<Term, TermAdapter.TermViewHolder> {

    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termTitleView;
        private final TextView termDatesView;

        private TermViewHolder(@NonNull View itemView) {
            super(itemView);
            termTitleView = itemView.findViewById(R.id.term_title_text_view);
            termDatesView = itemView.findViewById(R.id.term_dates_text_view);

            // onClickListener
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Term selectedTerm = mTerms.get(position);

                Intent intent = new Intent(context, TermDetails.class);
                intent.putExtra("termId", selectedTerm.getId());
                intent.putExtra("title", selectedTerm.getTitle());
                intent.putExtra("startDate", selectedTerm.getStartDate());
                intent.putExtra("endDate", selectedTerm.getEndDate());
                context.startActivity(intent);
            });
        }

    }

    public static class TermDiff extends DiffUtil.ItemCallback<Term> {

        @Override
        public boolean areItemsTheSame(@NonNull Term oldItem, @NonNull Term newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Term oldItem, @NonNull Term newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }

    private ArrayList<Term> mTerms;
    private final Context context;

    public TermAdapter(@NonNull DiffUtil.ItemCallback<Term> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public TermAdapter.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.term_list_item, parent, false);
        return new TermViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermViewHolder holder, int position) {
        if (mTerms != null) {
            Term current = mTerms.get(position);

            String myFormat = "MM/dd/yy";
            String formattedStart = new SimpleDateFormat(myFormat, Locale.US).format(current.getStartDate());
            String formattedEnd = new SimpleDateFormat(myFormat, Locale.US).format(current.getEndDate());

            holder.termTitleView.setText(current.getTitle());
            holder.termDatesView.setText(new StringBuilder(formattedStart).append(" - ").append(formattedEnd));
        } else {
            holder.termTitleView.setText("No term provided.");
        }
    }

    public void setTerms(List<Term> terms) {
        mTerms = (ArrayList<Term>) terms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTerms != null)
            return mTerms.size();
        else return 0;
    }
}
