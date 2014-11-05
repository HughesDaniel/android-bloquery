package com.bloc.bloquery.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bloc.bloquery.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * Created by Daniel on 10/30/2014.
 */
public class QuestionModel {

    private static final String TAG = ".QuestionModel.java";

    // the fields we use to store data in our parse cloud
    private static final String PARSE_CLASS = "Question";
    private static final String PARSE_QUESTION = "theQuestion";
    private static final String PARSE_QUESTION_ASKER = "questionAsker";
    private static final String PARSE_NUM_ANSWERS = "numberOfAnswers";
    private static final String PARSE_QUESTION_ANSWERS = "answers";

    // member variables
    private Context mContext;
    private String mQuestionId;
    private String mQuestion;
    private String mAskerId;
    private int mNumberOfAnswers = 0;
    //private List<Object> mAnswers = new ArrayList<Object>();

    // Creates a Question for a Question we already have on parse, using its unique ID
    public QuestionModel(String questionId, final QuestionModelCenter.onParseUpdate observer) {
        mQuestionId = questionId;

        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASS);
        query.getInBackground(mQuestionId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (null == e) {
                    // successful, now set set the member variables
                    mQuestion = object.getString(PARSE_QUESTION);
                    mAskerId= object.getString(PARSE_QUESTION_ASKER);
                    mNumberOfAnswers = object.getInt(PARSE_NUM_ANSWERS);
                    //call back to let know whoever that all the Model variables contain data
                    observer.onModelUpdate();
                }
            }
        });
    }

    // Creates a new Question and uploads it to Parse
    public QuestionModel(Context context, String question, String userId) {
        mContext = context;
        mQuestion = question;
        mAskerId = userId;

        addToParse();
    }

    // Creates the parse object and all the key,value pairs and uploads the data to parse
    // parse automatically does this in a background thread
    private void addToParse() {
        final ParseObject question = new ParseObject(PARSE_CLASS);
        question.put(PARSE_QUESTION, mQuestion);
        question.put(PARSE_QUESTION_ASKER, mAskerId);
        question.put(PARSE_NUM_ANSWERS, mNumberOfAnswers);
        //question.put(PARSE_QUESTION_ANSWERS, mAnswers);

        question.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (null == e) { // Saved successfully
                    // gets the Id of the object and saves it in our member variable
                    mQuestionId = question.getObjectId();
                    // Creates a toast upon successful save to parse
                    Toast.makeText(mContext, mContext.getString(R.string.question_posted),
                            Toast.LENGTH_SHORT);
                } else {
                    // The save failed
                    Log.d(TAG, "Error in saving: " + e);
                }
            }
        });
    }

    public String getQuestionId() {
        return mQuestionId;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public String getAskerId() {
        return mAskerId;
    }

    public int getNumberOfAnswers() {
        return mNumberOfAnswers;
    }

    @Override
    public String toString() {
        return mQuestion;
    }
}
