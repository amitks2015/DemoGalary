package sample.com.demogalary;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{
    ListView mList;
    ImageAdapter mAdapter;
    ArrayList<ImageData> mImageList;
    public static final String [] PROJECTION = new String[] {
            ImageContentProvider.ID,
            ImageContentProvider.LOC,
            ImageContentProvider.ADDRESS,
            ImageContentProvider.BITMAP
    };
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = (ListView)findViewById(R.id.imageList);
        mImageList = new ArrayList<>();
        mAdapter = new ImageAdapter(this, mImageList);
        mList.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
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
        if (id == R.id.action_camera) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, 0);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Intent intent = new Intent(this, NewPhotoActivity.class);
            intent.putExtra("image", extras);
            startActivity(intent);
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cl = new CursorLoader(this, ImageContentProvider.CONTENT_URI,
                PROJECTION, null, null, null);
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished");
        if(mImageList != null && !cursor.isClosed()) {
            mImageList.clear();
            Log.d(TAG, "Data count = " + cursor.getCount());
            if(cursor.moveToFirst()) {
                do {
                    ImageData id = new ImageData();
                    id.setCoordinates(cursor.getString(cursor.getColumnIndex(ImageContentProvider.LOC)));
                    id.setLocation(cursor.getString(cursor.getColumnIndex(ImageContentProvider.ADDRESS)));
                    id.setImgResId(cursor.getBlob(cursor.getColumnIndex(ImageContentProvider.BITMAP)));
                    mImageList.add(id);
                } while(cursor.moveToNext());
            }
        }
        Log.d(TAG, "notify data set changed");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
