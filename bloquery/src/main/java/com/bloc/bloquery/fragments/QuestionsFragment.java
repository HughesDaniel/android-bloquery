package com.bloc.bloquery.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bloc.bloquery.R;
import com.bloc.bloquery.adapters.QuestionsAdapter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by Daniel on 10/29/2014.
 */
//public class QuestionsFragment extends Fragment implements QuestionModelCenter.onParseUpdate {
public class QuestionsFragment extends Fragment {

    public static interface QuestionFragmentCallback {
        void onQuestionItemSelected(String id, String question, String askerId,
                                    String askerUsername, byte[] avatar);
    }

    private static final String TAG = ".QuestionsFragment.java";

    // Parse fields for Question class
    private static final String PARSE_CLASS = "Question";
    private static final String PARSE_QUESTION = "theQuestion";
    private static final String PARSE_NUM_ANSWERS = "numberOfAnswers";
    private static final String PARSE_QUESTION_ASKER = "questionAsker";
    private static final String PARSE_QUESTION_CREATED_BY = "createdBy";
    private static final String PARSE_UPDATED_AT = "updatedAt";

    private ListView mListView;
    private QuestionsAdapter mAdapter;

    private QuestionFragmentCallback mCallBack;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallBack = (QuestionFragmentCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement QuestionFragmentCallback");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.questions_listview,
                container, false);

        mListView = (ListView) rootView.findViewById(R.id.lv_questions);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery create() {
                        ParseQuery query = new ParseQuery(PARSE_CLASS);
                        query.orderByDescending(PARSE_UPDATED_AT);
                        return query;
                    }
                };

        mAdapter = new QuestionsAdapter(getActivity(), factory);
        mListView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // set title back to "BloQuery"
        getActivity().setTitle(R.string.app_name);


    }

    private void selectItem(int position) {
        // the object that was selected
        ParseObject object = mAdapter.getItem(position);
        // properties of that object
        String id = object.getObjectId();
        String question = object.getString(PARSE_QUESTION);
        String askerId = object.getString(PARSE_QUESTION_ASKER);
        String askerUsername = object.getString(PARSE_QUESTION_CREATED_BY);
        byte[] avatar = convertParseFile(object);

        mCallBack.onQuestionItemSelected(id, question, askerId, askerUsername, avatar);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.questions_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.new_bloquery) {
            if (isLoggedIn()) { // user logged in, let them ask question
                AskQuestionDialog askQuestion = new AskQuestionDialog();
                askQuestion.show(getFragmentManager(), "ask_question_dialog_fragment");
            } else { // user not logged in, must log in before asking question
                LoginDialog login = new LoginDialog();
                login.show(getFragmentManager(), "login_dialog_fragment");
            }
            return true;
        }
        return false;
    }

    private boolean isLoggedIn() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        return (currentUser != null);
    }

    // returns the byte array that all parse files are stored as
    private byte[] convertParseFile(ParseObject object) {
        byte[] avatar = null;
        try {
            avatar = object.getParseFile("askerAvatar").getData();
        } catch (ParseException e) {
            Log.d(TAG, "ParseException");
            e.printStackTrace();
        }
        return avatar;
    }

    public void refreshAdapter() {
        mAdapter.loadObjects();
    }
}
