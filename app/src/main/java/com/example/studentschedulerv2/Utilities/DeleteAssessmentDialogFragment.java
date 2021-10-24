package com.example.studentschedulerv2.Utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeleteAssessmentDialogFragment extends DialogFragment {

    public interface DeleteAssessmentDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    DeleteAssessmentDialogListener listener;

    // instantiate the listener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteAssessmentDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeleteAssessmentDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Assessment")
                .setMessage("Are you sure you want to delete this assessment?")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    listener.onDialogPositiveClick(DeleteAssessmentDialogFragment.this);
                })
                .setNegativeButton("Cancel", null);
        return builder.create();
    }
}
