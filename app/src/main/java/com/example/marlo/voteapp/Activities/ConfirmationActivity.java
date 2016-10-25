package com.example.marlo.voteapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marlo.voteapp.Helpers.HttpHandler;
import com.example.marlo.voteapp.Helpers.ListCandidateAdapter;
import com.example.marlo.voteapp.Helpers.StaticHelper;
import com.example.marlo.voteapp.Models.Candidate;
import com.example.marlo.voteapp.Models.Elector;
import com.example.marlo.voteapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.R.attr.type;

public class ConfirmationActivity extends AppCompatActivity
{

    //region [ Private Fields ]

    private Button confirmButton;
    private ListView listView;
    private ArrayList<Candidate> itemsSource;
    private ListCandidateAdapter adapter;
    private AdapterView.OnItemClickListener onListItemClickListener;
    private View.OnClickListener onConfirmButtonClickListener;

    //endregion

    //region [ Life Cycle ]

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setupContentView();
        this.setupListAdapter();
        this.setupListeners();
        this.setupEventHandlers();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setupListAdapter();
    }

    //endregion

    //region [ Setup Activity ]

    /* Setup content view */
    private void setupContentView()
    {
        setContentView(R.layout.activity_confirmation);
        this.confirmButton = (Button)findViewById(R.id.confirmButton);
        this.listView = (ListView) findViewById(R.id.listView);
        setTitle("Confirm vote?");
    }

    /* Setup adapter to ListView */
    private void setupListAdapter()
    {
        this.populateItemsSource();
        this.adapter = new ListCandidateAdapter(this, itemsSource);
        this.listView.setAdapter((this.adapter));
    }

    /* Setup listeners */
    private void setupListeners()
    {
        onListItemClickListener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                editCandidate(itemsSource.get(position));
            }
        };

        onConfirmButtonClickListener = new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                confirmVote();
            }
        };

    }

    /* Setup Event Handlers */
    private void setupEventHandlers()
    {
        listView.setOnItemClickListener(onListItemClickListener);
        confirmButton.setOnClickListener(onConfirmButtonClickListener);
    }

    private void sendJson()
    {
        //JSONArray jsonArray = new JSONArray();
        JSONObject jsonElector = new JSONObject();
        try
        {
            Elector e = StaticHelper.CurrentElector;
            jsonElector.put("id", String.valueOf(e.getId()));
            jsonElector.put("voterRegistration", e.getVoterRegistration());
            jsonElector.put("password", e.getPassword());
            jsonElector.put("aldermanId", String.valueOf(e.getAldermanId()));
            jsonElector.put("mayorId", String.valueOf(e.getMayorId()));
            //jsonElector.toJSONArray()
            new SendElectorTask(ConfirmationActivity.this).execute(StaticHelper.serverURL, jsonElector.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //endregion

    //region [ Private Methods ]

    private void confirmVote()
    {
        if(StaticHelper.CurrentElector.getAldermanId() == 0)
            Toast.makeText(getApplicationContext(), "You have to choose an Alderman.", Toast.LENGTH_SHORT).show();
        else if(StaticHelper.CurrentElector.getMayorId() == 0)
            Toast.makeText(getApplicationContext(), "You have to choose a Mayor.", Toast.LENGTH_SHORT).show();
        else
            sendJson();

    }

    private void showAlertDialog(String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(ConfirmationActivity.this).create();
        alertDialog.setTitle("Send to server:");
        alertDialog.setMessage(message);
        alertDialog.show();
    }

    private void editCandidate(Candidate candidate)
    {
        Intent intent = new Intent(this, ListActivity.class);
        Bundle params = new Bundle();
        params.putString("candidateType",candidate.getType().name());
        params.putBoolean("isLookup", true);
        intent.putExtras(params);
        startActivity(intent);
    }

    private void populateItemsSource()
    {
        itemsSource = new ArrayList<Candidate>();
        for(Candidate c : StaticHelper.Candidates)
            if(( StaticHelper.CurrentElector.getAldermanId() == c.getId() && c.getType().equals(Candidate.CandidateType.ALDERMAN)) ||
                    (StaticHelper.CurrentElector.getMayorId() == c.getId() && c.getType().equals(Candidate.CandidateType.MAYOR)))
                itemsSource.add(c);
    }

    //endregion

    //region [ SendElectorTask ]

    private class SendElectorTask extends AsyncTask<String, Void, String>
    {

        //region [ Private Fields ]

        private ProgressDialog progressDialog;
        private Context context;

        //endregion

        //region [ Constructors ]

        private SendElectorTask(Context context)
        {
            this.context = context;
        }

        //endregion

        //region [ AsyncTask Overrides ]

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection urlConnection = null;
            try
            {

                urlConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Accept","application/json");


                OutputStreamWriter printout = new OutputStreamWriter(urlConnection.getOutputStream());
                String jsonObjectStr = params[1];
                printout.write(jsonObjectStr);
                printout.flush();
                printout.close();

                data = urlConnection.getResponseMessage();
                //data = urlConnection.getContent().toString();

            }
            catch (Exception e)
            {
                e.printStackTrace();

            }
            finally
            {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
            showAlertDialog(result);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Sending to server...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        //endregion
    }

    //endregion

}
