package com.study.android.earlymap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.study.android.earlymap.EditListAdapter.EditItemView;
import com.study.android.earlymap.EditListAdapter.RouteEditAdapter;
import com.study.android.earlymap.SeeListAdapter.RouteItemView;
import com.study.android.earlymap.SeeListAdapter.RouteListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tatsuya on 2018/03/31.
 */

public class RouteEditActivity extends AppCompatActivity {

    RecyclerView routeListView;
    RouteEditAdapter routeEditAdapter;
    EditText desEdit;
    ArrayList<RouteItemView> list=new ArrayList<>();
    ArrayList<RouteItemView> intent_list=new ArrayList<>();
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        intent=getIntent();
        list=(ArrayList<RouteItemView>) intent.getSerializableExtra("LIST");
        routeListView=findViewById(R.id.route_list_edit);
        routeEditAdapter=new RouteEditAdapter(this,list);
        routeListView.setAdapter(routeEditAdapter);
        routeListView.setLayoutManager(new LinearLayoutManager(this));


        Toolbar myToolbar =findViewById(R.id.editview_toolbar);
        setSupportActionBar(myToolbar);

        desEdit=findViewById(R.id.des_edit);
        Button addButton=findViewById(R.id.add_button);
        addButton.setOnClickListener(addButtonClick);
        routeEditAdapter.setOnLongClickListener(listLongClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu_layout,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if(id==R.id.detail_icon){
            new AlertDialog.Builder(RouteEditActivity.this)
                    .setTitle("確認")
                    .setMessage("このリストを保存しますか（後からでも編集できます）")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        intent.putExtra("LIST",list);
                        setResult(RESULT_OK,intent);
                        finish();
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();
        }
        return true;
    }

   public View.OnLongClickListener listLongClick=new View.OnLongClickListener() {
       @Override
       public boolean onLongClick(View v) {

           final int pos =routeListView.getChildAdapterPosition(v);
           new AlertDialog.Builder(RouteEditActivity.this)
                   .setTitle("確認")
                   .setMessage(list.get(pos).getDestination()+"を削除しますか？")
                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           list.remove(pos);
                           routeEditAdapter.refreshItem(list);
                           Toast.makeText(RouteEditActivity.this,"削除",Toast.LENGTH_LONG).show();
                       }
                   })
                   .setNegativeButton("Cancel",null)
                   .show();
           return false;
       }

   };

    public View.OnClickListener addButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String searchText=desEdit.getText().toString();
            if(searchText.length()!=0){
                addList(searchText);
                desEdit.getEditableText().clear();
                Toast.makeText(RouteEditActivity.this,"追加しました",Toast.LENGTH_LONG).show();
            }
        }
    };

    public void addList(String desName){
        RouteItemView listItem=new RouteItemView();
        listItem.setDestination(desName);
        list.add(listItem);
        routeEditAdapter.refreshItem(list);
    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//        AlertDialog.Builder builder= new AlertDialog.Builder(RouteEditActivity.this);
//               builder.setTitle("確認")
//                .setMessage("前の画面に移動しますか？（保存はされません）")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                })
//                .setNegativeButton("Cancel",null);
//
//        return builder.create();
//    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder= new AlertDialog.Builder(RouteEditActivity.this);
        builder.setTitle("確認")
                .setMessage("前の画面に移動しますか？（保存はされません）")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }
}
