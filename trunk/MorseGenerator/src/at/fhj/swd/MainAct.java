package at.fhj.swd;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
        
        // SetUp global preferences for speed of morsecode-output
        SharedPreferences settings = getSharedPreferences("MORSE", MODE_WORLD_READABLE );
        SharedPreferences.Editor editor = settings.edit();
        // dit = "."    dah = "-"    see http://www.teachersparadise.com/ency/de/wikipedia/m/mo/morsecode.html
        editor.putInt("dit", 200);
        editor.putInt("dah", 600);
        editor.commit();

    }

    /**
     * Handles all click-events (button start, settings, ...)
     */
	public void onClick(View view) {
		Log.i("ClickEvent", "Button " + view.toString() + " clicked");

		
		// Button "Start output!"
		if (view == bt_start) {			
			// Check if at least one checkbox is selected
			if (!isCheckboxSelected(allCheckBoxes))
			{
				new AlertDialog.Builder(this).setMessage(
						"Select at least one output-option!")
						.setNeutralButton("OK", null).show();
			}
			else {
				// Translate Input-String to Morse-Text
				String morsecode = MorseGenerator.getInstance(this).translate(et_input_text.getText().toString());
				Log.i("Got following Text from Morsegenerator", morsecode);
				
				if (cb_light_flash.isChecked()) { // Light-Flash-Output
					//TODO
					// this might help:
					// http://stackoverflow.com/questions/3878294/camera-parameters-flash-mode-torch-replacement-for-android-2-1
					// http://mobilecoder.wordpress.com/2010/12/06/android-torch-led/
					new AlertDialog.Builder(this).setMessage(
					"Not implemented yet!")
					.setNeutralButton("Schade...", null).show();
				}
				if (cb_ligth_dis.isChecked()) { // Light-Display-Output
					Intent i = new Intent(this, ScreenLightOutputAct.class);
					i.putExtra("inputtext", et_input_text.getText().toString());
					i.putExtra("morsecode", morsecode);
					startActivity(i);
				}
				if (cb_sms.isChecked()) { // SMS-Output
					//TODO
					new AlertDialog.Builder(this).setMessage(
					"Not implemented yet!")
					.setNeutralButton("Schade...", null).show();
				}
				if (cb_sound.isChecked() || cb_vibration.isChecked()) { // Sound-Output or Vibration-Output
					Intent i = new Intent(this, OutputAct.class);
					i.putExtra("inputtext", et_input_text.getText().toString());
					i.putExtra("morsecode", morsecode);
					if (cb_sound.isChecked()) i.putExtra("soundoutput", true); else i.putExtra("soundoutput", false);
					if (cb_vibration.isChecked()) i.putExtra("vibrationoutput", true); else i.putExtra("vibrationoutput", false);
					startActivity(i);
				}

				if (cb_text_dis.isChecked()) { // Text-Output
					Intent i = new Intent(this, TextOutputAct.class);
					i.putExtra("inputtext", et_input_text.getText().toString());
					i.putExtra("morsecode", morsecode);
					startActivity(i);
				}	
			}
		}
		// Button "Settings" clicked
		if (view == bt_settings) {
			//TODO
			new AlertDialog.Builder(this).setMessage(
			"Not implemented yet!")
			.setNeutralButton("Schade...", null).show();
		}
	}

	/**
	 * Holds the logic of valid checkbox-combinations
	 * Enables/Disables checkboxes accordingly
	 */
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// ################## Text (display) ##################
		// When checkbox "Text (display)" is enabled, disable all other checkboxes  
		if (buttonView.equals(cb_text_dis) && cb_text_dis.isChecked())		{
			disableOtherCheckboxes(cb_text_dis);
		} // When checkbox "Text (display)" is disabled, enable all other checkboxes
		if (buttonView.equals(cb_text_dis) && !cb_text_dis.isChecked()) 	{
			enableAllCheckboxes();
		}
		
		// ################## Sound && Vibration ##################
		// When checkbox "Sound" or "Vibration" is enabled, leave only Sound and Vibration enabled 
		if ((buttonView.equals(cb_sound) && cb_sound.isChecked()) ||
				(buttonView.equals(cb_vibration) && cb_vibration.isChecked()))		{
			disableOtherCheckboxes(cb_sound, cb_vibration);
		}		
		// When checkbox "Sound" and "Vibration" are disabled, enable all other checkboxes
		if ((buttonView.equals(cb_sound) && !cb_sound.isChecked()) && (!cb_vibration.isChecked()))		{
			enableAllCheckboxes();
		}
		if ((buttonView.equals(cb_vibration) && !cb_vibration.isChecked()) && (!cb_sound.isChecked()))		{
			enableAllCheckboxes();
		}
		
		// ################## Light (display) ##################
		// When checkbox "Light (display)" is enabled, disable all other checkboxes  
		if (buttonView.equals(cb_ligth_dis) && cb_ligth_dis.isChecked())		{
			disableOtherCheckboxes((CheckBox)buttonView); }
		// When checkbox "Light (display)" is disabled, enable all other checkboxes
		if (buttonView.equals(cb_ligth_dis) && !cb_ligth_dis.isChecked()) 	{
			enableAllCheckboxes();
		}
		
		//TODO: check for all compatible output-options ???
	}
	
	/**
	 * Various initialization-work
	 */
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
	
	/**
	 * Helper-Method
	 * @param checkboxes
	 * @return Returns true if one of the checkboxes is checked (selected)
	 */
	private boolean isCheckboxSelected(List <CheckBox> checkboxes)
	{
		boolean isSelected = false;
		Iterator <CheckBox> i = checkboxes.iterator();
		while (i.hasNext())
		{
			if (i.next().isChecked())
			{
				isSelected = true;
			}
		}
		return isSelected;	
	}
	
	/**
	 * Helper-Method
	 * Enables all checkboxes 
	 */
	private void enableAllCheckboxes()
	{
		Iterator <CheckBox> i = allCheckBoxes.iterator();
		while (i.hasNext())
		{
			i.next().setEnabled(true);
		}
	}
	
	/**
	 * Helper-Method
	 * Disables all checkboxes, but not the one passed as a parameter
	 * @param clickedCheckbox = Checkbox that should not be disabled 
	 */
	private void disableOtherCheckboxes(CheckBox... clickedCheckbox)
	{
		boolean match=false;
		Iterator <CheckBox> i = allCheckBoxes.iterator();
		CheckBox current; 
		while (i.hasNext())
		{
			current = i.next();
			Log.i("cbx - current",current.getText().toString());
			
			for (int j=0; j<clickedCheckbox.length; j++) {
				if (current.equals(clickedCheckbox[j]))
				{
					match = true;
				}
			}
			if (!match) {
				current.setEnabled(false);
				current.setChecked(false);
			}
			match = false;
		}
	}
}
