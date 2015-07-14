package com.example.zwiesel.recipe_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * This class/activity provides the possibility to view your saved recipes.
 * Also you can edit every part by long-clicking on it.
 */
public class Showroom extends AppCompatActivity {

    //TODO-soe Add or delete ingredient in editing mode

    private LinearLayout rIngContent;
    private Recipe usedRecipe;
    private int intentValue;
    private ArrayList<ViewSwitcher[]> ingViewSwitcherAL = new ArrayList<>();
    private EditText newNameEditText;
    private EditText newDescriptionEditText;
    private boolean changedName = false;
    private boolean changedIngredient = false;
    private boolean changedDescription = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showroom);

        LinearLayout.LayoutParams rParamsIngItem = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 120);

        //Getting the message, send by the MainActivity, which contains the recipe to be shown.
        Intent intent = getIntent();
        intentValue = intent.getIntExtra(MainActivity.EXTRA_MESSAGE, 0);

        final EditText rNameEditText = (EditText) findViewById(R.id.editText_showroom_name);
        final EditText rDescriptionEditText = (EditText) findViewById(R.id.editText_showroom_description);
        final ViewSwitcher switchRDescription = (ViewSwitcher) findViewById(R.id.viewSwitcher_showroom_description);
        final ViewSwitcher switchRName = (ViewSwitcher) findViewById(R.id.viewSwitcher_showroom_name);
        TextView rName = (TextView) findViewById(R.id.textView_showroom_name);
        TextView rDescription = (TextView) findViewById(R.id.textView_showroom_description);
        rIngContent = (LinearLayout) findViewById(R.id.showroom_ing_content);
        usedRecipe = MainActivity.rArrayList.get(intentValue);


        //Showing the name and switching the view, when long-clicking on the TextView
        switchRName.setLongClickable(true);
        rName.setText(usedRecipe.getName());
        rNameEditText.setText(rName.getText());
        switchRName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switchRName.showNext();
                rNameEditText.requestFocus();
                rNameEditText.setSelection(rNameEditText.getText().length());
                newNameEditText = rNameEditText;
                changedName = true;
                return false;
            }
        });


        //Dynamically creating views for every ingredient the recipe contains.
        //Also handling the view switching, when long-clicking on the TextView.
        for(int j = 0; j<usedRecipe.getIngredientCount(); j++){

            ViewSwitcher[] viewSwitcherArray = new ViewSwitcher[3];

            String[] ingredientStringArray = usedRecipe.getIngredient(j);
            LinearLayout ingItem = new LinearLayout(this);
            ingItem.setLayoutParams(rParamsIngItem);
            ingItem.setOrientation(LinearLayout.HORIZONTAL);
            ingItem.setPadding(10, 10, 10, 10);


            //-------------Ingredient title viewSwitcher-------------
            TextView ingTitle = new TextView(this);
            ingTitle.setWidth((int) (150 * (this.getResources().getDisplayMetrics().density) + 0.5f));
            ingTitle.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
            ingTitle.setText(ingredientStringArray[0]);
            ingTitle.setPadding(10, 10, 10, 10);

            final EditText ingTitleEditText = new EditText(this);
            ingTitleEditText.setWidth(ingTitle.getWidth());
            ingTitleEditText.setTextAlignment(EditText.TEXT_ALIGNMENT_VIEW_START);
            ingTitleEditText.setText(ingTitle.getText());
            ingTitleEditText.setPadding(10, 10, 10, 10);
            ingTitleEditText.setTextSize(15);
            ingTitleEditText.setMaxHeight(ingTitle.getHeight());
            ingTitleEditText.setInputType(android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS);

            final ViewSwitcher switchTitle = new ViewSwitcher(this);
            switchTitle.addView(ingTitle);
            switchTitle.addView(ingTitleEditText);
            switchTitle.setLongClickable(true);
            switchTitle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    switchTitle.showNext();
                    ingTitleEditText.requestFocus();
                    ingTitleEditText.setSelection(ingTitleEditText.getText().length());
                    changedIngredient = true;
                    return false;
                }
            });

            viewSwitcherArray[0] = switchTitle;
            ingItem.addView(switchTitle);



            //--------------Ingredient amount viewSwitcher-------------
            TextView ingAmount = new TextView(this);
            ingAmount.setWidth((int) (50 * (this.getResources().getDisplayMetrics().density) + 0.5f));
            ingAmount.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_END);
            ingAmount.setText(ingredientStringArray[1]);
            ingAmount.setPadding(10, 10, 10, 10);

            final EditText ingAmountEditText = new EditText(this);
            ingAmountEditText.setWidth(ingAmount.getWidth());
            ingAmountEditText.setTextAlignment(EditText.TEXT_ALIGNMENT_VIEW_START);
            ingAmountEditText.setText(ingAmount.getText());
            ingAmountEditText.setPadding(10, 10, 10, 10);
            ingAmountEditText.setTextSize(15);
            ingAmountEditText.setMaxHeight(ingAmount.getHeight());
            ingAmountEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

            final ViewSwitcher switchAmount = new ViewSwitcher(this);
            switchAmount.addView(ingAmount);
            switchAmount.addView(ingAmountEditText);
            switchAmount.setLongClickable(true);
            switchAmount.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    switchAmount.showNext();
                    ingAmountEditText.requestFocus();
                    ingAmountEditText.setSelection(ingAmountEditText.getText().length());
                    changedIngredient = true;
                    return false;
                }
            });

            viewSwitcherArray[1] = switchAmount;
            ingItem.addView(switchAmount);



            //-------------Ingredient unit viewSwitcher--------------
            TextView ingUnit = new TextView(this);
            ingUnit.setWidth((int) (50 * (this.getResources().getDisplayMetrics().density) + 0.5f));
            ingUnit.setText(ingredientStringArray[2]);
            ingUnit.setPadding(10, 10, 10, 10);

            ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                    R.array.unit_array,
                    android.R.layout.simple_spinner_item);
            final Spinner spinUnit = new Spinner(this);
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinUnit.setAdapter(spinAdapter);

            final ViewSwitcher switchUnit = new ViewSwitcher(this);
            switchUnit.addView(ingUnit);
            switchUnit.addView(spinUnit);
            switchUnit.setLongClickable(true);
            switchUnit.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    switchUnit.showNext();
                    spinUnit.requestFocus();
                    changedIngredient = true;
                    return false;
                }
            });

            viewSwitcherArray[2] = switchUnit;
            ingItem.addView(switchUnit);
            rIngContent.addView(ingItem);
            ingViewSwitcherAL.add(viewSwitcherArray);
        }


        //Showing and switching views for the description part of the recipe
        rDescription.setText(usedRecipe.getDescription());
        rDescriptionEditText.setText(rDescription.getText());
        switchRDescription.setLongClickable(true);
        switchRDescription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                switchRDescription.showNext();
                rDescriptionEditText.requestFocus();
                rDescriptionEditText.setSelection(rDescriptionEditText.getText().length());
                newDescriptionEditText = rDescriptionEditText;
                changedDescription = true;
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_showroom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id==R.id.action_save_edited) {
            saveEditedRecipe();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Saving the edited recipe. First refreshing the static recipe-arrayList and then
     * creating a new XML-file, thus overwriting the old file, for permanent saving.
     */
    public void saveEditedRecipe(){

        if(changedName)
            usedRecipe.setName(newNameEditText.getText().toString());

        if(changedIngredient){
            for(int c = 0; c<ingViewSwitcherAL.size(); c++){
                String unit;
                TextView titleTV = (TextView) ingViewSwitcherAL.get(c)[0].getCurrentView();
                TextView amountTV = (TextView) ingViewSwitcherAL.get(c)[1].getCurrentView();

                if(ingViewSwitcherAL.get(c)[2].getDisplayedChild()==0){
                    TextView unitTV = (TextView) ingViewSwitcherAL.get(c)[2].getCurrentView();
                    unit = unitTV.getText().toString();
                }
                else{
                    Spinner unitS = (Spinner) ingViewSwitcherAL.get(c)[2].getCurrentView();
                    unit = unitS.getSelectedItem().toString();
                }

                usedRecipe.setIngredient(c,
                        titleTV.getText().toString(),
                        amountTV.getText().toString(), unit);
            }
        }

        if(changedDescription){
            usedRecipe.setDescription(newDescriptionEditText.getText().toString());
        }


        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("recipeList");
            doc.appendChild(rootElement);

            for(int i = 0; i<MainActivity.rArrayList.size(); i++) {
                Element recipe = doc.createElement("recipe");
                Attr recAttr = doc.createAttribute("name");
                Attr recAttrCat = doc.createAttribute("category");
                recAttr.setValue(MainActivity.rArrayList.get(i).getName());
                recAttrCat.setValue(MainActivity.rArrayList.get(i).getCategoryAsString());
                recipe.setAttributeNode(recAttr);
                recipe.setAttributeNode(recAttrCat);
                rootElement.appendChild(recipe);

                for (int j = 0; j < MainActivity.rArrayList.get(i).getIngredientCount(); j++) {

                    Element ing = doc.createElement("ingredient");
                    Attr ingAttrAmount = doc.createAttribute("amount");
                    Attr ingAttrUnit = doc.createAttribute("unit");
                    ingAttrAmount.setValue(MainActivity.rArrayList.get(i).getIngredient(j)[1]);
                    ingAttrUnit.setValue(MainActivity.rArrayList.get(i).getIngredient(j)[2]);
                    ing.setAttributeNode(ingAttrAmount);
                    ing.setAttributeNode(ingAttrUnit);
                    ing.appendChild(doc.createTextNode(MainActivity.rArrayList.get(i).getIngredient(j)[0]));
                    recipe.appendChild(ing);
                }

                Element infoTxt = doc.createElement("description");
                infoTxt.appendChild(doc.createTextNode(MainActivity.rArrayList.get(i).getDescription()));
                recipe.appendChild(infoTxt);
            }

            TransformerFactory transFact = TransformerFactory.newInstance();
            Transformer transf = transFact.newTransformer();
            DOMSource src = new DOMSource(doc);
            StreamResult res = new StreamResult(openFileOutput("recipes.xml", Context.MODE_PRIVATE));

            transf.transform(src, res);
        }
        catch (ParserConfigurationException pcEx) {
            Toast.makeText(this, "ParserConfigurationException", Toast.LENGTH_LONG).show();
        }
        catch (TransformerException tEx){
            Toast.makeText(this, "TransformerException", Toast.LENGTH_LONG).show();
        }
        catch (FileNotFoundException fnfEx) {
            Toast.makeText(this, "File Not Found", Toast.LENGTH_LONG).show();
        }


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, 0);
        startActivity(intent);
    }
}
