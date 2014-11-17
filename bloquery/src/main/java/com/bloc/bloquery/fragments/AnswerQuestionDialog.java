package com.bloc.bloquery.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloc.bloquery.R;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Daniel on 11/5/2014.
 */
public class AnswerQuestionDialog extends DialogFragment {

    public interface AnswerQuestionListener {
        void onAnswerQuestion(AnswerQuestionDialog answerQuestionDialog, String answer,
                              String questionId);
    }

    private static final String TAG = ".AnswerQuestionDialog.java";

    private static final String KEY_QUESTION =
            "com.bloc.bloquery.fragments.AnswerQuestionDialog.key_question";
    private static final String KEY_QUESTION_ID =
            "com.bloc.bloquery.fragments.AnswerQuestionDialog.key_question_id";
    private static final String KEY_ASKER_AVATAR =
            "com.bloc.bloquery.fragments.AnswerQuestionsDialog.key_questions_id";

    private static final String PARSE_USER_AVATAR = "avatar";



    private String mQuestion;
    private String mQuestionId;
    private EditText mEditText;
    private Bitmap mAskerAvatar;
    private AnswerQuestionListener mListener;

    public static AnswerQuestionDialog newInstance(String question, String questionId, byte[] askerAvatar) {
        AnswerQuestionDialog dialog = new AnswerQuestionDialog();
        Bundle args = new Bundle();
        args.putString(KEY_QUESTION, question);
        args.putString(KEY_QUESTION_ID, questionId);
        args.putByteArray(KEY_ASKER_AVATAR, askerAvatar);
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
        mQuestionId = getArguments().getString(KEY_QUESTION_ID);
        mAskerAvatar = decodeBitmap(getArguments().getByteArray(KEY_ASKER_AVATAR));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        super.onCreateView(inflater, container, bundle);

        View view = inflater.inflate(R.layout.dialog_answer_question, container, false);

        TextView questionTextView = (TextView) view.findViewById(R.id.tv_question_in_answer_dialog);
        questionTextView.setText(mQuestion);

        ImageView askerAvatar = (ImageView) view.findViewById(R.id.iv_avatar_question_asker_in_answer_dialog);
        askerAvatar.setImageBitmap(mAskerAvatar);

        mEditText = (EditText) view.findViewById(R.id.et_answer_question);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 0) {
                    mListener.onAnswerQuestion(AnswerQuestionDialog.this,
                            mEditText.getText().toString(), mQuestionId);
                    closeDialog();
                    return true;
                }

                return false;
            }
        });

        ImageView userAvatarImageView = (ImageView) view.findViewById(R.id.iv_avatar_user_answer_dialog);
        try {
            ParseUser dafuq = ParseUser.getCurrentUser().fetch();
            userAvatarImageView.setImageBitmap(decodeBitmap(dafuq.getParseFile(PARSE_USER_AVATAR).getData()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

    private Bitmap decodeBitmap(byte[] file) {
        int length = file.length;
        Bitmap bitmap = BitmapFactory.decodeByteArray(file, 0, length);

        return bitmap;
    }
}
