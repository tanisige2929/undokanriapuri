package com.example.ExerciseApplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class CalendarDialogFragment2 extends DialogFragment {

    private EditText editText;
    //private EditText editText2;
    private Context context;
    private Bundle bundle;

    CalendarDialogFragment2(Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //bundle = savedInstanceState;
        editText = new EditText(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialogtitle2);
        builder.setMessage(R.string.dialogmessage2);
        builder.setView(editText);
        builder.setPositiveButton(R.string.dialogok, new DialogButtonClickListener());
        builder.setNegativeButton(R.string.dialogcancel, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.dialogback, new DialogButtonClickListener());

        AlertDialog dialog = builder.create();
        return dialog;
    }

    /*public Dialog ExerciseUnitDialog() {
        editText2 = new EditText(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialogtitle2);
        builder.setMessage(R.string.dialogmessage2);
        builder.setView(editText2);
        builder.setPositiveButton(R.string.dialogok, new DialogButtonClickListener());
        builder.setNegativeButton(R.string.dialogcancel, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.dialogback, new DialogButtonClickListener());

        AlertDialog dialog2 = builder.create();
        return dialog2;
    }*/

    private class DialogButtonClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            String msg = "";
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    msg += "運動メニューを登録しました";
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
