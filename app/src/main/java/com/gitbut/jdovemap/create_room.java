package com.gitbut.jdovemap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class create_room extends AppCompatActivity {
   public EditText crname;
   public EditText crnumber;
   public TextView crlatitude;
   public TextView crlongitude;
    public String cname;
    public String cnumber;
    public String clatitude;
    public String clongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        //데이터를 추가할 때 사용될 에딧 텍스트
        Intent intent = getIntent();
        Double wee = intent.getDoubleExtra("위도", 1);
        Double gyeong = intent.getDoubleExtra("경도", 1);
        crname  = (EditText) findViewById(R.id.cname);
        crlatitude= (TextView) findViewById(R.id.cwee);
        crnumber = (EditText) findViewById(R.id.cnumber);
        crlongitude  = (TextView) findViewById(R.id.cgyeong);

        Button imageButton = (Button) findViewById(R.id.btn1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cname   = crname.getText().toString();
                cnumber = crnumber.getText().toString();
                clatitude= crlatitude.getText().toString();
                clongitude = crlongitude.getText().toString();
                Intent intent = new Intent(getApplicationContext(), listshow.class);
                intent.putExtra("방이름" , cname);
                intent.putExtra("번호" , cnumber);
                intent.putExtra("위도" , clatitude);
                intent.putExtra("경도" , clongitude);
                startActivity(intent);
            }
        });

        Button imageButton2 = (Button) findViewById(R.id.btn2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a=wee.toString();
                crlatitude.setText(a);
                String b=gyeong.toString();
                crlongitude.setText(b);
            }
        });
    }
}
