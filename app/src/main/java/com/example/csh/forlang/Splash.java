package com.example.csh.forlang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            Thread.sleep(1000);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("state", "launch");
            startActivity(intent);
            finish();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
