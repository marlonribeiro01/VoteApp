package com.example.marlo.voteapp.Models;

/**
 * Created by marlo on 20/10/2016.
 */

public class Elector
{

    //region [ Private Fields ]

    private int id;
    private String voterRegistration;
    private String password;
    private Integer aldermanId;
    private Integer mayorId;

    //endregion

    //region [ Constructors ]

    public Elector(int id, String voterRegistration, String password, Integer aldermanId, Integer mayorId)
    {
        this.id = id;
        this.voterRegistration = voterRegistration;
        this.password = password;
        this.aldermanId = aldermanId;
        this.mayorId = mayorId;
    }

    public Elector(int id, String voterRegistration, String password)
    {
        this.id = id;
        this.voterRegistration = voterRegistration;
        this.password = password;
    }

    public Elector(int id)
    {
        this.id = id;
    }

    //endregion

    //region [ Getters and Setters ]

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getVoterRegistration()
    {
        return voterRegistration;
    }

    public void setVoterRegistration(String voterRegistration)
    {
        this.voterRegistration = voterRegistration;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getAldermanId()
    {
        return aldermanId;
    }

    public void setAldermanId(int aldermanId)
    {
        this.aldermanId = aldermanId;
    }

    public int getMayorId()
    {
        return mayorId;
    }

    public void setMayorId(int mayorId)
    {
        this.mayorId = mayorId;
    }

    //endregion

}
