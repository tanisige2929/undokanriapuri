package com.example.ExerciseApplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class CalendarDialogFragment extends DialogFragment {

    private EditText editText;
    private Context context;

    CalendarDialogFragment(Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        editText = new EditText(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.daialogtitle1);
        builder.setMessage(R.string.dialogmessage);
        builder.setView(editText);
        builder.setPositiveButton(R.string.dialogok, new DialogButtonClickListener());
        builder.setNegativeButton(R.string.dialogcancel, new DialogButtonClickListener());
        AlertDialog dialog = builder.create();
        return dialog;
    }

    private class DialogButtonClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            String msg = "";
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    msg += "a";
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    msg += "b";
                    break;
            }
            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        }
    }
}
