package com.bloc.bloquery.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bloc.bloquery.R;
import com.bloc.bloquery.adapters.AnswersAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by Daniel on 11/4/2014.
 */
public class AnswersFragment extends Fragment {

    private static final String TAG = ".AnswersFragment.java";

    // key to set and retrieve the question ID we store in the args for this fragment
    private static final String KEY_QUESTION_ID =
            "com.bloc.bloquery.fragments.AnswersFragment.key_question_id";
    // key to set and retrieve the question we store in the args for this fragment
    private static final String KEY_QUESTION =
            "com.bloc.bloquery.fragments.AnswersFragment.key_question";
    // key to set and retriever the asker ID we store in the args for this fragment
    private static final String KEY_ASKER_ID =
            "com.bloc.bloquery.fragments.AnswerFragment.key_asker_id";
    //key to set and retrieve the user name of the person who asked the question
    private static final String KEY_ASKER_USERNAME =
            "com.bloc.bloquery.fragments.AnswerFragment.key_asker_username";
    //key to set and retrieve the avatar of the person who asked the question
    private static final String KEY_ASKER_AVATAR =
            "com.bloc.bloquery.fragments.AnswerFragment.key_asker_avatar";

    // The unique ID of the Question that we will display the answers to
    private String mQuestionId;
    private String mQuestion;
    private String mAskerId;
    private String mAskerUsername;

    private ListView mListView;
    private AnswersAdapter mAdapter;
    private byte[] mAskerAvatar;

    public static AnswersFragment newInstance(String questionId, String question, String askerId,
                                               String askerUsername, byte[] avatar) {
        AnswersFragment fragment = new AnswersFragment();
        Bundle args = new Bundle();
        args.putString(KEY_QUESTION_ID, questionId);
        args.putString(KEY_QUESTION, question);
        args.putString(KEY_ASKER_ID, askerId);
        args.putString(KEY_ASKER_USERNAME, askerUsername);
        args.putByteArray(KEY_ASKER_AVATAR, avatar);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mQuestionId = getArguments().getString(KEY_QUESTION_ID);
        mQuestion = getArguments().getString(KEY_QUESTION);
        mAskerId = getArguments().getString(KEY_ASKER_ID);
        mAskerUsername = getArguments().getString(KEY_ASKER_USERNAME);
        mAskerAvatar = getArguments().getByteArray(KEY_ASKER_AVATAR);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        super.onCreateView(inflater, container, bundle);

        // set the action bar title to "username asks..."
        setTitle();

        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.answers_listview,
                container, false);
        // The question
        TextView questionTextView = (TextView) rootView.findViewById(R.id.tv_question_in_answer_dialog);
        questionTextView.setText(mQuestion);
        // The avatar of the asker
        ImageView askerIdImageView = (ImageView) rootView.findViewById(R.id.iv_avatar_question_asker_in_answer_dialog);
        askerIdImageView.setImageBitmap(decodeBitmap(mAskerAvatar));
        // the view for when the listview is empty
        RelativeLayout emptyView = (RelativeLayout) rootView.findViewById(R.id.empty_layout);
        // The button to answer a question when the listview is empty
        ImageButton emptyButton = (ImageButton) rootView.findViewById(R.id.ib_add_question_empty);
        emptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerQuestion();
            }
        });

        mListView = (ListView) rootView.findViewById(R.id.lv_answers);
        mListView.setEmptyView(emptyView);

        // The set of conditions to give to the arrayadapter
        ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery create() {
                        ParseQuery query = new ParseQuery("Answer");
                        query.whereEqualTo("parent",
                                ParseObject.createWithoutData("Question", mQuestionId));
                        return query;
                    }
                };

        mAdapter = new AnswersAdapter(getActivity(), factory);

        mListView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.answers_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.answer_bloquery) {
            answerQuestion();
            return true;
        }
        return false;
    }

    private void answerQuestion() {
        if (isLoggedIn()) {
            AnswerQuestionDialog dialog = AnswerQuestionDialog.newInstance(mQuestion, mQuestionId, mAskerAvatar);
            dialog.show(getFragmentManager(), "answer_questions_dialog_fragment");
        } else { // make the user login
            LoginDialog login = new LoginDialog();
            login.show(getFragmentManager(), "login_dialog_fragment");
        }
    }

    private boolean isLoggedIn() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        return (currentUser != null);
    }

    private void setTitle() {
        String title = mAskerUsername + " " + getActivity().getString(R.string.asks);
        getActivity().setTitle(title);
    }

    private Bitmap decodeBitmap(byte[] file) {
        int length = file.length;
        Bitmap bitmap = BitmapFactory.decodeByteArray(file, 0, length);

        return bitmap;
    }

    public void refreshAdapter() {
        mAdapter.loadObjects();
    }
}
