package com.example.zhao.fivechess;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private WuziqiPanel wuziqiPanel;
    private Button myBtn_white;
    private Button myBtn_black;
    private Button myBtn_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wuziqiPanel = (WuziqiPanel) findViewById(R.id.id_wuziqi);

        myBtn_white = (Button)findViewById(R.id.btn_white);
        myBtn_black = (Button)findViewById(R.id.btn_black);
        myBtn_exit  = (Button)findViewById(R.id.btn_exit);

        myBtn_white.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                wuziqiPanel.reMoveWhite();
            }
        });

        myBtn_black.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                wuziqiPanel.reMoveBlack();
            }
        });

        myBtn_exit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                System.exit(0);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            wuziqiPanel.restart();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
