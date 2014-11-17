package com.bloc.bloquery.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bloc.bloquery.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
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
    private static final String PARSE_QUESTION_CREATED_BY = "createdBy";
    private static final String PARSE_ASKER_AVATAR = "askerAvatar";
    private static final String PARSE_USER_AVATAR = "avatar";

    // array of our Question objects
    private static Map<String, Question> sQuestionHash = new HashMap<String, Question>();

    Context mContext;

    public QuestionModelCenter(Context context) {
        mContext = context;
    }

    // Called when a question is created, it is not on Parse, so will add it
    public void createNewQuestion(String question) {
        // Id of the current logged in user
        ParseUser parseUser = ParseUser.getCurrentUser();
        // add the question to Parse
        addQuestionToParse(question, parseUser);
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
                            object.getParseFile(PARSE_ASKER_AVATAR), // asker's avatar
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

    //TODO: either finish this or erase it depending on further implementation
    public void getQuestions() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CLASS);

    }

    // helper method that adds the Question to Parse and creates a Question Model in the callback
    private void addQuestionToParse(final String question, final ParseUser parseUser) {
        final ParseObject Question = new ParseObject(PARSE_CLASS);
        Question.put(PARSE_QUESTION, question);
        Question.put(PARSE_QUESTION_ASKER, parseUser);
        Question.put(PARSE_NUM_ANSWERS, 0);
        Question.put(PARSE_QUESTION_CREATED_BY, parseUser.getUsername());

        try {
            parseUser.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //TODO hard code string
        final ParseFile askerAvatar = parseUser.getParseFile(PARSE_USER_AVATAR);
        Question.put(PARSE_ASKER_AVATAR, askerAvatar);


        Question.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (null == e) { // Saved successfully
                    // create Question model
                    Question q = new Question(Question.getObjectId(), // unique ID of question
                            question, // the question
                            parseUser, // the Id of the asker
                            askerAvatar, // the avatar of the asker
                            0, // Zero because its just being created so hasn't been answered
                            Question.getCreatedAt(), // When it was created
                            Question.getUpdatedAt()); // When it was updated
                    // add Question to our hashmap
                    sQuestionHash.put(Question.getObjectId(), q);
                    // Toast user post was successful
                    Toast.makeText(mContext, mContext.getString(R.string.question_posted),
                            Toast.LENGTH_LONG).show();
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
        private ParseFile mAskerAvatar;

        public Question( String questionId, String question, ParseUser askerId, ParseFile askerAvatar,
                         int numberOfAnwers, Date created, Date updated) {
            mQuestion = question;
            mQuestionId = questionId;
            mAskerId = askerId;
            mAskerAvatar = askerAvatar;
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

        public ParseFile getAskerAvatar() {
            return mAskerAvatar;
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
