package com.example.marlo.voteapp.models;

/**
 * Created by marlo on 20/10/2016.
 */


public class Candidate
{

    //region [ Enums ]

    public enum CandidateType
    {
        ALDERMAN,
        MAYOR
    }

    //endregion

    //region [ Private Fields ]

    private int id;
    private String name;
    private String party;
    private String image;
    private CandidateType type;

    //endregion

    //region [ Constructors ]

    public Candidate(int id, String name, String party, String image)
    {
        this.id = id;
        this.name = name;
        this.party = party;
        this.image = image;
    }

    //endregion

    //region [ Getters and Setters]

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getParty()
    {
        return party;
    }

    public void setParty(String party)
    {
        this.party = party;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public CandidateType getType()
    {
        return type;
    }

    public void setType(CandidateType type)
    {
        this.type = type;
    }

    //endregion

}
