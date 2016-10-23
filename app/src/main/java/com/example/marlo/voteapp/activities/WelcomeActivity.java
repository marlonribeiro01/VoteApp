package com.example.marlo.voteapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.marlo.voteapp.Models.Candidate;
import com.example.marlo.voteapp.R;

public class WelcomeActivity extends AppCompatActivity
{

    //region [ Setup Activity ]

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    //endregion

    // region [ Setup Menu ]

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_welcome, menu);
        return true;
    }

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

    private void goToList(Candidate.CandidateType candidateType)
    {
        Intent intent = new Intent(this, ListActivity.class);
        Bundle params = new Bundle();
        params.putString("candidateType",candidateType.name());
        params.putBoolean("isLookup", false);
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
