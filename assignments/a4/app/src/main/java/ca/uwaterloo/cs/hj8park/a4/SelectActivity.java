package ca.uwaterloo.cs.hj8park.a4;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.HashSet;
import java.util.Set;

import ca.uwaterloo.cs.hj8park.a4.fragment.LogoutFragment;
import ca.uwaterloo.cs.hj8park.a4.model.Model;

public class SelectActivity extends AppCompatActivity  {
    private Button mLoadButton;
    private Spinner mNumberSpinner1;
    private Model mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        mModel = Model.getInstance();
        try {
            mModel.setUpQuizzes(
                    getResources(),
                    getAssets().open("questions.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mLoadButton = findViewById(R.id.selectLoadButton);
        mNumberSpinner1 = findViewById(R.id.selectNumberSpinner1);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.select_number1, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mNumberSpinner1.setAdapter(adapter);

        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.loadQuiz();
                Intent intent = new Intent(SelectActivity.this, QuestionActivity.class);
                startActivity(intent);
            }
        });

        mNumberSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int numQuestions = Integer.valueOf(parent.getItemAtPosition(position).toString());
                mModel.setNumQuestions(numQuestions);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
