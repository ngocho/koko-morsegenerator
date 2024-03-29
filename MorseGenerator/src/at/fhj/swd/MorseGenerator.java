package at.fhj.swd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.StringTokenizer;
import android.util.Log;

/**
 * Translates normal Text to the morse-alphabet.
 * Implemented as a SINGLETON
 * @author Matthias Koinegg
 */
public class MorseGenerator {

	private static MorseGenerator instance = null;
	
	HashMap<String,String> ascii2morse;
	
	/**
	 * Reads the morse-alphabet from a flat file (in /res/raw/..) into an HashMap 
	 * @param mainAct Needs reference to mainAct for access to the input-file 	 */
	protected MorseGenerator(MainAct mainAct)
	{
		ascii2morse = new HashMap<String,String> ();
		
		// TODO add more errorhandling here
		Log.i("MorseGenerator", "Opening Input-Stream and BufferedReader...");
		InputStream is = mainAct.getResources().openRawResource(R.raw.ascii2morse);		
		BufferedReader bufRdr=null;
		try {
			bufRdr = new BufferedReader(new InputStreamReader(is,"UTF8"));			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String line = null;		
		try {			
			while((line = bufRdr.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line,"|");				
				while (st.hasMoreTokens())
				{
					ascii2morse.put(st.nextToken(),st.nextToken());
					//Log.i("FromFile",st.nextToken() + "|" + st.nextToken());
				}
			}
			bufRdr.close();
			Log.i("MorseGenerator", "Read from file..." + ascii2morse.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns an instance of the MorseGenerator-Singleton
	 */
	public static MorseGenerator getInstance(MainAct mainAct) {
		if (instance == null) {
			Log.i("MorseGenerator","First access");
			instance = new MorseGenerator(mainAct);
		}
		return instance;
	}
	
	/**
	 * @param inputText ASCII-Text which should be translated to morsecode
	 * @return Morsecode as a String 
	 */
	public String translate(String inputText)
	{
		if ((inputText == null) || (inputText.length()==0))
		{
			return "";
		}
		inputText = inputText.toUpperCase(); // there are only uppercase characters in morsecode
		
		StringBuilder sb = new StringBuilder();
		//translate every single character to morsecode
		for (int i=0; i<inputText.length();i++)
		{
			Log.i(inputText.substring(i,i+1),ascii2morse.get(inputText.substring(i,i+1)));
			if (!ascii2morse.containsKey(inputText.substring(i,i+1)))
			{	// Write an error to log if inputtext contains unsupported character
				Log.e("MorseGenerator", "Found no Morsecode for char " +  inputText.substring(i,i+1));
			}
			else
			{
			sb.append(ascii2morse.get(inputText.substring(i,i+1)));
			sb.append(" "); // put a space between every character
			}
		}		
		return sb.toString();
	}
}
