package ie.gmit.glen.placed;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class About extends Activity {

    TextView myTextView;
    String res = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        myTextView = (TextView) findViewById(R.id.about);

        try {

            // open text file to buffer reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    getAssets().open("about.txt")));

            String myLine = reader.readLine();

            // reading line by line
            while (myLine != null) {
                res += myLine + "\n";
                myLine = reader.readLine();
            }

            // close file
            reader.close();
        } catch (IOException e) {

            // show error
            Toast.makeText(getApplicationContext(),
                    "Error Opening the File !!!", Toast.LENGTH_LONG).show();
        }

        // display contents in myTextview
        myTextView.setText(res);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
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
}
