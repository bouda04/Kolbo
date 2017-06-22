package com.example.simpleandroidproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;



public class MyDialog extends DialogFragment {
    public final static int SCORE_DIALOG = 1;

    private ResultsListener listener;
    private int reqCode;

    static MyDialog newInstance(int requestCode) {
        MyDialog dlg = new MyDialog();
        Bundle args = new Bundle();
        args.putInt("rc", requestCode);
        dlg.setArguments(args);
        return dlg;
    }

    public void setCallback(ResultsListener listener) {
        this.listener = listener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.reqCode = getArguments().getInt("rc");
        if (reqCode== SCORE_DIALOG)
            return buildScoreDialog().create();
        else
            return buildExitDialog().create();
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            if (getParentFragment() != null)
                listener = (ResultsListener)getParentFragment();
            else
                listener = (ResultsListener)activity;
        } catch(ClassCastException e) {
            throw new ClassCastException("this fragment host must implement ResultsListener");
        }
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private AlertDialog.Builder buildExitDialog(){
        return new AlertDialog.Builder(getActivity())
                .setTitle("Closing the application")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listener.onFinishedDialog(reqCode, "ok");
                            }
                        }
                )
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                );
    }

    private AlertDialog.Builder buildScoreDialog(){
        final String[] selected = new String[1];
        final View view = getActivity().getLayoutInflater().inflate(R.layout.item_score, null);
        final SeekBar ScoreBar = (SeekBar) view.findViewById(R.id.skScore);

        ScoreBar.setProgress(2);
      

        return new AlertDialog.Builder(getActivity())
                .setTitle("Set this product's score")
                .setView(view)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listener.onFinishedDialog(reqCode, ScoreBar.getProgress());
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                );
    }

    public interface ResultsListener {
        void onFinishedDialog(int requestCode, Object results);
    }
}
