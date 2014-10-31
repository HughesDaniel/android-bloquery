package com.bloc.bloquery.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 10/30/2014.
 */
public class QuestionModelCenter {

    public static interface onParseUpdate {
        public void onUpdate(List<QuestionModel> questionList);
        public void onModelUpdate();
    }

    private static final String TAG = ".QuestionModelCenter.java";

    // the name of the parse class we store our question data in
    private static final String PARSE_CLASS = "Question";

    // array that will hold Questions
    private List<QuestionModel> mQuestions = new ArrayList<QuestionModel>();

    // will point to the class that will be passed in to the constructor
    onParseUpdate mCallingClass;

    public QuestionModelCenter(onParseUpdate observer) {
        mCallingClass = observer;
    }

    // Queries Parse and gets all Question classes that it has stored
    public void getQuestions() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASS);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (null == e) { // success
                    for (ParseObject o: parseObjects) {
                        // creates a question model
                        QuestionModel temp = new QuestionModel(o.getObjectId(), mCallingClass);
                        // adds it to the list
                        mQuestions.add(temp);
                    }
                    // passes the list back to whatever class called this method
                    mCallingClass.onUpdate(mQuestions);
                } else {
                    Log.d(TAG, "Error getting Questions: " + e);
                }
            }
        });
    }

    public List<QuestionModel> testGetQuestion() {
        return mQuestions;
    }
}
