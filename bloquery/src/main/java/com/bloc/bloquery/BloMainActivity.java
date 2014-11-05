package com.bloc.bloquery;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bloc.bloquery.fragments.AnswersFragment;
import com.bloc.bloquery.fragments.AskQuestionDialog;
import com.bloc.bloquery.fragments.QuestionsFragment;
import com.bloc.bloquery.models.QuestionModelCenter;


public class BloMainActivity extends Activity implements AskQuestionDialog.AskQuestionListener,
        QuestionsFragment.QuestionFragmentCallback{

    private static final String TAG = ".BloMainActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new QuestionsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return false;
    }

    // vvvvvvvvvvvvvvvvvvvvv callbacks vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

    @Override
    public void onAskQuestion(AskQuestionDialog askQuestionDialog, String question) {
        QuestionModelCenter qmc = new QuestionModelCenter();
        qmc.createNewQuestion(question);
    }

    @Override
    public void onQuestionItemSelected(String id, String question) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, AnswersFragment.newInstance(id, question))
                .commit();
    }
}
