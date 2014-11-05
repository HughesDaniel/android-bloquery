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
 * Created by Daniel on 11/5/2014.
 */
public class AnswerQuestionDialog extends DialogFragment {

    public interface AnswerQuestionListener {
        void onAnswerQuestion(AnswerQuestionDialog answerQuestionDialog, String answer);
    }

    private static final String TAG = ".AnswerQuestionDialog.java";

    private static final String KEY_QUESTION =
            "com.bloc.bloquery.fragments.AnswerQuestionDialog.key_question";

    private String mQuestion;
    private EditText mEditText;
    private AnswerQuestionListener mListener;

    public static AnswerQuestionDialog newInstance(String question) {
        AnswerQuestionDialog dialog = new AnswerQuestionDialog();
        Bundle args = new Bundle();
        args.putString(KEY_QUESTION, question);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try {
            mListener = (AnswerQuestionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
            " must implement AnswerQuestionListener");
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mQuestion = getArguments().getString(KEY_QUESTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        super.onCreateView(inflater, container, bundle);

        View view = inflater.inflate(R.layout.dialog_answer_question, container, false);

        TextView questionTextView = (TextView) view.findViewById(R.id.tv_question_in_answer_dialog);
        questionTextView.setText(mQuestion);

        mEditText = (EditText) view.findViewById(R.id.et_answer_question);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 0) {
                    mListener.onAnswerQuestion(AnswerQuestionDialog.this,
                            mEditText.getText().toString());
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


    private void closeDialog() {
        this.dismiss();
    }
}
