package com.study.android.earlymap.VIew;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.study.android.earlymap.Application.MainApplication;
import com.study.android.earlymap.Model.MapModel;
import com.study.android.earlymap.Model.Routes;
import com.study.android.earlymap.R;
import com.study.android.earlymap.SeeListAdapter.RouteItemView;
import com.study.android.earlymap.SeeListAdapter.RouteListAdapter;
import com.study.android.earlymap.Service.MapService;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private RecyclerView routeListView;
    private RouteListAdapter routeListAdapter;
    private ArrayList<RouteItemView> detinationList=new ArrayList<>();
    private Realm saveData;

    private static final int EDITVIEW_JUMP_KEY =1001;
    private static final int UPDATA_KEY=1002;
    private final String appid = "AIzaSyBOSI4Rd3PEEvgneXnIdmGl9CgdltGUSlg";
    private String mode="walking";

    private LocationManager locationManager;
    private String origin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        routeListView =findViewById(R.id.route_list);
        routeListAdapter=new RouteListAdapter(this,detinationList); //getApplicationContext()とthisは使い分ける
        routeListView.setLayoutManager(new LinearLayoutManager(this));
        routeListView.setAdapter(routeListAdapter);
        routeListAdapter.setListClickListener(clickList);

        RadioGroup modeTab=findViewById(R.id.mode_group);
        modeTab.setOnCheckedChangeListener(modeChangeAction);


        saveData.init(this);
        saveData=Realm.getDefaultInstance();
        List<RouteItemView> realmList=getDataLoad();
        for(int i=0;i<realmList.size();i++){
            RouteItemView routeItemView=new RouteItemView();
            routeItemView.setDestination(realmList.get(i).getDestination());
            detinationList.add(routeItemView);
        }

        Toolbar myToolbar =findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
                    1000);
        } else {
            locationStart();
        }
    }

    public RadioGroup.OnCheckedChangeListener modeChangeAction=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                case R.id.walking_button:
                    mode="walking";
                    Toast.makeText(MainActivity.this,"Walk",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.driving_button:
                    mode="driving";
                    Toast.makeText(MainActivity.this,"drive",Toast.LENGTH_SHORT).show();
                    break;
//                case R.id.bicycling_button:
//                    mode="bicycling";
//                    Toast.makeText(MainActivity.this,"bike",Toast.LENGTH_SHORT).show();
//                    break;
                case R.id.transit_button:
                    mode="transit";
                    Toast.makeText(MainActivity.this,"tran",Toast.LENGTH_SHORT).show();
                    break;
            }
            getTakeTime(detinationList);
        }

    };

    public void locationStart(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debug", "location manager Enabled");
        } else {
            // GPSを設定するように促す
            Intent settingsIntent =
                    new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            Log.d("debug", "not gpsEnable, startActivity");
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);

            Log.d("debug", "checkSelfPermission false");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 20, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 20, this);
    }

    public void getTakeTime(ArrayList<RouteItemView> list){
          final ArrayList<RouteItemView> desList=new ArrayList<>();
       for(int i=0;i<list.size();i++){
           final RouteItemView takeTimeItemView=list.get(i);
           final String destination=list.get(i).getDestination();

           MainApplication mainApplication = new MainApplication();
           MapService mapService = mainApplication.getService();
           mapService.getTakeTime(appid, origin, destination, mode)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<MapModel>() {
                        @Override
                        public void onCompleted() {
                            detinationList=desList;
                            routeListAdapter.refreshItem(detinationList);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(MainActivity.this,"取得できませんでした、しばらくお待ちください",Toast.LENGTH_SHORT).show();
                            takeTimeItemView.setTime("");
                            desList.add(takeTimeItemView);
                            Log.d("hoge2:", e.toString());
                        }

                        @Override
                        public void onNext(MapModel mapModels) {
                            List<Routes> mapList = mapModels.getRoutes();
                            String takeTimeText=String.valueOf(mapList.get(0).getLegs().get(0).getDuration().getText());
                            takeTimeItemView.setTime(takeTimeText);
                            Log.d("listnonakami",String.valueOf(detinationList.size())+" "+takeTimeText);
                            desList.add(takeTimeItemView);
                        }
                    });
        }

    }

    public List<RouteItemView> getDataLoad(){
        RealmResults<RouteItemView> saveresult=saveData.where(RouteItemView.class).findAll();
        return saveresult;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.top_menu_layout,menu);
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==EDITVIEW_JUMP_KEY){
            if (resultCode==RESULT_OK){
                detinationList=(ArrayList<RouteItemView>)data.getSerializableExtra("LIST");
                DataSave(detinationList);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.editview_jump){
            Intent intent=new Intent(MainActivity.this,RouteEditActivity.class);
            intent.putExtra("LIST",detinationList);
            startActivityForResult(intent,EDITVIEW_JUMP_KEY);
        }
        else if(id==R.id.updata_taketime){
            getTakeTime(detinationList);
        }
        return true;
    }

    private View.OnClickListener clickList=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           int position =routeListView.getChildAdapterPosition(v);
            String desName=routeListAdapter.getItem(position).getDestination(); //目的地
            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+desName);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            if(mapIntent.resolveActivity(getPackageManager())!=null){
                startActivity(mapIntent);
            }
        }
    };

    public void DataSave(ArrayList<RouteItemView> list){

        saveData.beginTransaction();
        saveData.delete(RouteItemView.class);
        saveData.commitTransaction();

        for(int i=0;i<list.size();i++){
            saveData.beginTransaction();
            RouteItemView routeItemView=saveData.createObject(RouteItemView.class);
            routeItemView.setDestination(list.get(i).getDestination());
            saveData.commitTransaction();
        }
        routeListAdapter.refreshItem(list);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug","checkSelfPermission true");
                locationStart();
            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this,
                        "これ以上なにもできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onLocationChanged(Location location) {
        // 緯度の表示
        String str1 = String.valueOf(location.getLatitude());
        // 経度の表示
        String str2 = String.valueOf(location.getLongitude());

        origin=str1+","+str2;
        Log.d("origin",origin);
    }

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(MainActivity.this);
    }
}
