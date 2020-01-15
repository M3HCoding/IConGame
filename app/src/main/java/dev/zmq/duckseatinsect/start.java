package dev.zmq.duckseatinsect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

public class start extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }
    public void startGame(View view)
    {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
    //Diasble return button
    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent)
    {
        if (keyEvent.getAction()==KeyEvent.ACTION_DOWN)
        {
            switch(keyEvent.getKeyCode())
            {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }
}
