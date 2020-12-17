package com.techstarworld.truth_dare;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button button,tru,dar,be;
    private ImageView mImageView,p1,p2,p3,p4;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    DatabaseReference gameref = myRef.child("td");
    private EditText mEditText;
    private Random mRandom = new Random();
    private int lastdirection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.imageView);
       button =  findViewById(R.id.button2);
        dar = findViewById(R.id.button);
        p1 = findViewById(R.id.imageView2);
        p4 = findViewById(R.id.imageView5);
        p3 = findViewById(R.id.imageView4);
        p2 = findViewById(R.id.imageView3);
        mEditText = findViewById(R.id.editText);
        be = findViewById(R.id.button4);
        tru = findViewById(R.id.button3);
        be.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bes =  mEditText.getText().toString();

                if(bes.isEmpty()){
                    mEditText.setError("Please Enter number of players");
                }
                else
                {
                   final int b = Integer.parseInt(bes);
                    mEditText.setVisibility(View.INVISIBLE);
                    be.setVisibility(View.GONE);
                    mImageView.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    p1.setVisibility(View.VISIBLE);
                    p2.setVisibility(View.VISIBLE);
                    p3.setVisibility(View.VISIBLE);
                    p4.setVisibility(View.VISIBLE);
                }
            }
        });
     button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//


                FirebaseApp.initializeApp(MainActivity.this);
                int b = Integer.parseInt(mEditText.getText().toString());
int newDirection = mRandom.nextInt(3600)*360/b;
float pivotx = mImageView.getWidth()/2;
float pivoty = mImageView.getHeight()/2;
               Animation rotate = new RotateAnimation(lastdirection,newDirection,pivotx,pivoty);
                rotate.setDuration(2000);
                rotate.setFillAfter(true);
                    rotate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            button.setEnabled(false);

                        }

                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            button.setVisibility(View.GONE);
                            dar.setVisibility(View.VISIBLE);
                            tru.setVisibility(View.VISIBLE);
                        button.setEnabled(true);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                   lastdirection = newDirection;
                mImageView.startAnimation(rotate);




            }
       });
     dar.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             dar.setVisibility(View.GONE);
             tru.setVisibility(View.GONE);
            int random = mRandom.nextInt(31)+1;

            DatabaseReference val = gameref.child("dare").child(Integer.toString(random));

            val.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String text = dataSnapshot.getValue(String.class);
                    showMessage("Dare",text);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this,"Error "+databaseError,Toast.LENGTH_SHORT).show();
                }
            });


         }
     });
tru.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dar.setVisibility(View.GONE);
        tru.setVisibility(View.GONE);
        int random = mRandom.nextInt(38)+1;

        DatabaseReference val = gameref.child("truth").child(Integer.toString(random));

        val.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String text = dataSnapshot.getValue(String.class);
                showMessage("Truth",text);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"Error "+databaseError,Toast.LENGTH_SHORT).show();
            }
        });


    }
});



    }
    public void showMessage(String title,String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
        dar.setVisibility(View.GONE);
        tru.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
    }
}
