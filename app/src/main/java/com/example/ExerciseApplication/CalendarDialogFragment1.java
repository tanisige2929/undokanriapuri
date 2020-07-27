package com.example.ExerciseApplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class CalendarDialogFragment1 extends DialogFragment {

    private EditText editText1;
    private EditText editText2;
    private CalendarDayActivity cda;
    private Bundle bundle;
    private AlertDialog.Builder builder;
    private int flag = 0;

    CalendarDialogFragment1(CalendarDayActivity cda) {
        this.cda = cda;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //bundle = savedInstanceState;
        flag = 0;
        editText1 = new EditText(cda);
        editText2 = new EditText(cda);
        builder = new AlertDialog.Builder(cda);
        builder.setTitle(R.string.dialogtitle1);
        builder.setMessage(R.string.dialogmessage1);
        builder.setView(editText1);
        //builder.setMessage(R.string.dialogmessage2);
        //builder.setView(editText2);
        builder.setPositiveButton(R.string.dialogok, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.dialogcancel, new DialogButtonClickListener());

        AlertDialog dialog1 = builder.create();
        return dialog1;
    }

    public void flagChange() {
        flag = 1;
    }
    public int getFlag() {
        return flag;
    }
    /*public int ExerciseUnitDialog() {
        editText2 = new EditText(cda);
        builder = new AlertDialog.Builder(cda);
        builder.setTitle(R.string.dialogtitle2);
        builder.setMessage(R.string.dialogmessage2);
        builder.setView(editText2);
        builder.setPositiveButton(R.string.dialogok, new DialogButtonClickListener());
        builder.setNegativeButton(R.string.dialogcancel, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.dialogback, new DialogButtonClickListener());

        AlertDialog dialog2 = builder.create();
        return 1;
    }*/

    private class DialogButtonClickListener extends AppCompatActivity implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            String msg = "";
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    flagChange();
                    if(flag == 1) {
                        CalendarDialogFragment2 dialogfragment2 = new CalendarDialogFragment2(cda);
                        dialogfragment2.show(getSupportFragmentManager(), "CalendarDialogFragment2");
                    }
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    msg += "キャンセルしました";
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
