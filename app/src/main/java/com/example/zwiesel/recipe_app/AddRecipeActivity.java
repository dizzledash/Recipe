package com.example.zwiesel.recipe_app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class AddRecipeActivity extends ActionBarActivity {

    //private ImageButton buttonAddIngr;
    private EditText recTitle, ingAmount, ingTitle, descTxt;
    private LinearLayout ingLayout;
    private Spinner unitSpinner, catSpinner;
    private ArrayList<EditText> saveIngNameArrayList = new ArrayList<>();
    private ArrayList<EditText> saveIngAmountArrayList = new ArrayList<>();
    private ArrayList<Spinner> saveIngUnitArrayList = new ArrayList<>();
    private boolean newLine;
    private final boolean REC_ADDED = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        recTitle = (EditText) findViewById(R.id.editText_rec_title);
        ingAmount = (EditText) findViewById(R.id.editText_ing_amount_shown);
        ingLayout = (LinearLayout) findViewById(R.id.linearLayout_dynamic_ing);
        ingTitle = (EditText) findViewById(R.id.editText_ing_name_shown);
        descTxt = (EditText) findViewById(R.id.editText_description);
        unitSpinner = (Spinner) findViewById(R.id.spinner_unit);
        catSpinner = (Spinner) findViewById(R.id.spinner_category);
        newLine = true;

        // Creation of dynamically added spinner
        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.unit_array,
                android.R.layout.simple_spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(spinAdapter);

        //Creation of the static category spinner
        ArrayAdapter<CharSequence> spinAdapterCat = ArrayAdapter.createFromResource(this,
                R.array.category,
                android.R.layout.simple_spinner_item);
        spinAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(spinAdapterCat);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_recipe, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.saveRecipeButton){
            saveRec();
            return true;
        }

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }



    /** After click on the plus-button, dynamically a new line of ingredient-input is created*/
    public void clickAddIngr(View v){

        if(newLine){
            saveIngNameArrayList.add(ingTitle);
            saveIngAmountArrayList.add(ingAmount);
            saveIngUnitArrayList.add(unitSpinner);
            newLine = false;
        }

        // Predefined parameters needed for the initialization
        LinearLayout.LayoutParams paramsName = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        LinearLayout.LayoutParams paramsAmount = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
        LinearLayout.LayoutParams paramsSpinner = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
        LinearLayout.LayoutParams paramsLinLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // LinearLayout as container for the views
        LinearLayout newLayout = new LinearLayout(this);
        newLayout.setLayoutParams(paramsLinLayout);
        newLayout.setOrientation(LinearLayout.HORIZONTAL);

        // EditText for name
        EditText edTxtName = new EditText(this);
        edTxtName.setHint("Ingredient name");
        edTxtName.setLayoutParams(paramsName);
        edTxtName.setTextSize(15);
        edTxtName.setMaxLines(1);
        newLayout.addView(edTxtName);
        saveIngNameArrayList.add(edTxtName);

        // EditText for amount
        EditText edTxtAmount = new EditText(this);
        edTxtAmount.setHint("Amount");
        edTxtAmount.setLayoutParams(paramsAmount);
        edTxtAmount.setMaxLines(1);
        edTxtAmount.setTextSize(15);
        edTxtAmount.setInputType(2);
        newLayout.addView(edTxtAmount);
        saveIngAmountArrayList.add(edTxtAmount);

        // Unit spinner
        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.unit_array,
                android.R.layout.simple_spinner_item);
        Spinner spinUnit = new Spinner(this);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinUnit.setAdapter(spinAdapter);
        spinUnit.setLayoutParams(paramsSpinner);
        newLayout.addView(spinUnit);
        saveIngUnitArrayList.add(spinUnit);

        ingLayout.addView(newLayout);
    }


    /**Saves the typed in recipe into an xml file*/
    public void saveRec() {

        //TODO-soe Check whether views - at least title-view - are empty; if true -> message and don't save


        File saveFile = getFileStreamPath("recipes.xml");

        if(saveFile.exists()) {

            try {

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(openFileInput("recipes.xml"));

                doc.getDocumentElement().normalize();

                //Getting the root-element to add a new recipe
                Element rootElement = doc.getDocumentElement();
                Element recipe = doc.createElement("recipe");
                Attr recAttr = doc.createAttribute("name");
                Attr recAttrCat = doc.createAttribute("category");
                recAttr.setValue(recTitle.getText().toString());
                recAttrCat.setValue(catSpinner.getSelectedItem().toString());
                recipe.setAttributeNode(recAttr);
                recipe.setAttributeNode(recAttrCat);
                rootElement.appendChild(recipe);


                //Adding the ingredients as child elements to the recipe element
                for(int j = 0; j<saveIngNameArrayList.size(); j++) {

                    Element ing = doc.createElement("ingredient");
                    Attr ingAttrAmount = doc.createAttribute("amount");
                    Attr ingAttrUnit = doc.createAttribute("unit");
                    ingAttrAmount.setValue(saveIngAmountArrayList.get(j).getText().toString());
                    ingAttrUnit.setValue(saveIngUnitArrayList.get(j).getSelectedItem().toString());
                    ing.setAttributeNode(ingAttrAmount);
                    ing.setAttributeNode(ingAttrUnit);
                    ing.appendChild(doc.createTextNode(saveIngNameArrayList.get(j).getText().toString()));
                    recipe.appendChild(ing);
                }

                //Adding the description text as child to the recipe element
                Element infoTxt = doc.createElement("description");
                infoTxt.appendChild(doc.createTextNode(descTxt.getText().toString()));
                recipe.appendChild(infoTxt);

                TransformerFactory transFact = TransformerFactory.newInstance();
                Transformer transf = transFact.newTransformer();
                DOMSource src = new DOMSource(doc);
                StreamResult res = new StreamResult(openFileOutput("recipes.xml", Context.MODE_PRIVATE));

                transf.transform(src, res);
            }
            catch (ParserConfigurationException pcEx) {
                Toast.makeText(this, "ParserConfigurationException", Toast.LENGTH_LONG).show();
            }
            catch (IOException ioEx){
                Toast.makeText(this, "IOException", Toast.LENGTH_LONG).show();
            }
            catch (SAXException SAXEx){
                Toast.makeText(this, "SAXException", Toast.LENGTH_LONG).show();
            }
            catch (TransformerException tEx){
                Toast.makeText(this, "TransformerException", Toast.LENGTH_LONG).show();
            }
                Toast.makeText(this, "Recipe saved", Toast.LENGTH_SHORT).show();
        }
        else
            createSaveFile();

        //Let the MainActivity know, that we added a new recipe
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, REC_ADDED);
        startActivity(intent);
    }

    /**If the XML-File for saving the recipes doesn't exists, it will be created here*/
    private void createSaveFile(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            //Only creating the root-element in the XML-file
            Element rootElement = doc.createElement("recipeList");
            doc.appendChild(rootElement);

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
        Toast.makeText(this, "New XML document created", Toast.LENGTH_LONG).show();
        saveRec();
    }
}
