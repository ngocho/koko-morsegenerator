package at.fhj.swd;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
    }

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
				}
				if (cb_ligth_dis.isChecked()) { // Light-Display-Output
					//TODO
				}
				if (cb_sms.isChecked()) { // SMS-Output
					//TODO
				}
				if (cb_sound.isChecked()) { // Sound-Output
					//TODO
					Intent i = new Intent(this, SoundOutputAct.class);
					i.putExtra("inputtext", et_input_text.getText().toString());
					i.putExtra("morsecode", morsecode);
					startActivity(i);
				}
				if (cb_vibration.isChecked()) { // Vibration-Output
					//TODO
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
		}
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// When checkbox "Text (display)" is enabled, disable all other checkboxes  
		if (buttonView.equals(cb_text_dis) && cb_text_dis.isChecked())		{
			disableOtherCheckboxes(cb_text_dis);
		} // When checkbox "Text (display)" is disabled, enable all other checkboxes
		if (buttonView.equals(cb_text_dis) && !cb_text_dis.isChecked()) 	{
			enableAllCheckboxes();
		}
		
		//TODO: check for all compatible output-options ???
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
	
	/**
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
	 * Disables all checkboxes, but not the one passed as a parameter
	 * @param clickedCheckbox = Checkbox that should not be disabled 
	 */
	
	private void disableOtherCheckboxes(CheckBox clickedCheckbox)
	{
		Iterator <CheckBox> i = allCheckBoxes.iterator();
		CheckBox current; 
		while (i.hasNext())
		{
			current = i.next();
			if (!current.equals(clickedCheckbox))
			{
				current.setEnabled(false);
				current.setChecked(false);
			}
		}
	}
}
