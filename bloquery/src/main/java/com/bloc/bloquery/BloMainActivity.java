package com.bloc.bloquery;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bloc.bloquery.fragments.AskQuestionDialog;
import com.bloc.bloquery.fragments.QuestionsFragment;
import com.bloc.bloquery.models.QuestionModel;


public class BloMainActivity extends Activity implements AskQuestionDialog.AskQuestionListener{

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blo_query, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.new_bloquery) {
            AskQuestionDialog askQuestion = new AskQuestionDialog();
            askQuestion.show(getFragmentManager(), "ask_question_dialog_fragment");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAskQuestion(AskQuestionDialog askQuestionDialog, String question) {
        //TODO: replace this with code to get the user ID
        String userId = "999";

        new QuestionModel(this, question, userId);
    }
}
