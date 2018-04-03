package com.study.android.earlymap.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by tatsuya on 2018/04/03.
 */

public class WarningDialog extends DialogFragment {

    public interface WarningDialogListener{
        void onWarnDialogClick(DialogFragment dialogFragment);
    }

    WarningDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener=(WarningDialogListener)context;
            String className=context.getClass().toString();
            Log.d("class",className);
        }catch (Exception e){
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("確認")
                .setMessage("前の画面に移動しますか？（保存はされません）")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onWarnDialogClick(WarningDialog.this);
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();

        return builder.create();
    }
}
