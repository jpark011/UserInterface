package ca.uwaterloo.cs.hj8park.a4;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import ca.uwaterloo.cs.hj8park.a4.fragment.QuestionFragment;
import ca.uwaterloo.cs.hj8park.a4.model.Model;
import ca.uwaterloo.cs.hj8park.a4.model.Quiz;

public class QuestionActivity extends AppCompatActivity implements Observer, QuestionFragment.OnFragmentInteractionListener {
    private Model mModel;
    private int mContainerId;
    private Button mPrevButton;
    private Button mNextButton;
    private TextView mCurNumText;
    private QuestionFragment[] fragments;
    private int startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Question time!
        startTime = (int) System.currentTimeMillis() / 1000;

        mModel = Model.getInstance();
        mModel.addObserver(this);

        int numQs = mModel.getNumQuestions();
        fragments = new QuestionFragment[numQs];

        // init fragments needed
        for (int i = 0; i < numQs; i++) {
            fragments[i] = QuestionFragment.newInstance(mModel.getQuiz(i), false);
        }

        mContainerId = R.id.questionContainer;
        mPrevButton = findViewById(R.id.questionPrevButtion);
        mNextButton = findViewById(R.id.questionNextButton);
        mCurNumText = findViewById(R.id.questionPageNum);

        // this updates...!!
        mModel.setCurNumQuestion(0);

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.prevPage();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Next
                if (mModel.getCurNumQuestion() < mModel.getNumQuestions() - 1) {
                    mModel.nextPage();
                // Finish
                } else {
                    int endTime = (int) System.currentTimeMillis() / 1000;
                    mModel.calcScore(endTime - startTime);
                    Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    // This usually involves fragment transition
    @Override
    public void update(Observable o, Object arg) {
        int curNumQuestion = mModel.getCurNumQuestion();
        int maxPage = mModel.getNumQuestions();
        // Fragment update
        // I don't understand... ugly
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(mContainerId, fragments[curNumQuestion]);
        transaction.commit();

        mCurNumText.setText((curNumQuestion + 1) + "/" + maxPage);

        mPrevButton.setEnabled(0 < curNumQuestion);
        mPrevButton.setAlpha((0 < curNumQuestion)? 1 : 0.5f);

        if (curNumQuestion < maxPage - 1) {
            mNextButton.setText(getString(R.string.next_button));
        } else {
            mNextButton.setText(getString(R.string.finish_button));
        }
    }

    @Override
    public void onFragmentRadioCheck(int answer) {
        mModel.updateAnswer(answer);
    }

    @Override
    public void onFragmentCheckBoxCheck(int answer) {
        mModel.addAnswer(answer);
    }

    @Override
    public void onFragmentCheckBoxUnCheck(int answer) {
        mModel.removeAnswer(answer);
    }
}
