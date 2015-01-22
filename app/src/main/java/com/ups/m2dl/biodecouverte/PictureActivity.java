package com.ups.m2dl.biodecouverte;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


public class PictureActivity extends Activity implements View.OnTouchListener, LocationListener {

    private static final String BASE_PATH = "BioDecouverte/photos/";

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private Uri imageUri;

    private Float posx = 0F;
    private Float posy = 0F;

    private Location location;

    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);

        String date = year + "-" + month + "-" + day + "_" + hour + "h" + min + "m" + sec;

        String storageDirectory = BASE_PATH + date + ".jpg";

        File photo = new File(Environment.getExternalStorageDirectory(), storageDirectory);

        photo.getParentFile().mkdirs();

        imageUri = Uri.fromFile(photo);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Uri selectedImage = imageUri;
                getContentResolver().notifyChange(selectedImage, null);
                try {

                    Toast.makeText(this, selectedImage.toString(), Toast.LENGTH_LONG).show();

                    ImageView imgView = (ImageView) findViewById(R.id.imageView);

                    imgView.setImageURI(imageUri);
                    imgView.setOnTouchListener(this);

                } catch (Exception e) {
                    Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                    Log.e("Camera", e.toString());
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            posx = event.getX();
            posy = event.getY();

            Toast.makeText(this, "POI defined at " + posx + "," + posy, Toast.LENGTH_LONG).show();
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        takePhoto();
    }

    public void validatePointOfInterest(View view) {
        //Save username
        SharedPreferences settings = getSharedPreferences(IndexActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        TextView usernameText = (TextView) findViewById(R.id.usernameText);
        editor.putString(IndexActivity.PREFS_URI,imageUri.toString());
        editor.putString(IndexActivity.PREFS_METADATA,location.toString());
        editor.putString(IndexActivity.PREFS_DATE_TAKEN,new Date().toString());
        editor.putString(IndexActivity.PREFS_POI_X,posx.toString());
        editor.putString(IndexActivity.PREFS_POI_Y,posy.toString());

        editor.commit();

        Intent intent = new Intent(this, CommentActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture, menu);
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

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
