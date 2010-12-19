package at.fhj.swd;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;
import at.fhj.swd.R;

/**
 * Main-Acitivity 
 * @author Matthias Koinegg
 *
 */
public class MainAct extends Activity implements OnClickListener, OnCheckedChangeListener{
	// Global variables
	Button bt_start, bt_settings;
	EditText et_input_text;
	CheckBox cb_text_dis, cb_ligth_dis, cb_light_flash, cb_sound, cb_vibration, cb_sms;
	List <CheckBox> allCheckBoxes;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final String app_name = getResources().getString(R.string.app_name);
        Log.i("MAIN startet!",app_name);
             
        setContentView(R.layout.main);
        
        // Diverse Initialisierungen
        this.init();
    }

	public void onClick(View view) {
		// Button "Start output!"
		if (view == bt_start) {
			Log.i("ClickEvent", "Button Start output clicked");

			Log.i("path",this.getFilesDir().toString());
			
			// Translate Input-String to Morse-Text
			String morse = MorseGenerator.getInstance(this).translate(et_input_text.getText().toString());			
			Log.i("Got following Text from Morsegenerator", morse);
			//TODO
		}
		if (view == bt_settings) {
			Log.i("ClickEvent", "Button Settings clicked");
			//TODO
		}
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Log.i("Checkboxes, status changed", buttonView.getText().toString());
		//TODO
	}
	
	private void init()
	{
        // Find all Screen-Components
		et_input_text = (EditText) findViewById(R.id.et_input_text);
        bt_start = (Button) findViewById(R.id.bt_start);
        bt_settings = (Button) findViewById(R.id.bt_settings);
        cb_text_dis = (CheckBox) findViewById(R.id.cb_text); 
        cb_ligth_dis = (CheckBox) findViewById(R.id.cb_ligth); 
        cb_light_flash = (CheckBox) findViewById(R.id.cb_flash); 
        cb_sound = (CheckBox) findViewById(R.id.cb_sound); 
        cb_vibration = (CheckBox) findViewById(R.id.cb_vibration); 
        cb_sms = (CheckBox) findViewById(R.id.cb_sms);         

        // Stuff all the checkboxes in one list
        allCheckBoxes = Arrays.asList(cb_text_dis, cb_ligth_dis, cb_light_flash, cb_sound, cb_vibration, cb_sms);
        
        // Set all the Listeners 
        	// Checkboxes
        Iterator <CheckBox> i = allCheckBoxes.iterator();
        while (i.hasNext())
        {
            i.next().setOnCheckedChangeListener(this);
        }
        	// Buttons
        bt_start.setOnClickListener(this);
        bt_settings.setOnClickListener(this);
	}
}