package at.fhj.swd;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


/**
 * Activity for sending the morsecode as SMS
 * @author Matthias Koinegg
 *
 */
public class SmsOutputAct extends Activity implements OnClickListener{

    @Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_sms);
        
        // Provide a button to get back to the main activity
        Button bt_back = (Button) findViewById(R.id.bt_sms_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
           setResult(RESULT_OK);
           finish();
           }
        });
        // Set listenter for send-button
        ((Button) findViewById(R.id.bt_sms_send)).setOnClickListener(this);
        
        String morsecode = getIntent().getExtras().getString("morsecode");
        String inputtext = getIntent().getExtras().getString("inputtext");        
        ((TextView) findViewById(R.id.tv_sms_morsecode)).setText(morsecode);
        ((TextView) findViewById(R.id.tv_sms_inputtext)).setText(inputtext);
        
        
    }

	public void onClick(View v) {
		if (v.equals((Button) findViewById(R.id.bt_sms_send)))
		{
			new AlertDialog.Builder(this).setMessage(
			"Not implemented yet!")
			.setNeutralButton("Schade...", null).show(); 
			
			/* 
			 * SmsManager sms = SmsManager.getDefault();
			 * sms.sendTextMessage(destination,null, "This is test message",null, null);
			 * 
			 */
		}
		
	}
}

