package com.bloc.bloquery.models;

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

    private static final String TAG = ".QuestionModelCenter.java";

    // the name of the parse class we store our question data in
    private static final String PARSE_CLASS = "Question";

    // array that will hold Questions
    private List<QuestionModel> mQuestions = new ArrayList<QuestionModel>();

    public QuestionModelCenter() {

    }

    public List<QuestionModel> getQuestions() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASS);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (null == e) {
                    for (ParseObject o: parseObjects) {;
                        mQuestions.add(new QuestionModel(o.getObjectId()));
                    }

                }
            }
        });

        return mQuestions;
    }
}
