package com.study.android.earlymap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import com.study.android.earlymap.Dialog.DeleteDialog;
import com.study.android.earlymap.Dialog.SaveingDialog;
import com.study.android.earlymap.Dialog.WarningDialog;
import com.study.android.earlymap.EditListAdapter.EditItemView;
import com.study.android.earlymap.EditListAdapter.RouteEditAdapter;
import com.study.android.earlymap.SeeListAdapter.RouteItemView;
import com.study.android.earlymap.SeeListAdapter.RouteListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tatsuya on 2018/03/31.
 */

public class RouteEditActivity extends AppCompatActivity
        implements WarningDialog.WarningDialogListener ,
        SaveingDialog.SaveingDialogListener,DeleteDialog.DeleteDialogListener
{

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
        if(id==R.id.detail_icon) {
            DialogFragment saveDialog = new SaveingDialog();
            saveDialog.show(getSupportFragmentManager(), "SaveingDialog");
        }
        return true;
    }

   public View.OnLongClickListener listLongClick=new View.OnLongClickListener() {
       @Override
       public boolean onLongClick(View v) {
           final int pos =routeListView.getChildAdapterPosition(v);
           DialogFragment deleteDialog=new DeleteDialog();
           Bundle bundle=new Bundle();
           bundle.putInt("DELETE_KEY",pos);
           deleteDialog.setArguments(bundle);
           deleteDialog.show(getSupportFragmentManager(),"DeleteDialog");
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

    @Override
    public void onBackPressed() {
        DialogFragment warnDialog=new WarningDialog();
        warnDialog.show(getSupportFragmentManager(),"WarningDialog");
    }

    @Override
    public void onWarnDialogClick(DialogFragment dialogFragment) {
        finish();
    }

    @Override
    public void onSaveDialogClick(DialogFragment dialogFragment) {
        intent.putExtra("LIST",list);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onDeleteDialogClick(DialogFragment dialogFragment,int pos) {
        list.remove(pos);
        routeEditAdapter.refreshItem(list);
        Toast.makeText(RouteEditActivity.this,"削除",Toast.LENGTH_LONG).show();
    }
}
