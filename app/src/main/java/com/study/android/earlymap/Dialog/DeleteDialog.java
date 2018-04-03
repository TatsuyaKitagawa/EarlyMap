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

public class DeleteDialog extends DialogFragment {



    public interface DeleteDialogListener{
        void onDeleteDialogClick(DialogFragment dialogFragment,int pos);
    }

    DeleteDialogListener mListner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListner=(DeleteDialogListener)context;
        }catch (Exception e){

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
       final int pos=getArguments().getInt("DELETE_KEY");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("確認")
                .setMessage("このリストを削除しますか？")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListner.onDeleteDialogClick(DeleteDialog.this,pos);
                    }
                })
                .setNegativeButton("Cancel",null);
        return builder.create();
    }
}
