package com.bloc.bloquery.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public AskQuestionDialog() {
        // required constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ask_question, container, false);
        getDialog().setTitle(R.string.new_question_dialog);

        mEditText = (EditText) view.findViewById(R.id.et_new_question);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 0 right now for debug as emulator doesnt have soft keyboard
                // replace with EditorInfo.IME_ACTION_DONE
                if (actionId == 0) {
                    AskQuestionListener activity = (AskQuestionListener) getActivity();
                    //call back attached to the activity
                    activity.onAskQuestion(AskQuestionDialog.this, mEditText.getText().toString());
                    closeDialog();
                    return true;
                }

                return false;
            }
        });


        return view;
    }

    // helper method that closes the dialog
    private void closeDialog() {
        this.dismiss();
    }

}
