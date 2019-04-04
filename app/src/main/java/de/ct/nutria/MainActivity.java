package de.ct.nutria;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import foodSelector.FoodItem;
import de.ct.nutria.foodSelector.FoodItemFragment;
import de.ct.nutria.foodSelector.NutriaRequestCallback;

public class MainActivity extends AppCompatActivity implements FoodItemFragment.OnListSelect, NutriaRequestCallback<String> {

    private TextView mTextMessage;
    private FoodItemFragment foodListFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //foodListFragment = (FoodItemFragment) getFragmentManager().findFragmentById(R.id.FoodList);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onListSelect(FoodItem item) {
        Log.d("Food item in activity", item.getName());
    }


    @Override
    public void updateFromRequest(String result) {

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        return null;
    }

    @Override
    public void onProgressUpdate(int progressCode) {

    }

    @Override
    public void finishRequest() {

    }
}
