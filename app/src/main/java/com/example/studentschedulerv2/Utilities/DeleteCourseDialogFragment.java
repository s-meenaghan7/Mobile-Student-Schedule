package com.example.studentschedulerv2.Utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.studentschedulerv2.R;

public class DeleteCourseDialogFragment extends DialogFragment {

    public interface DeleteCourseDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    DeleteCourseDialogListener listener;

    // instantiate the listener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteCourseDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeleteCourseDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Course")
                .setMessage(R.string.delete_course_message)
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    listener.onDialogPositiveClick(DeleteCourseDialogFragment.this);
                })
                .setNegativeButton("Cancel", null);
        return builder.create();
    }

}
