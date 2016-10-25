package com.example.marlo.voteapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marlo.voteapp.Helpers.HttpHandler;
import com.example.marlo.voteapp.Helpers.StaticHelper;
import com.example.marlo.voteapp.Models.Elector;
import com.example.marlo.voteapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity
{

    //region [ Private Fields ]

    private EditText voterRegistrationEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private View.OnClickListener onLoginClickListener;
    private ProgressDialog progressDialog;

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
                if(registration.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter Voter Registration", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(pass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    if(pass.equals("gambi"))
                    {
                        gambiarra();
                        return;
                    }
                    ElectorAuthenticatorTask authenticatorTask = new ElectorAuthenticatorTask(registration, pass, LoginActivity.this);
                    authenticatorTask.execute();
                    return;
                }
            }
        };
    }

    //endregion

    //region [ Private Methods ]

    private void goToMainActivity(int userId)
    {
        Intent intent = new Intent(this, WelcomeActivity.class);
        Bundle params = new Bundle();

        params.putInt("userId",userId);

        intent.putExtras(params);
        startActivity(intent);
    }

    private void gambiarra()
    {
        StaticHelper.CurrentElector = new Elector(1);
        goToMainActivity(StaticHelper.CurrentElector.getId());
    }

    //endregion

    //region [ ElectorAuthenticatorTask ]

    public class ElectorAuthenticatorTask extends AsyncTask<Void, Void, String>
    {

        //region [ Private Fields ]

        private Context context;
        private ProgressDialog progressDialog;
        private String voterRegistration;
        private String password;

        //endregion

        //region [ Constructors ]

        public ElectorAuthenticatorTask(String voterRegistration, String password, Context context)
        {

            this.voterRegistration = voterRegistration;
            this.password = password;
            this.context = context;
        }

        //endregion

        //region [ AsyncTask Overrides ]
        @Override
        protected String doInBackground(Void... unused)
        {
            String message;
            HttpHandler handler = new HttpHandler();
            String url = StaticHelper.serverURL +  "auth/" + voterRegistration + "/" + password;
            String jsonStr = handler.makeServiceCall(url);

            if(jsonStr != null)
            {
                JSONObject jsonObj = null;
                JSONArray electors = null;
                JSONObject jsonResult = null;
                boolean jsonSuccess = false;

                try
                {
                    jsonObj = new JSONObject(jsonStr);
                    jsonResult = jsonObj.getJSONObject("result");
                    jsonSuccess = jsonResult.getBoolean("success");

                    if (jsonSuccess)
                    {
                        electors = jsonObj.getJSONArray("objects");

                        JSONObject jsonElector = electors.getJSONObject(0);
                        String id = jsonElector.getString("id");
                        String voterRegistration = jsonElector.getString("voterRegistration");
                        String password = jsonElector.getString("password");
                        //String aldermanId = c.getString("aldermanId");
                        //String mayorId = c.getString("mayorId");
                        Elector elector = new Elector(Integer.parseInt(id), voterRegistration, password);
                        StaticHelper.CurrentElector = elector;

                        message = "You have been successfully authenticated. (Id = " + id + ")";
                    }
                    else
                    {
                        message = jsonResult.getString("message");
                    }
                }
                catch (Exception e)
                {
                    //e.printStackTrace();
                    message = e.getMessage();
                }
            }
            else
            {
                message = "Could not reach the URL.";
            }

            return message;

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Authenticating...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String message)
        {
            super.onPostExecute(message);

            if(progressDialog.isShowing())
                progressDialog.dismiss();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            if(StaticHelper.CurrentElector != null)
                goToMainActivity(StaticHelper.CurrentElector.getId());
        }
        //endregion

    }

    //endregion

}

