package com.example.marlo.voteapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marlo.voteapp.Helpers.DownloadImageTask;
import com.example.marlo.voteapp.Helpers.StaticHelper;
import com.example.marlo.voteapp.Models.Candidate;
import com.example.marlo.voteapp.R;

import java.util.Collections;

import static com.example.marlo.voteapp.R.id.fab;

public class DetailActivity extends AppCompatActivity
{

    //region [ Private Fields ]

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Candidate.CandidateType currentCandidateType;
    private Candidate currentCandidate;
    private TextView aboutTextView;
    private ImageView imageView;

    //endregion

    //region [ Life Cycle ]

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setupContentView();
        getParams();
        setupListeners();
        setTexts();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        DownloadImageTask downloadImageTask = new DownloadImageTask(imageView,DetailActivity.this, true);
        downloadImageTask.execute(currentCandidate.getImage());

    }

    //endregion

    //region [ Setup Activity ]

    private void setupContentView()
    {
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        aboutTextView = (TextView) findViewById(R.id.aboutTextView);
        imageView = (ImageView)findViewById(R.id.imageView);

    }

    private void setupListeners()
    {
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(currentCandidateType.equals(Candidate.CandidateType.ALDERMAN))
                    StaticHelper.CurrentElector.setAldermanId(currentCandidate.getId());
                else if (currentCandidateType.equals(Candidate.CandidateType.MAYOR))
                    StaticHelper.CurrentElector.setMayorId(currentCandidate.getId());

                Toast.makeText(getApplicationContext(), "Your vote was registered!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //endregion

    //region [ Private Methods ]

    private void setTexts()
    {
        String type =  currentCandidateType.toString().substring(0, 1).toUpperCase() +
                currentCandidateType.toString().substring(1).toLowerCase();
        String about = type + " " + currentCandidate.getName()
                + "\n" +  "Party: " + currentCandidate.getParty()
                + "\n" +  "Id: " + currentCandidate.getId();

        aboutTextView.setText(about);
        setTitle(currentCandidate.getName());
    }

    private void getParams()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            Bundle params = intent.getExtras();
            final int candidateId = params.getInt("candidateId");
            String candidateTypeString = params.getString("candidateType");
            this.currentCandidateType = Candidate.CandidateType.valueOf(candidateTypeString);

            for (Candidate c : StaticHelper.Candidates)
            {
                if (c.getType() == this.currentCandidateType && c.getId() == candidateId)
                    this.currentCandidate = c;
            }
        }
    }

    //endregion

}
