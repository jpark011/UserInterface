package ca.uwaterloo.cs.hj8park.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.channels.SelectableChannel;

import ca.uwaterloo.cs.hj8park.a4.model.Model;

public class WelcomeActivity extends AppCompatActivity {
    private EditText mNameEditText;
    private Button mNextButton;
    private Model mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mModel = Model.getInstance();

        mNameEditText = findViewById(R.id.nameEditText);
        mNextButton = findViewById(R.id.welcomeNext);

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNextButton.setEnabled( 0 < s.length() );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mNameEditText.getText().toString();
                mModel.setUserName(userName);

                Intent intent = new Intent(WelcomeActivity.this, SelectActivity.class);
                startActivity(intent);
            }
        });
    }


}
