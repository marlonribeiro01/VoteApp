package com.example.marlo.voteapp.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

/**
 * Created by marlo on 23/10/2016.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
{

    //region [ Private Fields ]

    private Context context;
    private ImageView imageView;
    private ProgressDialog progressDialog;
    private boolean showLoading;

    //endregion

    //region [ Constructors ]

    public DownloadImageTask(ImageView imageView, Context context, boolean showLoading)
    {
        this.imageView = imageView;
        this.context = context;
        this.showLoading = showLoading;
    }

    //endregion

    //region [ AsyncTask Overrides ]

    @Override
    protected Bitmap doInBackground(String... urls)
    {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try
        {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        }
        catch (Exception e)
        {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        imageView.setImageBitmap(result);
        if(showLoading)
            if(progressDialog.isShowing())
                progressDialog.dismiss();
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        if(showLoading)
        {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading image...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }

    //endregion

}
