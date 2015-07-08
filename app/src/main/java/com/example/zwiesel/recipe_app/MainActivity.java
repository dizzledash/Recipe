package com.example.zwiesel.recipe_app;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends ActionBarActivity {

    private ListView mDrawerList, rRecipeList;
    private LinearLayout rMainContentLayout;
    private RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ArrayList<Recipe> rArrayList = new ArrayList<>();
    private ArrayList<NavItem> mNavItems = new ArrayList<>();

    TextView tv1,tv2;
    EditText eT1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // tv1=(TextView)findViewById(R.id.textView1);
        //tv2=(TextView)findViewById(R.id.textView2);

        //tv1=(TextView)findViewById(R.id.textView1);
        //Typeface face= Typeface.createFromAsset(getAssets(), "fonts/coders_crux.ttf");
        //tv1.setTypeface(face);
        //eT1=(EditText)findViewById(R.id.whatever);
        //eT1.setTypeface(face);

       // tv2=(TextView)findViewById(R.id.textView2);
        //Typeface face1= Typeface.createFromAsset(getAssets(), "fonts/coders_crux.ttf");
        //tv2.setTypeface(face1);


        rMainContentLayout = (LinearLayout) findViewById(R.id.main_Content);

        //------------Drawer--------------
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_Layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);

        // Draweritems
        mNavItems.add(new NavItem("Home", R.drawable.ic_action_home_dark));
        mNavItems.add(new NavItem("Add Recipe", R.drawable.ic_action_add_dark));
        mNavItems.add(new NavItem("Appetizer", R.drawable.ic_action_cat_appetizer));
        mNavItems.add(new NavItem("Desserts", R.drawable.ic_action_cat_dessert));
        mNavItems.add(new NavItem("Main Dish", R.drawable.ic_action_cat_entree));
        mNavItems.add(new NavItem("Snacks", R.drawable.ab_solid_example));
        mNavItems.add(new NavItem("Salads", R.drawable.ab_solid_example));
        mNavItems.add(new NavItem("Soups", R.drawable.ab_solid_example));

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




        //-------------Recipelist----------------

        loadRecipesIntoArrayList();
        createRecipeList();





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


    public void loadRecipesIntoArrayList() {

        File file = getFileStreamPath("recipes.xml");

        if (file.exists()) {
            Toast.makeText(this, "File existiert", Toast.LENGTH_SHORT).show();
            try {
                InputStream inputXML = openFileInput("recipes.xml");
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputXML);

                doc.getDocumentElement().normalize();

                NodeList nList = doc.getElementsByTagName("recipe");

                for (int i = 0; i < nList.getLength(); i++) {
                    Node nNode = nList.item(i);


                    Element eElement = (Element) nNode;

                    Recipe nListItem = new Recipe(eElement.getAttribute("name"));
                    NodeList nListIng = eElement.getElementsByTagName("ingredient");

                    for (int j = 0; j < nListIng.getLength(); j++) {
                        Node nNodeIng = nListIng.item(j);
                        Element eElementIng = (Element) nNodeIng;

                        nListItem.addIngredient(eElementIng.getTextContent(),
                                eElementIng.getAttribute("amount"),
                                eElementIng.getAttribute("unit"));
                    }

                    nListItem.setDescription(
                            eElement.getElementsByTagName("description").item(0).getTextContent());
                    rArrayList.add(nListItem);
                }

                Toast.makeText(this, rArrayList.get(0).getDescription(), Toast.LENGTH_SHORT).show();

            }
            catch(ParserConfigurationException pcEx){
                Toast.makeText(this, "Parser Configuration Exception", Toast.LENGTH_LONG).show();
            }
            catch(FileNotFoundException fnfEx){
                Toast.makeText(this, "File Not Found", Toast.LENGTH_LONG).show();
            }
            catch(IOException ioEx){
                Toast.makeText(this, "IOException", Toast.LENGTH_LONG).show();
            }
            catch(SAXException SAXEx){
                Toast.makeText(this, "SAXException", Toast.LENGTH_LONG).show();
            }
        }

        else
            Toast.makeText(this, "Datei nicht vorhanden", Toast.LENGTH_LONG).show();
    }


    //Population of the MainActivity with the saved recipes
    public void createRecipeList(){
        for(int i=0; i<rArrayList.size(); i++){
            TextView rTextView = new TextView(this);
            rTextView.setText(rArrayList.get(i).getName());
            rMainContentLayout.addView(rTextView);
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


    // Inner class DrawerListAdapter, needed by the drawer and populated by the NavItems
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


    /*class RecipeListAdapter extends ArrayAdapter{

        private List<Recipe> rList;
        private Context rContext;

        public RecipeListAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);
            rList = objects;
            rContext = context;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) rContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.r_list_item, null);
            }
            else {
                view = convertView;
            }

            TextView rListItem = (TextView) findViewById(R.id.rList_item_view);
            rListItem.setText(rList.get(position).getName());


            return view;
        }
    }*/
}