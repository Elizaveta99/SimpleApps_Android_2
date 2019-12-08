package com.android.guessnumber8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    private GestureLibrary gLibrary;
    private GestureDetectorCompat mDetector;

    private TextView tvInfo;
    private EditText etInput;
    private Button bControl;
    private Button newGameButton;
    private int number;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private boolean isOver;
    AlertDialog.Builder builder;
    StringBuffer input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDetector = new GestureDetectorCompat(this, new MyGestListener());

        gLibrary =
                GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gLibrary.load()) {
            finish();
        }

        GestureOverlayView gOverlay =
                (GestureOverlayView) findViewById(R.id.gOverlay);
        gOverlay.addOnGesturePerformedListener(this);


        tvInfo = findViewById(R.id.textView);
        etInput = findViewById(R.id.editText);
        bControl = findViewById(R.id.button);
        newGameButton = findViewById(R.id.button2);
        progressBar = findViewById(R.id.progressBar);
        builder = new AlertDialog.Builder(MainActivity.this);

        newGame();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    class MyGestListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public
        boolean onFling(MotionEvent event1, MotionEvent event2,
                        float velocityX, float velocityY)
        //void onLongPress(MotionEvent event)
        {
            //tvOutput.setText("onLongPress: " + event.toString());
            Log.e(LOG_TAG, String.format("stop app"));
            finish();
            System.exit(0);
            return true;
        }
    }



    public void onGesturePerformed(GestureOverlayView overlay, Gesture
            gesture) {
        ArrayList<Prediction> predictions =
                gLibrary.recognize(gesture);

        if (predictions.size() > 0 && predictions.get(0).score > 1.0) {

            String action = predictions.get(0).name;
            Toast.makeText(this, action, Toast.LENGTH_SHORT).show();


            if (predictions.get(0).name.equals("one"))
                input.append("1");
             else if (predictions.get(0).name.equals("two")) {
                input.append("2");
            } else if (predictions.get(0).name.equals("three")) {
                input.append("3");
            } else if (predictions.get(0).name.equals("four")) {
                input.append("4");
            } else if (predictions.get(0).name.equals("five")) {
                input.append("5");
            } else if (predictions.get(0).name.equals("six")) {
                input.append("6");
            } else if (predictions.get(0).name.equals("seven")) {
                input.append("7");
            } else if (predictions.get(0).name.equals("eight")) {
                input.append("8");
            } else if (predictions.get(0).name.equals("nine")) {
                input.append("9");
            } else if (predictions.get(0).name.equals("zero")) {
                input.append("0");
            } else if (predictions.get(0).name.equals("stop"))
            {
                etInput.setText(input.toString());
                Log.e(LOG_TAG, String.format("stop gesture %s", input));
            }
        }

    }


    private void newGame()
    {
        Log.e(LOG_TAG, String.format("new game"));
        progressStatus = 0;
        progressBar.setProgress(progressStatus);
        Random r = new Random();
        int low = 1;
        int high = 10; // 200, 10 to test
        number = r.nextInt(high - low) + low;
        newGameButton.setEnabled(false);
        bControl.setEnabled(true);
        tvInfo.setText(R.string.try_to_guess);
        isOver = false;
        input = new StringBuffer();

    }

    public void enterButton(View view) {
        Log.e(LOG_TAG, String.format("Enter button"));

        input = new StringBuffer();

        progressStatus++;
        if (progressStatus > 10)
        {
            builder.setTitle(getResources().getString(R.string.oops))
                    .setMessage(getResources().getString(R.string.tries_over))
                    .setCancelable(false)
                    .setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    isOver = true;
                                    newGameButton.setEnabled(true);
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {

            progressBar.setProgress(progressStatus);

            if (etInput.getText().toString().equals("")) {
                Log.e(LOG_TAG, String.format("Empty input"));

                builder.setTitle(R.string.error)
                        .setMessage(R.string.empty)
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {

                try {
                    int temp = Integer.parseInt(etInput.getText().toString());

                    if (temp < 1 || temp > 10) {
                        builder.setTitle(R.string.error)
                                .setMessage(R.string.value_borders)
                                .setCancelable(false)
                                .setNegativeButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                        etInput.setText("");
                    } else {
                        if (temp > number) {
                            tvInfo.setText(getResources().getString(R.string.ahead));
                        } else if (temp < number) {
                            tvInfo.setText(getResources().getString(R.string.behind));
                        } else if (temp == number) {
                            tvInfo.setText(getResources().getString(R.string.hit));

                            builder.setTitle(getResources().getString(R.string.hit))
                                    .setMessage(getResources().getString(R.string.hit) + " in " + progressStatus + " tries")
                                    .setCancelable(false)
                                    .setNegativeButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    isOver = true;
                                                    newGameButton.setEnabled(true);
                                                    bControl.setEnabled(false);
                                                    //newGame();
                                                    //etInput.setText("");

                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();


                        }
                    }
                } catch (NumberFormatException e) {
                    builder.setTitle(R.string.error)
                            .setMessage(R.string.value_not_int)
                            .setCancelable(false)
                            .setNegativeButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                    etInput.setText("");
                }
            }
        }
    }

    public void newGame(View view) {
        newGame();
        etInput.setText("");

        //image animation
    }
}
