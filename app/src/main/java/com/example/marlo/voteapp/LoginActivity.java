package com.example.marlo.voteapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity
{

    //region [ Private Fields ]

    private EditText voterRegistrationEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private View.OnClickListener onLoginClickListener;

    //endregion

    //region [ Life Cycle ]

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setupContentView();
        setupListeners();
        setupEvents();
    }


    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onStop()
    {
        //dataBase.close();
        super.onStop();
    }

    //endregion

    //region [ Setup Activity ]

    private void setupContentView()
    {
        setContentView(R.layout.activity_login);
        this.voterRegistrationEditText = (EditText)findViewById(R.id.voterRegistrationEditText);
        this.passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        this.loginButton = (Button)findViewById(R.id.loginButton);
    }

    private void setupEvents()
    {
        this.loginButton.setOnClickListener(onLoginClickListener);
    }

    private void setupListeners()
    {
        this.onLoginClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String registration = voterRegistrationEditText.getText().toString();
                String pass = passwordEditText.getText().toString();
                if (!registration.isEmpty() && !pass.isEmpty())
                {
                    int userId = authenticate(registration, pass);
                    if (userId == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Invalid credentials. Try again.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        goToMainActivity(userId);
                    }

                }
            }
        };
    }

    //endregion

    //region [ Private Methods ]

    private void goToMainActivity(int userId)
    {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle params = new Bundle();

        params.putInt("userId",userId);

        intent.putExtras(params);
        startActivity(intent);
    }

    private int authenticate(String registration, String pass)
    {
        /* Connect to server here and authenticate */
        /* return CourierID */
        return 1;
    }

    //endregion

}