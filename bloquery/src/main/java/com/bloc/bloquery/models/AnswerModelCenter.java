package com.bloc.bloquery.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bloc.bloquery.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 11/4/2014.
 */
public class AnswerModelCenter {

    private static final String TAG = ".AnswerModel.java";

    // column fields for Parse Answer class
    private static final String PARSE_CLASS = "Answer";
    private static final String PARSE_ANSWER = "theAnswer";
    private static final String PARSE_ANSWERER = "answerer";
    private static final String PARSE_PARENT = "parent";
    private static final String PARSE_NUM_UPVOTES = "numberOfUpVotes";
    private static final String PARSE_UP_VOTERS = "upVoters";
    // column field for Parse Question, used to increment field when asnwer is added
    private static final String PARSE_NUM_ANSWERS = "numberOfAnswers";

    private Context mContext;
    private static Map<String, Answer> sAnswerMap = new HashMap<String, Answer>();

    public AnswerModelCenter(Context context) {
        mContext = context;
    }

    // called when an answer is given for the the first time
    public void createAnswerToQuestion(String answer, String questionId) {
        ParseUser userId = ParseUser.getCurrentUser();
        // the parent question
        ParseObject Parent = ParseObject.createWithoutData("Question", questionId);
        addAnswerToParse(answer, userId, Parent);
        incrementQuestionNumAnswers(Parent);
    }

    private void addAnswerToParse(final String answer, final ParseUser userId, final ParseObject parent) {
        final ParseObject Answer = new ParseObject(PARSE_CLASS);
        Answer.put(PARSE_ANSWER, answer);
        Answer.put(PARSE_ANSWERER, userId);
        Answer.put(PARSE_PARENT, parent);
        Answer.put(PARSE_NUM_UPVOTES, 0); // 0 as its new so cant have been upvoted
        Answer.add(PARSE_UP_VOTERS, null);

        Answer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Answer a = new Answer(Answer.getObjectId(), // unique Id of Parse Answer Obj
                            answer, // the textual answer
                            userId, // the Parse user
                            parent, // the Parse Question answer is pointing to
                            0, // number of upvotes
                            Answer.getCreatedAt(),
                            Answer.getUpdatedAt());
                    // inform user of success
                    Toast.makeText(mContext, mContext.getString(R.string.answer_added),
                            Toast.LENGTH_LONG).show();
                    // add object to hashmap
                    sAnswerMap.put(Answer.getObjectId(), a);
                } else {
                    Log.d(TAG, "Error adding Answer to Parse " + e);
                }
            }
        });
        
    }

    private void incrementQuestionNumAnswers(ParseObject Parent) {
        Parent.increment(PARSE_NUM_ANSWERS);
        Parent.saveInBackground();
    }

    // Adds username to the "upVoters" array for the answer, and increments the number of upvotes
    public void addUpVoterToAnswer(ParseObject Answer, String userName) {
        Answer.addUnique(PARSE_UP_VOTERS, userName);
        Answer.increment(PARSE_NUM_UPVOTES);
        Answer.saveInBackground();
    }

    // Removes username from the array of upvotes, and decrements the number of upvotes
    public void removeUpVoterFromAnswer(ParseObject Answer, String userName) {
        Answer.removeAll(PARSE_UP_VOTERS, Arrays.asList(userName));
        Answer.increment(PARSE_NUM_UPVOTES, -1);
        Answer.saveInBackground();
    }


    private class Answer {

        private String mAnswerId;
        private String mAnswer;
        private ParseUser mAnswerer;
        private ParseObject mParent;
        private int mNumUpVotes;
        private List<String> mUpVoters; // Not a param in constructor because it starts empty
        private Date mCreatedAt;
        private Date mUpdatedAt;

        public Answer(String answerId, String answer, ParseUser answerer, ParseObject parent,
                      int numUpVotes, Date createdAt, Date updatedAt) {

            mAnswerId = answerId;
            mAnswer = answer;
            mAnswerer = answerer;
            mParent = parent;
            mNumUpVotes = numUpVotes;
            mUpVoters = new ArrayList<String>();
            mCreatedAt = createdAt;
            mUpdatedAt = updatedAt;

        }

        public String getAnswerId() {
            return mAnswerId;
        }

        public String getAnswer() {
            return mAnswer;
        }

        public ParseUser getAnswerer() {
            return mAnswerer;
        }

        public ParseObject getParent() {
            return mParent;
        }

        public int getNumUpVotes() {
            return mNumUpVotes;
        }

        public List getUpVoters() {
            return mUpVoters;
        }

        public Date getCreatedAt() {
            return mCreatedAt;
        }

        public Date getUpdatedAt() {
            return mUpdatedAt;
        }

        @Override
        public String toString() {
            return mAnswer;
        }

    }
}
