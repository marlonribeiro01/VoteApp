package com.example.marlo.voteapp.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marlo.voteapp.R;
import com.example.marlo.voteapp.models.Candidate;

import java.util.ArrayList;


public class ListItemAdapter extends ArrayAdapter <Candidate>
{
    public ListItemAdapter(Context context, ArrayList<Candidate> candidates)
    {
        super(context, 0, candidates);
    }

    /* Inflate layout with Candidate properties */
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

        candidateNameView.setText(candidate.getName());
        candidatePartyView.setText(candidate.getParty());
        //itemImageView.setImageResource(candidate.getImage());

        return convertView;
    }
}
