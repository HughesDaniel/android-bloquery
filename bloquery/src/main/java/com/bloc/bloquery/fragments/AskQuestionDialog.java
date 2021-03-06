package com.bloc.bloquery.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.bloc.bloquery.R;

/**
 * Created by Daniel on 11/3/2014.
 */
public class AskQuestionDialog extends DialogFragment {

    public interface AskQuestionListener {
        void onAskQuestion(AskQuestionDialog askQuestionDialog, String question);
    }

    private static final String TAG = ".AskQuestionDialog.java";

    private EditText mEditText;
    private AskQuestionListener mListener;

    public AskQuestionDialog() {
        // required constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            mListener = (AskQuestionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
            " must implement AskQuestionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ask_question, container, false);

        mEditText = (EditText) view.findViewById(R.id.et_new_question);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 0 right now for debug as emulator doesnt have soft keyboard
                // replace with EditorInfo.IME_ACTION_DONE
                if (actionId == 0) {
                    mListener.onAskQuestion(AskQuestionDialog.this, mEditText.getText().toString());
                    closeDialog();
                    return true;
                }

                return false;
            }
        });


        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    // helper method that closes the dialog
    private void closeDialog() {
        this.dismiss();
    }

}
