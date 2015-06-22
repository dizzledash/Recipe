package com.example.zwiesel.recipe_app;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
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

    private ImageButton buttonAddIngr;
    private EditText recTitle, ingAmount, ingTitle, descTxt;
    private LinearLayout ingLayout;
    private Spinner unitSpinner, catSpinner;
    private ActionBar actBar;
    private int countIngAdd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        buttonAddIngr = (ImageButton) findViewById(R.id.button_add_ingr);
        recTitle = (EditText) findViewById(R.id.editText_rec_title);
        ingAmount = (EditText) findViewById(R.id.editText_ing_amount_shown);
        ingLayout = (LinearLayout) findViewById(R.id.linearLayout_dynamic_ing);
        ingTitle = (EditText) findViewById(R.id.editText_ing_name_shown);
        descTxt = (EditText) findViewById(R.id.editText_description);
        unitSpinner = (Spinner) findViewById(R.id.spinner_unit);
        catSpinner = (Spinner) findViewById(R.id.spinner_category);

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

    // After click on the plus-button, dynamically a new line of ingredient-input is created
    public void clickAddIngr(View v){

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

        // EditText for amount
        EditText edTxtAmount = new EditText(this);
        edTxtAmount.setHint("Amount");
        edTxtAmount.setLayoutParams(paramsAmount);
        edTxtAmount.setMaxLines(1);
        edTxtAmount.setTextSize(15);
        edTxtAmount.setInputType(2);
        newLayout.addView(edTxtAmount);

        // Unit spinner
        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.unit_array,
                android.R.layout.simple_spinner_item);
        Spinner spinUnit = new Spinner(this);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinUnit.setAdapter(spinAdapter);
        spinUnit.setLayoutParams(paramsSpinner);
        newLayout.addView(spinUnit);

        ingLayout.addView(newLayout);
        countIngAdd++;
    }

    // Saving isn not working yet
    public void saveRec(View view) {
        Toast.makeText(this, "Got here", Toast.LENGTH_LONG).show();
        File saveFile = new File("/data/recipes.xml");
        if(saveFile.exists()) {
            try {
                //String[][] ingArray = new String[][3]

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(saveFile);

                doc.getDocumentElement().normalize();

                Element rootElement = doc.getDocumentElement();
                Element recipe = doc.createElement("recipe");
                Attr recAttr = doc.createAttribute("name");
                recAttr.setValue(recTitle.getText().toString());
                recipe.setAttributeNode(recAttr);
                rootElement.appendChild(recipe);

                Element ing = doc.createElement("ingredient");
                Attr ingAttrAmount = doc.createAttribute("amount");
                Attr ingAttrUnit = doc.createAttribute("unit");
                ingAttrAmount.setValue(ingAmount.getText().toString());
                ingAttrUnit.setValue(unitSpinner.getSelectedItem().toString());
                ing.setAttributeNode(ingAttrAmount);
                ing.setAttributeNode(ingAttrUnit);
                ing.appendChild(doc.createTextNode(ingTitle.getText().toString()));
                recipe.appendChild(ing);

                Element infoTxt = doc.createElement("description");
                infoTxt.appendChild(doc.createTextNode(descTxt.getText().toString()));
                recipe.appendChild(infoTxt);

                TransformerFactory transFact = TransformerFactory.newInstance();
                Transformer transf = transFact.newTransformer();
                DOMSource src = new DOMSource(doc);
                StreamResult res = new StreamResult(new File("recipes.xml"));

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
            Toast.makeText(this, "Recipe saved", Toast.LENGTH_LONG).show();
        }
        else
            createSaveFile();
    }

    // Saving is not working yet
    private void createSaveFile(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("recipeList");
            doc.appendChild(rootElement);

            TransformerFactory transFact = TransformerFactory.newInstance();
            Transformer transf = transFact.newTransformer();
            DOMSource src = new DOMSource(doc);
            StreamResult res = new StreamResult(new File("/data/recipes.xml"));

            transf.transform(src, res);
        }
        catch (ParserConfigurationException pcEx) {
            Toast.makeText(this, "ParserConfigurationException", Toast.LENGTH_LONG).show();
        }
        catch (TransformerException tEx){
            Toast.makeText(this, "TransformerException", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, "New XML document created", Toast.LENGTH_LONG).show();
        saveRec(buttonAddIngr);
    }
}
