package com.example.studentschedulerv2.Utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.studentschedulerv2.R;

public class CreateMentorDialogFragment extends DialogFragment {

    private View dialogView;
    private EditText mentorNameEditText;
    private EditText mentorEmailEditText;
    private EditText mentorPhoneEditText;

    public interface CreateMentorDialogListener {
        void onCreateMentorDialogPositiveClick(CreateMentorDialogFragment dialog);
    }

    CreateMentorDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CreateMentorDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement CreateMentorDialogListener");
        }
    }

    public CreateMentorDialogFragment(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        dialogView = inflater.inflate(R.layout.create_mentor_dialog, null);

        mentorNameEditText = (EditText) dialogView.findViewById(R.id.editMentorName);
        mentorEmailEditText = (EditText) dialogView.findViewById(R.id.editMentorEmail);
        mentorPhoneEditText = (EditText) dialogView.findViewById(R.id.editMentorPhone);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Enter Mentor Details")
                .setView(dialogView)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    listener.onCreateMentorDialogPositiveClick(CreateMentorDialogFragment.this);
                })
                .setNegativeButton("Cancel", null);

        return builder.create();
    }

    public EditText getMentorNameEditText() {
        return mentorNameEditText;
    }

    public EditText getMentorEmailEditText() {
        return mentorEmailEditText;
    }

    public EditText getMentorPhoneEditText() {
        return mentorPhoneEditText;
    }
}
