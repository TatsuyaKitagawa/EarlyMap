package com.study.android.earlymap.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by tatsuya on 2018/04/03.
 */

public class SaveingDialog extends DialogFragment {

    public interface SaveingDialogListener{
        void onSaveDialogClick(DialogFragment dialogFragment);
    }

    SaveingDialogListener mListner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListner=(SaveingDialogListener)context;
        }catch (Exception e){

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("確認")
                .setMessage("このリストを保存しますか（後からでも編集できます）")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       mListner.onSaveDialogClick(SaveingDialog.this);
                    }
                })
                .setNegativeButton("Cancel",null);
        return builder.create();
    }
}
