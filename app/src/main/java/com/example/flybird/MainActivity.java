package com.example.flybird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    ImageView background;
    ImageView start;
    ImageView bird;
    ImageView end;
    ImageView column;
    TextView score2;
    DrawerLayout drawerLayout;

    private final int clickToRun=0;
    private final int Run=1;
    private final int clickToOver=2;
    private final int clickToStart=3;
    private int state=0;

    float initbirdX;
    float initbirdY=0;
    float initcolumnX;
    float initcolumnY=0;

    float birdY=0;
    float columnX=0;
    float columnY=0;
    float ground=600;

    int score=0;

    private final Handler handler = new Handler();

    Random r=new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            Run();
        }
    }
    private void Run() {
        background = (ImageView) findViewById(R.id.background);
        start = (ImageView) findViewById(R.id.start);
        bird = (ImageView) findViewById(R.id.bird);
        end = (ImageView) findViewById(R.id.end);
        column = (ImageView) findViewById(R.id.column);
        score2 = (TextView) findViewById(R.id.score2);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        initbirdX = (bird.getTranslationX() - 100);
        initcolumnX = (column.getTranslationX() + 500);
        bird.setTranslationX(initbirdX);
        column.setTranslationX(initcolumnX);

        bird.setVisibility(View.GONE);
        end.setVisibility(View.GONE);
        column.setVisibility(View.GONE);


        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (state) {
                    case clickToRun:
                        score = 0;
                        end.setVisibility(View.GONE);
                        start.setVisibility(View.GONE);
                        bird.setVisibility(View.VISIBLE);
                        column.setVisibility(View.VISIBLE);
                        //游戏运行中
                        state = Run;
                        birdRun();
                        break;
                    case Run:
                        for (int i = 0; i < 1000; i++) {
                            bird.setTranslationY(bird.getTranslationY() - 0.2f);
                        }
                        break;
                    case clickToOver:
                        end.setVisibility(View.VISIBLE);
                        column.setVisibility(View.GONE);
                        //展示分数
                        score2.setText(String.valueOf(score));
                        score2.setVisibility(View.VISIBLE);
                        //存入数据库
                        saveDB();
                        state = clickToStart;
                        break;
                    case clickToStart:
                        drawerLayout.openDrawer(GravityCompat.START);
                        end.setVisibility(View.GONE);
                        bird.setVisibility(View.GONE);
                        column.setVisibility(View.GONE);
                        start.setVisibility(View.VISIBLE);
                        score2.setVisibility(View.GONE);
                        //复位小鸟柱子
                        birdY = initbirdY;
                        columnX = initcolumnX;
                        columnY = initcolumnY;

                        bird.setTranslationY(birdY);
                        column.setTranslationX(columnX);
                        column.setTranslationY(columnY);

                        state = clickToRun;
                        break;
                    default:
                        break;
                }
            }
        });

    }
    private void saveDB() {
        Score scoredb=new Score();
        scoredb.setScore(score);
        scoredb.save();

    }


    private void birdRun() {
        handler.removeCallbacks(birdColumnMove);
        handler.postDelayed(birdColumnMove, 10);
    }

    private final Runnable birdColumnMove = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            birdY=bird.getTranslationY();
            columnX=column.getTranslationX();
            columnY=column.getTranslationY();
            if(columnX<(initbirdX-200)){
                columnX=initcolumnX;
                columnY=(r.nextInt(600)-300);
                column.setTranslationX(columnX);
                column.setTranslationY(columnY);
            }


            if(birdY<600&&birdY>-1100) {
                if (state == Run) {
                    if(bird.getTranslationX()==columnX){
                        if(birdY<(columnY-100)||birdY>(columnY+100)){

                            state=clickToOver;
                            Log.d("collision","abc"+bird.getTranslationX()+birdY+columnY);

                        }else{
                            score+=1;
                            Log.d("DDD","score "+score);
                        }}
                    column.setTranslationX(columnX-8);
                    bird.setTranslationY(birdY + 8);
                    Log.d("AAA", "X is " + bird.getTranslationY());
                    Log.d("bbb", "X is " + column.getTranslationX());
                }

            }else{
                state=clickToOver;
            }handler.postDelayed(this, 10);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Run();
                }else{
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
        }
    }
}