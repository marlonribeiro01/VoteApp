package com.example.marlo.voteapp.Activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.marlo.voteapp.Helpers.ListCandidateAdapter;
import com.example.marlo.voteapp.Helpers.StaticHelper;
import com.example.marlo.voteapp.Models.Candidate;
import com.example.marlo.voteapp.R;

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

    //endregion

    //region [ Setup Activity ]

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setupContentView();
        this.getParams();
        this.setupListAdapter();
        this.setupListeners();
        this.setupEventHandlers();
    }

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
        this.adapter = new ListCandidateAdapter(this, itemsSource);
        this.listView.setAdapter((this.adapter));
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

}