package sample.com.demogalary;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;

public class NewPhotoActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    ImageView mImageView;
    TextView mTv;
    TextView mTv2;
    Button updateButton;
    double mLongitude, mLatitude;
    String mAddress;
    Bitmap mBitmap;
    LocationManager mLocationManager;
    AddressResultReceiver mResultReceiver;
    private GoogleApiClient mGoogleApiClient;
    Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_photo);
        mImageView = (ImageView)findViewById(R.id.newImage);
        mTv =(TextView)findViewById(R.id.loc);
        mTv2 =(TextView)findViewById(R.id.address);
        updateButton = (Button)findViewById(R.id.updateButton);
        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        Bundle extras = getIntent().getBundleExtra("image");
        mBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(mBitmap);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mTv.setText("Acquiring location...");
        mTv2.setText("Acquiring address...");
        mResultReceiver = new AddressResultReceiver(new Handler());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Amit", "onResume");
        //mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        //mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        //mLocationManager.removeUpdates(mLocationListener);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            Log.d("Amit", " " + mLatitude + " " + mLongitude);
            mTv.setText(mLatitude + " " + mLongitude);
            //startIntentService(location);
            updateButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.d("Amit","status changed"+s);
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.d("Amit","provider enabled"+s);
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.d("Amit","provider disabled"+s);

        }
    };

    public void updateClicked(View v) {
        ContentValues cv = new ContentValues();
        cv.put(ImageContentProvider.ADDRESS, mAddress);
        cv.put(ImageContentProvider.LOC, "Long: "+mLongitude+", Lat: "+mLatitude);
        cv.put(ImageContentProvider.BITMAP, getBytes(mBitmap));
        getContentResolver().insert(ImageContentProvider.CONTENT_URI, cv);
        mBitmap.recycle();
        mBitmap = null;
        finish();
    }

    public void cancelClicked(View v) {
        finish();
    }

    private  static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    protected void startIntentService(Location location) {
        Intent intent = new Intent(this, AddressFinderService.class);
        intent.putExtra("RECEIVER", mResultReceiver);
        intent.putExtra("LOCATION_DATA_EXTRA", location);
        startService(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("Amit", "Connected to Google API client");
        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        mLatitude = mLastKnownLocation.getLatitude();
        mLongitude = mLastKnownLocation.getLongitude();
        Log.d("Amit", "long "+mLongitude+ "lat "+ mLatitude);
        mTv.setText(mLatitude + " " + mLongitude);
        startIntentService(mLastKnownLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Amit", "Disconnected to Google API client");
        mGoogleApiClient.connect();
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            String addressOutput = resultData.getString("RESULT_DATA_KEY");
            //Log.d("Amit", Thread.currentThread().toString());
            mAddress = addressOutput;
            mTv2.setText(addressOutput);
            updateButton.setVisibility(View.VISIBLE);

        }
    }
}
