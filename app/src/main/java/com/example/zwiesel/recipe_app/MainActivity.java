package com.example.zwiesel.recipe_app;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends ActionBarActivity {

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_Layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_action_menu_dark,
                R.string.drawer_open,
                R.string.drawer_close);

        // Draweritems
        mNavItems.add(new NavItem("Home", R.drawable.ic_action_home_dark));
        mNavItems.add(new NavItem("Add Recipe", R.drawable.ic_action_add_dark));
        mNavItems.add(new NavItem("Appetizer", R.drawable.ic_action_cat_appetizer));
        mNavItems.add(new NavItem("Entree", R.drawable.ic_action_cat_entree));
        mNavItems.add(new NavItem("Dessert", R.drawable.ic_action_cat_dessert));

        // Populate the Navigation Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.navigation_Content);
        mDrawerList = (ListView) findViewById(R.id.navigation_List);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //loadIntoArrayList();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    // Actions for the different items on the drawer
    public void selectItemFromDrawer(int position){
        switch (mNavItems.get(position).mTitle){
            case "Add Recipe":
                openAddRecipe();
            default:
                mDrawerLayout.closeDrawer(mDrawerPane);
        }
    }

    public void openAddRecipe(){
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
    }

   @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    public void loadIntoArrayList(){

        try{
            InputStream inputXML = openFileInput("recipes.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputXML);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("recipe");

            //for(int i = 0; i < nList.getLength(); i++){
                //Node
            //}
        }
        catch (ParserConfigurationException pcEx){
            Toast.makeText(this, "Parser Configuration Exception", Toast.LENGTH_LONG).show();
        }
        catch (FileNotFoundException fnfEx){
            Toast.makeText(this, "File Not Found", Toast.LENGTH_LONG).show();
        }
        catch (IOException ioEx){
            Toast.makeText(this, "IOException", Toast.LENGTH_LONG).show();
        }
        catch (SAXException SAXEx){
            Toast.makeText(this, "SAXException", Toast.LENGTH_LONG).show();
        }
    }

    // Inner class for the DrawerListAdapter
    class NavItem {
        String mTitle;
        int mIcon;

        public NavItem(String title, int icon) {
            mTitle = title;
            mIcon = icon;
        }
    }

    // Inner class DrawerListAdapter needed for the drawer and populated by the NavItems
    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        public int getCount() {
            return mNavItems.size();
        }

        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.nav_List_item_title);
            ImageView iconView = (ImageView) view.findViewById(R.id.nav_List_item_icon);

            titleView.setText( mNavItems.get(position).mTitle);
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }
}
