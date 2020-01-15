package dev.zmq.duckseatinsect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class result extends AppCompatActivity
{
private TextView scoreLabel;
private  TextView highScoreLabel;
private Button Exit;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        scoreLabel=(TextView)findViewById(R.id.textView_150);
        highScoreLabel=(TextView)findViewById(R.id.textView_High_Score);
        Exit=(Button)findViewById(R.id.btn_exit);


        int score=getIntent().getIntExtra("score",0);
        scoreLabel.setText(score+"");

        SharedPreferences setting=getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore=setting.getInt("HIGH_SCORE",0);

        if (score>highScore)
        {
            highScoreLabel.setText("High Score:"+score);
            //Save Score
            SharedPreferences.Editor editor=setting.edit();
            editor.putInt("HIGH_SCORE",score);
            editor.commit();
        }
        else
        {
            highScoreLabel.setText("High Score:"+highScore);

        }


        Exit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                System.exit(0);
            }
        });

    }
    public void tryAgain(View view)
    {
        startActivity(new Intent(getApplicationContext(),start.class));
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
