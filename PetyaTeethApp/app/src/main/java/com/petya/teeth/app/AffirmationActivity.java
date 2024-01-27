package com.petya.teeth.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.petya.teeth.app.handlers.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;


public class AffirmationActivity extends AbstractAppCompatActivity {

    private final static String AFF_ENDPOINT = "https://www.affirmations.dev";

    private TextView textView;
    private Button btn;
    private String affirmation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affirmation);

        this.btn = findViewById(R.id.reloadAffirmation);
        new GetAffirmation().execute();

        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.button_click_animation);
        animator.setTarget(btn);

        this.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animator.start();
                new GetAffirmation().execute();
            }
        });
    }

    private class GetAffirmation extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast toast = Toast.makeText(AffirmationActivity.this, "Preparing affirmation for you!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            btn.setEnabled(false);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(AFF_ENDPOINT);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    affirmation = jsonObj.getString("affirmation");
                } catch (final JSONException e) {
                    //TODO
                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                        btn.setEnabled(true);
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            textView = findViewById(R.id.affirmationText);
            textView.setText(affirmation);
            btn.setEnabled(true);
        }
    }
}


