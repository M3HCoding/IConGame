package dev.zmq.duckseatinsect;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{
    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView yellow;
    private ImageView green;
    private ImageView red;

    private int frameHeight;
    private int boxSize;

    private int boxY;

    private int yellowX;
    private int yellowY;
    private int greenX;
    private int greenY;
    private int redX;
    private int redY;

    private int screenWidth;
    private int screenHeight;

    private int score=0;

    private SoundPlayer sound;
    private int boxSpeed;
    private int yellowSpeed;
    private int greenSpeed;
    private int redSpeed;

    private boolean action_flag=false;
    private boolean start_flag=false;

    private Handler handler=new Handler();
    Timer timer=new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sound=new SoundPlayer(this);

        scoreLabel=(TextView)findViewById(R.id.scoreLabel);
        startLabel=(TextView)findViewById(R.id.startLabel);

        box=(ImageView)findViewById(R.id.box);
        green=(ImageView)findViewById(R.id.face1);
        yellow=(ImageView)findViewById(R.id.face2);
        red=(ImageView)findViewById(R.id.face3);

        //Get Screen size
        WindowManager windowManager=getWindowManager();
        Display display=windowManager.getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);

        screenWidth=size.x;
        screenHeight=size.y;

        //now
        //Nexus4 width:768,height:1184
        //speed BoxSpeed:10,yellowSpped:10,greenSpeed:12,redSpeed:11

        boxSpeed=Math.round(screenHeight/75F);//1184/75=15.786666667==>16
        yellowSpeed=Math.round(screenHeight/90F);//768/90=8.533==>8
        greenSpeed=Math.round(screenHeight/80F);//768/80=9.6==>10
        redSpeed=Math.round(screenHeight/70F);//768/70=10.97777==>11

        Log.v("SPEED_BOX",boxSpeed+"");
        Log.v("YELLOW_BOX",yellowSpeed+"");
        Log.v("GREEN_BOX",greenSpeed+"");
        Log.v("RED_BOX",redSpeed+"");

        //move to out of screen
        yellow.setX(-80);
        yellow.setX(-80);
        green.setX(-80);
        green.setX(-80);
        red.setX(-80);
        red.setX(-80);

    scoreLabel.setText("score:0");

    }
    public void changePos()
    {
        hitCheck();
        //yellow ball
        yellowX-=yellowSpeed;
        if(yellowX<0)
        {
            yellowX=screenWidth+20;
            yellowY=(int) Math.floor(Math.random()*(frameHeight-yellow.getHeight()));

        }
        yellow.setX(yellowX);
        yellow.setY(yellowY);

        //green ball
        greenX-=greenSpeed;
        if(greenX<0)
        {
            greenX=screenWidth+10;
            greenY=(int) Math.floor(Math.random()*(frameHeight-green.getHeight()));

        }

        green.setX(greenX);
        green.setY(greenY);

        //red ball
        redX-=redSpeed;
        if(redX<0)
        {
            redX=screenWidth+10;
            redY=(int) Math.floor(Math.random()*(frameHeight-red.getHeight()));

        }

        red.setX(redX);
        red.setY(redY);


        if (action_flag==true)
        {
            //Touching
            boxY-=boxSpeed;

        }else
        {
            //Releasing
            boxY+=boxSpeed;

        }
        if(boxY<0) boxY=0;
        if (boxY>frameHeight-boxSize)boxY=frameHeight-boxSize;

         box.setY(boxY);
        scoreLabel.setText("score:"+score);

    }
    public void hitCheck()
    {
        //if center of the ball is in the box

        //for Yellow
        int yellowCenterX=yellowX+yellow.getWidth()/2;
        int yellowCenterY=yellowY+yellow.getHeight()/2;

        //<=yellowCenterX<=boxWidth
        //boxY<= yellowCenterY<=boxY+boxHeight

        if (0 <= yellowCenterX && yellowCenterX <= boxSize
                && boxY <= yellowCenterY && yellowCenterY <= boxY + boxSize)
        {
            score+=10;
            yellowX=-10;
            sound.playHitSound();
        }

        //for green
        int greenCenterX=greenX+green.getWidth()/2;
        int greenCenterY=greenY+green.getHeight()/2;

        if (0<= greenCenterX && greenCenterX <=
                boxSize && boxY <= greenCenterY && greenCenterY<=boxY+boxSize)
        {
            score+=20;
            greenX=-20;
            sound.playHitSound();
        }

        //for red
        int redCenterX=redX+red.getWidth()/2;
        int redCenterY=redY+red.getHeight()/2;

        if (0<= redCenterX && redCenterX <= boxSize
                && boxY <= redCenterY && redCenterY<=boxY+boxSize)
        {
            //stop
            timer.cancel();
            timer=null;
            sound.playOverSound();


            //show result
            //this intent acticity refered to result activity
            Intent intent=new Intent(getApplicationContext(),result.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("score",score);
            startActivity(intent);
            }
        }


    public boolean onTouchEvent(MotionEvent me)
    {
        if (start_flag==false)
        {
        start_flag=true;
            FrameLayout frame=(FrameLayout)findViewById(R.id.frame);
            frameHeight=frame.getHeight();
            boxY=(int) box.getY();
            boxSize=box.getHeight();


        startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            },0,20);
        }
        else
        {
            if (me.getAction()==MotionEvent.ACTION_DOWN)
            {
                action_flag=true;

            }
            else if (me.getAction()==MotionEvent.ACTION_UP)
            {
                action_flag=false;
            }
        }

        return  true;
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
