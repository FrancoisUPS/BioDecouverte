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
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Identification extends Activity implements View.OnClickListener {
    private static Tree<String> determination = null;
    private String parentNode = null;
    private ListView determinationList;
    private ArrayList<String> currentChoices;
    private Collection<String> childList = null;


    private class Tree<T> {
        public List<T> roots;
        public List<T> allNodes;
        public Map<T,List<T> > children;

        public Tree() {
            allNodes = new ArrayList<T>();
            children = new HashMap<T, List<T> >();
            roots = new ArrayList<T>();
        }

        public void add(T node) {
            roots.add(node);
            allNodes.add(node);
            children.put(node, new ArrayList<T>());
        }

        public void add(T parent, T child) {
            addAll(parent, Arrays.asList(child));
        }

        public void addAll(T parent, List<T> childs) {
            allNodes.addAll(childs);
            children.get(parent).addAll(childs);

            for(T child: childs){
                children.put(child, new ArrayList<T>());
            }
        }

        public List<T> children(T parent) {
            return children.get(parent);
        }

        public List<T> roots() {
            return roots;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: initialize tree from config file
        if(determination == null) {
            determination = new Tree<String>();

            determination.add("Plante");
            determination.add("Animal");
            determination.addAll("Plante", Arrays.asList("Hauteur > 50 cm", "Hauteur < 50 cm"));
            determination.addAll("Animal", Arrays.asList("Invertébré", "Vertébré"));

        }
        setContentView(R.layout.activity_identification);

        //Get previous choices
        currentChoices = (ArrayList)getIntent().getSerializableExtra("currentChoices");
        if(currentChoices == null)
            currentChoices = new ArrayList<String>();

        //parent node == last choice clicked
        if(currentChoices.size() > 0)
            parentNode = currentChoices.get(currentChoices.size()-1);

        //Display children of parent or roots if no choice made yet
        if(parentNode != null)
            childList = determination.children(parentNode);
        else
            childList = determination.roots();

        //display list of choices
        determinationList = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> listAdapter  = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>(childList));
        determinationList.setAdapter(listAdapter);

        //Display choices made in header above listview
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(currentChoices.toString());


        determinationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentChoices.add(new ArrayList<>(childList).get(position));

                Intent intent = new Intent(Identification.this, Identification.class);
                intent.putExtra("currentChoices", currentChoices);
                startActivity(intent);
            }
        });

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
        Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",
                Toast.LENGTH_LONG).show();

        //send everything by mail and save to bd
        
    }
}
