package com.example.marlo.voteapp.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.marlo.voteapp.Helpers.HttpHandler;
import com.example.marlo.voteapp.Helpers.ListCandidateAdapter;
import com.example.marlo.voteapp.Helpers.StaticHelper;
import com.example.marlo.voteapp.Models.Candidate;
import com.example.marlo.voteapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity
{

    //region [ Private Fields ]

    private ListView listView;
    private ArrayList<Candidate> itemsSource;
    private ListCandidateAdapter adapter;
    private AdapterView.OnItemClickListener onClickListener;
    private Candidate.CandidateType candidateType;
    private boolean isLookup;
    private ProgressDialog progressDialog;

    //endregion

    //region [ Life Cycle ]

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setupContentView();
        this.getParams();
        this.setupListAdapter();
        this.setupListeners();
        this.setupEventHandlers();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(this.itemsSource.size() == 0)
        {
            CandidatesReader candidatesReader = new CandidatesReader(ListActivity.this, candidateType);
            candidatesReader.execute();
        }
    }

    //endregion

    //region [ Setup Activity ]

    /* Setup values from content view */
    private void setupContentView()
    {
        setContentView(R.layout.activity_list);
        this.listView = (ListView) findViewById(R.id.listView);
    }

    /* Setup adapter to ListView */
    private void setupListAdapter()
    {
        this.populateItemsSource();
        /*
        progressDialog = new ProgressDialog(ListActivity.this);
        progressDialog.setMessage("Downloading images...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        */

        this.adapter = new ListCandidateAdapter(this, itemsSource);
        this.listView.setAdapter((this.adapter));

        /*
        progressDialog.dismiss();
        */
    }

    /* Setup listeners */
    private void setupListeners()
    {
        onClickListener = new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(isLookup)
                    setVote(itemsSource.get(position));
                else
                    goToDetail(itemsSource.get(position));
            }
        };
    }

    private void setVote(Candidate candidate)
    {
        if(candidateType.equals(Candidate.CandidateType.ALDERMAN))
            StaticHelper.CurrentElector.setAldermanId(candidate.getId());
        else if(candidateType.equals(Candidate.CandidateType.MAYOR))
            StaticHelper.CurrentElector.setMayorId(candidate.getId());
        finish();
    }

    /* Setup event handlers */
    private void setupEventHandlers()
    {
        listView.setOnItemClickListener(onClickListener);
    }

    //endregion

    //region [ Setup Menu ]

    /* Setup menu items */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_welcome, menu);
        if(candidateType == Candidate.CandidateType.ALDERMAN)
            menu.removeItem(R.id.voteForAlderman);
        else if(candidateType == Candidate.CandidateType.MAYOR)
            menu.removeItem(R.id.voteForMayor);
        return true;
    }

    /* Setup menu items events */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.voteForAlderman:
                goToList(Candidate.CandidateType.ALDERMAN);
                break;
            case R.id.voteForMayor:
                goToList(Candidate.CandidateType.MAYOR);
                break;
            case R.id.confirmVote:
                confirmVote();
                break;
            case R.id.exit:
                exit();
                break;
        }
        return true;
    }

    //endregion

    //region [ Private Methods ]

    private void populateItemsSource()
    {
        itemsSource = new ArrayList<Candidate>();

        for(Candidate c : StaticHelper.Candidates)
            if(c.getType() == this.candidateType)
                itemsSource.add(c);
    }

    private void getParams()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            Bundle params = intent.getExtras();

            String type = params.getString("candidateType");
            this.isLookup = params.getBoolean("isLookup");
            this.candidateType = Candidate.CandidateType.valueOf(type);

            if (type.toString().equals("MAYOR"))
                setTitle("Mayors");
            else if (type.toString().equals("ALDERMAN"))
                setTitle("Aldermen");

        }
    }

    private void goToList(Candidate.CandidateType candidateType)
    {
        Intent intent = new Intent(this, ListActivity.class);
        Bundle params = new Bundle();
        params.putString("candidateType", candidateType.name());
        params.putBoolean("isLookup", false);
        intent.putExtras(params);
        startActivity(intent);
    }

    private void goToDetail(Candidate candidate)
    {

        Intent intent = new Intent(this, DetailActivity.class);
        Bundle params = new Bundle();
        params.putString("candidateType", candidate.getType().name());
        params.putInt("candidateId", candidate.getId());
        intent.putExtras(params);
        startActivity(intent);

    }

    private void confirmVote()
    {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        startActivity(intent);
    }

    private void exit()
    {
        Intent i = new Intent (this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    //endregion

    //region [ Candidates Reader ]

    public class CandidatesReader extends AsyncTask<Void, Void, Void>
    {

        private Context context;
        private String url;
        private String arrayName;
        private Candidate.CandidateType candidateType;

        public CandidatesReader(Context context, Candidate.CandidateType candidateType)
        {
            this.context = context;
            this.candidateType = candidateType;

            if (candidateType.equals(Candidate.CandidateType.ALDERMAN))
            {
                this.url = StaticHelper.aldermenUrl;
                this.arrayName = StaticHelper.aldermenArrayName;
            }
            else if (candidateType.equals(Candidate.CandidateType.MAYOR))
            {
                this.url = StaticHelper.mayorsUrl;
                this.arrayName = StaticHelper.mayorsArrayName;
            }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            HttpHandler handler = new HttpHandler();
            String jsonStr = handler.makeServiceCall(url);

            if(jsonStr != null)
            {
                JSONObject jsonObj = null;
                try
                {
                    jsonObj = new JSONObject(jsonStr);
                    JSONArray candidates = jsonObj.getJSONArray(arrayName);

                    for(int i = 0; i < candidates.length(); i++)
                    {
                        JSONObject c = candidates.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("nome");
                        String party = c.getString("partido");
                        String image = c.getString("foto");

                        Candidate candidate = new Candidate(Integer.parseInt(id),name,party,image,candidateType);

                        StaticHelper.Candidates.add(candidate);

                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Downloading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            setupListAdapter();
            if(progressDialog.isShowing())
                progressDialog.dismiss();

        }
    }

    //endregion

}