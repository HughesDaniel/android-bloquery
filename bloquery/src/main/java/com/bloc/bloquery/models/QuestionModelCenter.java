package com.bloc.bloquery.models;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 11/4/2014.
 */
public class QuestionModelCenter {

    private static final String TAG = ".newQuestionModelCenter.java";

    // the fields we use to store data in our parse cloud for a Question
    private static final String PARSE_CLASS = "Question";
    private static final String PARSE_QUESTION = "theQuestion";
    private static final String PARSE_QUESTION_ASKER = "questionAsker";
    private static final String PARSE_NUM_ANSWERS = "numberOfAnswers";
    private static final String PARSE_QUESTION_ANSWERS = "answers";

    // array of our Question objects
    private static Map<String, Question> sQuestionHash = new HashMap<String, Question>();

    public QuestionModelCenter() {
        // our constructor
    }

    // Called when a question is created, it is not on Parse, so will add it
    public void createNewQuestion(String question) {
        // Id of the current logged in user
        ParseUser userId = ParseUser.getCurrentUser();
        // add the question to Parse
        addQuestionToParse(question, userId);
    }

    // called to create a model of a question that already exists on Parse
    public void createExistingQuestion(final String questionId) {
        // get Data from Parse
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASS);
        query.getInBackground(questionId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // create Question
                    Question q = new Question(questionId, // unique ID of question
                            object.getString(PARSE_QUESTION), // the question
                            object.getParseUser(PARSE_QUESTION_ASKER), // the ID of the asker
                            object.getInt(PARSE_NUM_ANSWERS), // Number of answers
                            object.getCreatedAt(), // when question was created
                            object.getUpdatedAt()); // when the last time it was updated
                    // add Question to the hashmap
                    sQuestionHash.put(questionId, q);
                } else {
                    // retrieval failed
                    Log.d(TAG, "Query failed: " + e);
                }
            }
        });
    }

    public void getQuestions() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASS);

    }

    // helper method that adds the Question to Parse and creates a Question Model in the callback
    private void addQuestionToParse(final String question, final ParseUser userId) {
        final ParseObject Question = new ParseObject(PARSE_CLASS);
        Question.put(PARSE_QUESTION, question);
        Question.put(PARSE_QUESTION_ASKER, userId);
        Question.put(PARSE_NUM_ANSWERS, 0);

        Question.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (null == e) { // Saved successfully
                    // create Question model
                    Question q = new Question(Question.getObjectId(), // unique ID of question
                            question, // the question
                            userId, // the Id of the asker
                            0, // Zero because its just being created so hasn't been answered
                            Question.getCreatedAt(), // When it was created
                            Question.getUpdatedAt()); // When it was updated
                    // add Question to our hashmap
                    sQuestionHash.put(Question.getObjectId(), q);
                } else {
                    // The save failed
                    Log.d(TAG, "Error in saving: " + e);
                }
            }
        });
    }










    private class Question {

        private static final String TAG = ".QuestionModel.java";

        // member variables
        private String mQuestionId;
        private String mQuestion;
        private ParseUser mAskerId;
        private int mNumberOfAnswers;
        private Date mCreatedAt;
        private Date mUpdatedAt;

        public Question( String questionId, String question, ParseUser askerId, int numberOfAnwers,
                         Date created, Date updated) {
            mQuestion = question;
            mQuestionId = questionId;
            mAskerId = askerId;
            mNumberOfAnswers = numberOfAnwers;
            mCreatedAt = created;
            mUpdatedAt = updated;
        }

        public String getQuestionId() {
            return mQuestionId;
        }

        public String getTheQuestion() {
            return mQuestion;
        }

        public ParseUser getAskerId() {
            return mAskerId;
        }

        public int getNumberOfAnswers() {
            return mNumberOfAnswers;
        }

        public Date getDateCreated() {
            return mCreatedAt;
        }

        public Date getDateUpdated() {
            return mUpdatedAt;
        }

        @Override
        public String toString() {
            return mQuestion;
        }

    }
}
