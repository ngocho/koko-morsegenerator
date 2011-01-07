package at.fhj.swd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Activity for sending the morsecode as SMS
 * @author Matthias Koinegg
 *
 */
public class SmsOutputAct extends Activity implements OnClickListener{

	private String sms_number, inputtext;
	
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
        inputtext = getIntent().getExtras().getString("inputtext"); 
        ((TextView) findViewById(R.id.tv_sms_morsecode)).setText(morsecode);
        ((TextView) findViewById(R.id.tv_sms_inputtext)).setText(inputtext);
        
        SharedPreferences settings =this.getSharedPreferences("MORSE", 0);
        sms_number = settings.getString("sms", "112");
;        
    }

	public void onClick(View v) {
		if (v.equals((Button) findViewById(R.id.bt_sms_send)))
		{
//			new AlertDialog.Builder(this).setMessage(
//			"Not implemented yet!")
//			.setNeutralButton("Schade...", null).show(); 
			
			/* 
			 * SmsManager sms = SmsManager.getDefault();
			 * sms.sendTextMessage(destination,null, "This is test message",null, null);
			 * 
			 */
			
			 String phoneNo = sms_number;
             String message = inputtext;
             
             if (phoneNo.length()>0 && message.length()>0)                
                 sendSMS(phoneNo, message);                
             else
            	 Log.i("SMS-Section","Number or message not valid, phoneNo: " + phoneNo +
            		   " - message: " + message);

		}	
	}
	
	// sends an SMS message
	 private void sendSMS(String phoneNumber, String message)
	    {        
	        String SENT = "SMS_SENT";
	 
	        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
	            new Intent(SENT), 0);
	  
	        //---when the SMS has been sent---
	        registerReceiver(new BroadcastReceiver(){
	            @Override
	            public void onReceive(Context arg0, Intent arg1) {
	                switch (getResultCode())
	                {
	                    case Activity.RESULT_OK:
	                        Toast.makeText(getBaseContext(), "SMS sent", 
	                                Toast.LENGTH_SHORT).show();
	                        break;
	                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	                        Toast.makeText(getBaseContext(), "Generic failure", 
	                                Toast.LENGTH_SHORT).show();
	                        break;
	                    case SmsManager.RESULT_ERROR_NO_SERVICE:
	                        Toast.makeText(getBaseContext(), "No service", 
	                                Toast.LENGTH_SHORT).show();
	                        break;
	                    case SmsManager.RESULT_ERROR_NULL_PDU:
	                        Toast.makeText(getBaseContext(), "Null PDU", 
	                                Toast.LENGTH_SHORT).show();
	                        break;
	                    case SmsManager.RESULT_ERROR_RADIO_OFF:
	                        Toast.makeText(getBaseContext(), "Radio off", 
	                                Toast.LENGTH_SHORT).show();
	                        break;
	                }
	            }
	        }, new IntentFilter(SENT));
	 
	        SmsManager sms = SmsManager.getDefault();
	        sms.sendTextMessage(phoneNumber, null, message, sentPI, null);        
	    } 
}

