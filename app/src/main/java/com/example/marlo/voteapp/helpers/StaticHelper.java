package com.example.marlo.voteapp.Helpers;

import com.example.marlo.voteapp.Models.Candidate;
import com.example.marlo.voteapp.Models.Elector;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by marlo on 20/10/2016.
 */

public class StaticHelper
{

    public static ArrayList<Candidate> Candidates = new ArrayList<Candidate>(Arrays.asList(
            new Candidate(1, "Marlon", "ALDERMAN", "Whatever", Candidate.CandidateType.ALDERMAN),
            new Candidate(2, "Mari", "ALDERMAN", "Whatever", Candidate.CandidateType.ALDERMAN),
            new Candidate(1, "Marlon", "MAYOR", "Whatever", Candidate.CandidateType.MAYOR),
            new Candidate(2, "Mari", "MAYOR", "Whatever", Candidate.CandidateType.MAYOR)
    ));

    public static Elector CurrentElector = new Elector(1);
}
