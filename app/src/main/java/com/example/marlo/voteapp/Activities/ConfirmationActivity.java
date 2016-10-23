package com.example.marlo.voteapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marlo.voteapp.Helpers.ListCandidateAdapter;
import com.example.marlo.voteapp.Helpers.StaticHelper;
import com.example.marlo.voteapp.Models.Candidate;
import com.example.marlo.voteapp.R;

import java.util.ArrayList;

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

    //endregion

    //region [ Private Methods ]

    private void confirmVote()
    {
        if(StaticHelper.CurrentElector.getAldermanId() == 0)
            Toast.makeText(getApplicationContext(), "You have to choose an Alderman.", Toast.LENGTH_SHORT).show();
        else if(StaticHelper.CurrentElector.getMayorId() == 0)
            Toast.makeText(getApplicationContext(), "You have to choose a Mayor.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Vote confirmed!", Toast.LENGTH_SHORT).show();
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

}
