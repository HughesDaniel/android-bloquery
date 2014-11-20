package com.bloc.bloquery;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bloc.bloquery.fragments.AnswerQuestionDialog;
import com.bloc.bloquery.fragments.AnswersFragment;
import com.bloc.bloquery.fragments.AskQuestionDialog;
import com.bloc.bloquery.fragments.LoginDialog;
import com.bloc.bloquery.fragments.QuestionsFragment;
import com.bloc.bloquery.models.AnswerModelCenter;
import com.bloc.bloquery.models.QuestionModelCenter;
import com.parse.ParseUser;


public class BloMainActivity extends Activity implements AskQuestionDialog.AskQuestionListener,
        QuestionsFragment.QuestionFragmentCallback, AnswerQuestionDialog.AnswerQuestionListener {

    private static final String TAG = ".BloMainActivity.java";

    private QuestionsFragment mQuestionsFragment;
    private AnswersFragment mAnswersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mQuestionsFragment = new QuestionsFragment();
            getFragmentManager().beginTransaction()
                    //.add(R.id.container, new QuestionsFragment())
                    .add(R.id.container, mQuestionsFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bloquery_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // checks if user is logged in or out, and displays appropriate menu title
        MenuItem loginstatus = menu.findItem(R.id.login_out_menu);
        if (ParseUser.getCurrentUser() == null) {
            loginstatus.setTitle(R.string.login);
        } else {
            loginstatus.setTitle(R.string.logout);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.login_out_menu) {
            if (isLoggedIn()) { // user logged in, log them out
                ParseUser.getCurrentUser().logOut();
            } else { // user logged out, display login dialog
                LoginDialog login = new LoginDialog();
                login.show(getFragmentManager(), "login_dialog_fragment");
            }
            return true;
        }
        return false;
    }


    private boolean isLoggedIn() {
        return (ParseUser.getCurrentUser() != null);
    }

    // vvvvvvvvvvvvvvvvvvvvv callbacks vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    @Override
    public void onAskQuestion(AskQuestionDialog askQuestionDialog, String question) {
        QuestionModelCenter qmc = new QuestionModelCenter(this);
        qmc.createNewQuestion(question);
        // user added a question, reload the adapter
        mQuestionsFragment.refreshAdapter();
    }

    @Override
    public void onQuestionItemSelected(String id, String question, String askerId,
                                       String askerUsername, byte[] avatar) {
        mAnswersFragment =  AnswersFragment.newInstance(id, question, askerId,
                askerUsername, avatar);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mAnswersFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAnswerQuestion(AnswerQuestionDialog answerQuestionDialog, String answer,
                                 String questionId) {
        AnswerModelCenter amc = new AnswerModelCenter(this);
        amc.createAnswerToQuestion(answer, questionId);
        mAnswersFragment.refreshAdapter();
    }
}
