package com.study.android.earlymap;

import android.content.Intent;
import android.net.Uri;
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

import com.study.android.earlymap.SeeListAdapter.RouteItemView;
import com.study.android.earlymap.SeeListAdapter.RouteListAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private RecyclerView routeListView;
    private RouteListAdapter routeListAdapter;
    private ArrayList<RouteItemView> list=new ArrayList<>();
    private Realm saveData;
    private static final int EDITVIEW_JUMP_KEY=1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        routeListView =findViewById(R.id.route_list);
        routeListAdapter=new RouteListAdapter(this,list); //getApplicationContext()とthisは使い分ける
        routeListView.setLayoutManager(new LinearLayoutManager(this));
        routeListView.setAdapter(routeListAdapter);
        routeListAdapter.setListClickListener(clickList);

        saveData.init(this);
        saveData=Realm.getDefaultInstance();
        List<RouteItemView> realmList=getDataLoad();
        for(int i=0;i<realmList.size();i++){
            RouteItemView routeItemView=new RouteItemView();
            routeItemView.setDestination(realmList.get(i).getDestination());
            list.add(routeItemView);
        }

        Toolbar myToolbar =findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //myToolbar.inflateMenu(R.menu.top_menu_layout);

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
                list=(ArrayList<RouteItemView>)data.getSerializableExtra("LIST");
                DataSave(list);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.editview_jump){
            Intent intent=new Intent(MainActivity.this,RouteEditActivity.class);
            intent.putExtra("LIST",list);
            startActivityForResult(intent,EDITVIEW_JUMP_KEY);
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
}
