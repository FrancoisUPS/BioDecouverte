package com.ups.m2dl.biodecouverte;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.googlecode.jctree.LinkedTree;
import com.googlecode.jctree.NodeNotFoundException;
import com.googlecode.jctree.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class Identification extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static Tree<String> determination = null;
    private String parentNode = null;
    private ListView determinationList;
    private ArrayList<String> currentChoices;
    private Collection<String> childList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: initialize tree from config file
        if(determination == null) {
            determination = new LinkedTree<String>();

            determination.add("root");
            try {
                determination.addAll("root", Arrays.asList("Plante", "Animal"));
                determination.addAll("Plante", Arrays.asList("Hauteur > 50 cm", "Hauteur < 50 cm"));
                determination.addAll("Animal", Arrays.asList("Invertébré", "Vertébré"));
            } catch (NodeNotFoundException e) {
                e.printStackTrace();
            }
        }
        setContentView(R.layout.activity_identification);

        currentChoices = (ArrayList)getIntent().getSerializableExtra("currentChoices");
        if(currentChoices == null)
            currentChoices = new ArrayList<String>();

        if(currentChoices.size() > 0)
            parentNode = currentChoices.get(currentChoices.size()-1);

        determinationList = (ListView) findViewById(R.id.listView);

        try {
            if(parentNode != null)
                childList = determination.children(parentNode);
        } catch (NodeNotFoundException e) {
            e.printStackTrace();
        }

        if(childList == null) {
            try {
                childList = determination.children(determination.root());
            } catch (NodeNotFoundException e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> listAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>(childList));
        determinationList.setAdapter(listAdapter);

        TextView textView = new TextView(getBaseContext());
        textView.setText(currentChoices.toString());
        determinationList.addHeaderView(textView);

        determinationList.setOnItemClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentChoices.add(new ArrayList<>(childList).get(position-1));

        Intent intent = new Intent(this, Identification.class);
        intent.putExtra("currentChoices", currentChoices);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(currentChoices.size() > 0) {
            currentChoices.remove(currentChoices.size() - 1);

            Intent intent = new Intent(this, Identification.class);
            intent.putExtra("currentChoices", currentChoices);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        //Save everything to db and send email
    }
}
