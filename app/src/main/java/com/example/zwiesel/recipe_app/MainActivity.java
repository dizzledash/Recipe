package com.example.zwiesel.recipe_app;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
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
import android.widget.BaseAdapter;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private ListView mDrawerList;
    private LinearLayout rLayoutRecList;
    private RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    public static ArrayList<Recipe> rArrayList = new ArrayList<>();
    private ArrayList<NavItem> mNavItems = new ArrayList<>();
    private ArrayList<DisplayItem> rDisplayArrayList = new ArrayList<>();
    public final static String EXTRA_MESSAGE = "com.example.zwiesel.recipe_app.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rLayoutRecList = (LinearLayout) findViewById(R.id.linearLayout_recList);


        //------------Drawer--------------
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_Layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close);

        // Draweritems
        mNavItems.add(new NavItem("Home", R.drawable.ic_action_home_dark));
        mNavItems.add(new NavItem("Add Recipe", R.drawable.ic_action_add_dark));
        mNavItems.add(new NavItem("Appetizer", R.drawable.ic_action_appetizer));
        mNavItems.add(new NavItem("Main Dish", R.drawable.ic_action_meal));
        mNavItems.add(new NavItem("Dessert", R.drawable.ic_action_dessert));
        mNavItems.add(new NavItem("Snacks", R.drawable.ic_action_snack));
        mNavItems.add(new NavItem("Salad", R.drawable.ic_action_salad));
        mNavItems.add(new NavItem("Soup", R.drawable.ic_action_soup));

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

        Intent intent = getIntent();
        boolean recAdded = intent.getBooleanExtra(EXTRA_MESSAGE, false);

        //Check if a new recipe was added, so that we need to reload the array
        //and the displayed recipes
        //if(recAdded)
            loadRecipesIntoArrayList();
        createRecipeList(6);

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

    /**Actions for the different items on the drawer*/
    public void selectItemFromDrawer(int position){
        switch (mNavItems.get(position).mTitle){
            case "Add Recipe":
                openAddRecipe();
                break;
            case "Appetizer":
                createRecipeList(Recipe.CATEGORY_APPETIZER);
                mDrawerLayout.closeDrawer(mDrawerPane);
                break;
            case "Main Dish":
                createRecipeList(Recipe.CATEGORY_MAIN_DISH);
                mDrawerLayout.closeDrawer(mDrawerPane);
                break;
            case "Dessert":
                createRecipeList(Recipe.CATEGORY_DESSERT);
                mDrawerLayout.closeDrawer(mDrawerPane);
                break;
            case "Snacks":
                createRecipeList(Recipe.CATEGORY_SNACKS);
                mDrawerLayout.closeDrawer(mDrawerPane);
                break;
            case "Salad":
                createRecipeList(Recipe.CATEGORY_SALAD);
                mDrawerLayout.closeDrawer(mDrawerPane);
                break;
            case "Soup":
                createRecipeList(Recipe.CATEGORY_SOUP);
                mDrawerLayout.closeDrawer(mDrawerPane);
                break;
            default:
                createRecipeList(6);
                mDrawerLayout.closeDrawer(mDrawerPane);
                break;
        }
    }

    /**Opens the AddRecipeActivity*/
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


    /**Loads the recipes out of the XML-file into an ArrayList*/
    public void loadRecipesIntoArrayList() {

        rArrayList.clear();
        File file = getFileStreamPath("recipes.xml");

        if (file.exists()) {
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
                    nListItem.setCategory(eElement.getAttribute("category"));
                    rArrayList.add(nListItem);
                }
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
            Toast.makeText(this, "File doesn't exist", Toast.LENGTH_LONG).show();
    }


    /**Population of the MainActivity with the saved recipes*/
    public void createRecipeList(int category){
        rLayoutRecList.removeAllViews();
        rDisplayArrayList.clear();

        LinearLayout.LayoutParams paramsMain = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams paramsBar = new LinearLayout.LayoutParams(60, LinearLayout.LayoutParams.MATCH_PARENT);

        for(int i=0; i<rArrayList.size(); i++){
            if(category==rArrayList.get(i).getCategory()||category==6) {
                LinearLayout rLinearLayoutMain = new LinearLayout(this);
                LinearLayout rLinearLayoutCatBar = new LinearLayout(this);
                TextView rTextView = new TextView(this);

                rLinearLayoutMain.setOrientation(LinearLayout.HORIZONTAL);
                rLinearLayoutMain.setLayoutParams(paramsMain);

                switch (rArrayList.get(i).getCategory()) {
                    case Recipe.CATEGORY_APPETIZER:
                        rLinearLayoutCatBar.setBackgroundColor(Color.parseColor("#E91E63"));
                        break;
                    case Recipe.CATEGORY_DESSERT:
                        rLinearLayoutCatBar.setBackgroundColor(Color.parseColor("#9C27B0"));
                        break;
                    case Recipe.CATEGORY_SNACKS:
                        rLinearLayoutCatBar.setBackgroundColor(Color.parseColor("#2196F3"));
                        break;
                    case Recipe.CATEGORY_SALAD:
                        rLinearLayoutCatBar.setBackgroundColor(Color.parseColor("#4CAF50"));
                        break;
                    case Recipe.CATEGORY_SOUP:
                        rLinearLayoutCatBar.setBackgroundColor(Color.parseColor("#009688"));
                        break;
                    default:
                        rLinearLayoutCatBar.setBackgroundColor(Color.parseColor("#607D8B"));
                        break;
                }
                rLinearLayoutCatBar.setLayoutParams(paramsBar);

                rTextView.setText(rArrayList.get(i).getName());
                rTextView.setPadding(50, 50, 50, 50);
                rTextView.setTextSize(20f);
                rTextView.setClickable(true);
                rTextView.setOnClickListener(this);

                rLinearLayoutMain.addView(rLinearLayoutCatBar);
                rLinearLayoutMain.addView(rTextView);
                rLayoutRecList.addView(rLinearLayoutMain);

                rDisplayArrayList.add(new DisplayItem(i, rTextView));
            }
        }
    }

    public void onClick(View view){
        openShowroom();
    }

    public void openShowroom(){

        Intent intent = new Intent(this, Showroom.class);
        //intent.putExtra(MainActivity.EXTRA_MESSAGE, position);
        startActivity(intent);
    }


    class DisplayItem{
        int positionInMainArray;
        TextView assignedTextView;

        public DisplayItem(int position, TextView view){
            positionInMainArray = position;
            assignedTextView = view;
        }
    }

    /**Inner class, providing items for the DrawerListAdapter*/
    class NavItem {
        String mTitle;
        int mIcon;

        public NavItem(String title, int icon) {
            mTitle = title;
            mIcon = icon;
        }
    }


    /**Inner class, needed by the Drawer's ListView and populated by NavItems*/
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
                if(position==0||position==1)
                    view = inflater.inflate(R.layout.drawer_item_org, null);
                else
                    view = inflater.inflate(R.layout.drawer_item, null);
            }
            else
                view = convertView;

            TextView titleView = (TextView) view.findViewById(R.id.nav_List_item_title);
            ImageView iconView = (ImageView) view.findViewById(R.id.nav_List_item_icon);

            titleView.setText(mNavItems.get(position).mTitle);
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }
}