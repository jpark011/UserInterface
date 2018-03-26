package ca.uwaterloo.cs.hj8park.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import ca.uwaterloo.cs.hj8park.a4.model.Model;

public class SelectActivity extends AppCompatActivity {
    private Button mLogoutButton;
    private Button mLoadButton;
    private TextView mTopicText;
    private Spinner mNumberSpinner1;
    private Model mModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        mModel = Model.getInstance();

        mLogoutButton = findViewById(R.id.selectLogoutButton);
        mLoadButton = findViewById(R.id.selectLoadButton);
        mTopicText = findViewById(R.id.selectTopicText);
        mNumberSpinner1 = findViewById(R.id.selectNumberSpinner1);

        mTopicText.append(" " + mModel.getUserName());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.select_number1, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mNumberSpinner1.setAdapter(adapter);

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.reset();
                Intent intent = new Intent(SelectActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
