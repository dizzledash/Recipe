package com.example.zwiesel.recipe_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        LinearLayout.LayoutParams rParamsIngItem = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Intent intent = getIntent();
        intentValue = intent.getIntExtra(MainActivity.EXTRA_MESSAGE, 0);
        rName = (TextView) findViewById(R.id.textView_showroom_name);
        rDescription = (TextView) findViewById(R.id.textView_showroom_description);
        rIngContent = (LinearLayout) findViewById(R.id.showroom_ing_content);
        usedRecipe = MainActivity.rArrayList.get(intentValue);

        rName.setText(usedRecipe.getName());
        rDescription.setText(usedRecipe.getDescription());

        for(int j = 0; j<usedRecipe.getIngredientCount(); j++){

            String[] ingredientStringArray = usedRecipe.getIngredient(j);
            LinearLayout ingItem = new LinearLayout(this);
            ingItem.setLayoutParams(rParamsIngItem);
            ingItem.setOrientation(LinearLayout.HORIZONTAL);
            ingItem.setPadding(10, 10, 10, 10);



            TextView ingTitle = new TextView(this);
            ingTitle.setWidth((int) (150 * (this.getResources().getDisplayMetrics().density) + 0.5f));
            ingTitle.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_START);
            ingTitle.setText(ingredientStringArray[0]);
            ingTitle.setPadding(10, 10, 10, 10);
            //ingItem.addView(ingTitle);

            EditText ingTitleEditText = new EditText(this);
            ingTitleEditText.setWidth(ingTitle.getWidth());
            ingTitleEditText.setTextAlignment(ingTitle.getTextAlignment());
            ingTitleEditText.setText(ingTitle.getText());
            ingTitleEditText.setPadding(10, 10, 10, 10);

            final ViewSwitcher switchTitle = new ViewSwitcher(this);
            switchTitle.addView(ingTitle);
            switchTitle.addView(ingTitleEditText);
            switchTitle.setClickable(true);
            switchTitle.showNext();
            switchTitle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    switchTitle.showNext();
                    return false;
                }
            });

            ingItem.addView(switchTitle);



            TextView ingAmount = new TextView(this);
            ingAmount.setWidth((int) (50 * (this.getResources().getDisplayMetrics().density) + 0.5f));
            ingAmount.setTextAlignment(TextView.TEXT_ALIGNMENT_VIEW_END);
            ingAmount.setText(ingredientStringArray[1]);
            ingAmount.setPadding(10, 10, 10, 10);
            ingItem.addView(ingAmount);



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
        if (id==R.id.action_edit) {
            //openEdit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
