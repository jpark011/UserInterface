package ca.uwaterloo.cs.hj8park.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.xml.transform.Result;

import ca.uwaterloo.cs.hj8park.a4.model.Model;

public class ResultActivity extends AppCompatActivity {
    private Model mModel;
    private TextView mScoreText;
    private Button mToTopicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mModel = Model.getInstance();
        int score = mModel.getScore();
        int maxScore = mModel.getNumQuestions();

        mScoreText = findViewById(R.id.resultScoreText);
        mToTopicButton = findViewById(R.id.resultToTopicButton);

        String scoreMsg = getResources().getString(R.string.result_score) + ": " + score + "/" + maxScore;
        mScoreText.setText(scoreMsg);

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
}
