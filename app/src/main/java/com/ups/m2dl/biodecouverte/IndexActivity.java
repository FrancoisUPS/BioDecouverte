package com.ups.m2dl.biodecouverte;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class IndexActivity extends Activity {
    public static final String PREFS_NAME = "BioDecouvertePrefs";
    public static final String PREFS_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        //Sets back previous username
        SharedPreferences settings = getSharedPreferences(IndexActivity.PREFS_NAME, 0);
        ((TextView) findViewById(R.id.usernameText)).setText(settings.getString(IndexActivity.PREFS_USERNAME, "Username"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_identification, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchPictureActivity(View view) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        TextView usernameText = (TextView) findViewById(R.id.usernameText);
        editor.putString(PREFS_USERNAME, usernameText.getText().toString());

        // Commit the edits!
        editor.commit();

        Intent intent = new Intent(this, Identification.class);
        startActivity(intent);
    }
}
