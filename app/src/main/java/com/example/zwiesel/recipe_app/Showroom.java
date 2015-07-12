package com.example.zwiesel.recipe_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;


public class Showroom extends AppCompatActivity {

    private TextView rName, rDescription;
    private LinearLayout rIngContent;
    private Recipe usedRecipe;
    private int intentValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showroom);

        final MenuItem saveItem = (MenuItem) findViewById(R.id.action_save_edited);
        //saveItem.setVisible(false);

        LinearLayout.LayoutParams rParamsIngItem = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 120);
        Intent intent = getIntent();
        intentValue = intent.getIntExtra(MainActivity.EXTRA_MESSAGE, 0);
        final ViewSwitcher switchRName = (ViewSwitcher) findViewById(R.id.viewSwitcher_showroom_name);
        rName = (TextView) findViewById(R.id.textView_showroom_name);
        EditText rNameEditText = (EditText) findViewById(R.id.editText_showroom_name);
        rDescription = (TextView) findViewById(R.id.textView_showroom_description);
        rIngContent = (LinearLayout) findViewById(R.id.showroom_ing_content);
        usedRecipe = MainActivity.rArrayList.get(intentValue);


        switchRName.setLongClickable(true);
        rName.setText(usedRecipe.getName());
        rNameEditText.setText(rName.getText());
        switchRName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //saveItem.setVisible(true);
                switchRName.showNext();
                return false;
            }
        });
        rDescription.setText(usedRecipe.getDescription());

        for(int j = 0; j<usedRecipe.getIngredientCount(); j++){

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
            //ingItem.addView(ingTitle);

            EditText ingTitleEditText = new EditText(this);
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
                    //saveItem.setVisible(true);
                    switchTitle.showNext();
                    return false;
                }
            });

            ingItem.addView(switchTitle);



            //--------------Ingredient amount viewSwitcher-------------
            TextView ingAmount = new TextView(this);
            ingAmount.setWidth((int) (50 * (this.getResources().getDisplayMetrics().density) + 0.5f));
            ingAmount.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_END);
            ingAmount.setText(ingredientStringArray[1]);
            ingAmount.setPadding(10, 10, 10, 10);
            //ingItem.addView(ingAmount);

            EditText ingAmountEditText = new EditText(this);
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
                    //saveItem.setVisible(true);
                    switchAmount.showNext();
                    return false;
                }
            });

            ingItem.addView(switchAmount);



            //-------------Ingredient unit viewSwitcher--------------
            TextView ingUnit = new TextView(this);
            ingUnit.setWidth((int) (50 * (this.getResources().getDisplayMetrics().density) + 0.5f));
            ingUnit.setText(ingredientStringArray[2]);
            ingUnit.setPadding(10,10,10,10);
            ingItem.addView(ingUnit);

            rIngContent.addView(ingItem);
        }


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
            //openEdit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
