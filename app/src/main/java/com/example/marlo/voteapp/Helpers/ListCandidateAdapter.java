package com.example.marlo.voteapp.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marlo.voteapp.R;
import com.example.marlo.voteapp.Models.Candidate;

import java.util.ArrayList;


public class ListCandidateAdapter extends ArrayAdapter <Candidate>
{

    //region [ Private Fields ]

    private Context context;

    //endregion

    //region [ Constructors ]

    public ListCandidateAdapter(Context context, ArrayList<Candidate> candidates)
    {
        super(context, 0, candidates);
        this.context = context;
    }

    //endregion

    //region [ ArrayAdapter Overrides ]

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Candidate candidate = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell, parent, false);
        }

        TextView candidateNameView = (TextView) convertView.findViewById(R.id.candidateNameView);
        TextView candidatePartyView = (TextView) convertView.findViewById(R.id.candidatePartyView);
        ImageView candidateImageView =  (ImageView) convertView.findViewById(R.id.candidateImageView);

        DownloadImageTask downloadImageTask = new DownloadImageTask(candidateImageView,context);
        downloadImageTask.execute(candidate.getImage());
        candidateNameView.setText(candidate.getName());
        candidatePartyView.setText(candidate.getParty());
        //itemImageView.setImageResource(candidate.getImage());

        return convertView;
    }

    //endregion

}
