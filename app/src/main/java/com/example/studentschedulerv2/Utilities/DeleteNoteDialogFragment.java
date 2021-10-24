package com.example.studentschedulerv2.Utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeleteNoteDialogFragment extends DialogFragment {

    public interface DeleteNoteDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    DeleteNoteDialogListener listener;

    //instantiate the listener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteNoteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeleteNoteDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    listener.onDialogPositiveClick(DeleteNoteDialogFragment.this);
                })
                .setNegativeButton("Cancel", null);
        return builder.create();
    }

}
