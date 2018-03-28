package ca.uwaterloo.cs.hj8park.a4;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import javax.xml.transform.Result;

import ca.uwaterloo.cs.hj8park.a4.fragment.QuestionFragment;
import ca.uwaterloo.cs.hj8park.a4.model.Model;
import ca.uwaterloo.cs.hj8park.a4.model.Quiz;

public class ResultActivity extends AppCompatActivity implements QuestionFragment.OnFragmentInteractionListener {
    private Model mModel;
    private TextView mScoreText;
    private Button mToTopicButton;
    private TextView mTimeText;
    private ViewGroup mAnswersContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mModel = Model.getInstance();
        int score = mModel.getScore();
        int maxScore = mModel.getNumQuestions();
        int time = mModel.getTime();

        mScoreText = findViewById(R.id.resultScoreText);
        mToTopicButton = findViewById(R.id.resultToTopicButton);
        mTimeText = findViewById(R.id.resultTimeTaken);

        String scoreMsg = getResources().getString(R.string.result_score) + ": " + score + "/" + maxScore;
        mScoreText.setText(scoreMsg);

        String timeMsg = getResources().getString(R.string.result_time) + ": " + time + " sec";
        mTimeText.setText(timeMsg);

        mAnswersContainer = findViewById(R.id.resultAnswersContainer);
        buildAnswers(maxScore);


        mToTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // I don't want to destroy everything!
                String userName = mModel.getUserName();
                mModel = mModel.reset();
                mModel.setUserName(userName);
                Intent intent = new Intent(ResultActivity.this, SelectActivity.class);
                startActivity(intent);
            }
        });
    }

    private void buildAnswers(int maxScore) {
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();

        for (int i = 0; i < maxScore; i++) {
            Quiz quiz = mModel.getQuiz(i);
            QuestionFragment frag = QuestionFragment.newInstance(quiz, true);
            fragTransaction.add(mAnswersContainer.getId(), frag);
        }

        fragTransaction.commit();
    }

    // THESE ARE JUST PLACEHOLDERS (NO EVENT INTERACTIONS)
    @Override
    public void onFragmentRadioCheck(int answer) {

    }

    @Override
    public void onFragmentCheckBoxCheck(int answer) {

    }

    @Override
    public void onFragmentCheckBoxUnCheck(int answer) {

    }
}
