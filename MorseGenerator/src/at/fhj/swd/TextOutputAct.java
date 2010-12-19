package at.fhj.swd;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Activity to show the morsecode as text on the display
 * @author Matthias Koinegg
 */
public class TextOutputAct extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_text);
        
        // Provide a button to get back to the main activity
        Button bt_back = (Button) findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
           setResult(RESULT_OK);
           finish();
           }
        });
        
        Log.i("TextOutputAct","Acitivty created");
        
        String morsecode = getIntent().getExtras().getString("morsecode");
        String inputtext = getIntent().getExtras().getString("inputtext");        
        ((TextView) findViewById(R.id.tv_morsecode)).setText(morsecode);
        ((TextView) findViewById(R.id.tv_inputtext)).setText(inputtext);
    }
	
}
