/**
 * 
 */
package com.antonio081014.pricetip;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

/**
 * @author antonio081014
 * 
 */
public class Setting extends Activity {

	private EditText et_taxRate;
	private EditText et_tipRate;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		et_taxRate = (EditText) findViewById(R.id.setting_edittext_taxrate);
		et_tipRate = (EditText) findViewById(R.id.setting_edittext_tiprate);
	}

	protected void onStart() {
		super.onStart();
		SharedPreferences settings = getSharedPreferences(Predefined.PREF_NAME,
				0);
		et_taxRate.setText(Float.toString(settings.getFloat(
				Predefined.PREF_TAXRATE,
				Float.parseFloat(getResources().getString(
						R.string.setting_default_taxrate_value)))));
		et_tipRate.setText(Integer.toString(settings.getInt(
				Predefined.PREF_TIPRATE,
				Integer.parseInt(getResources().getString(
						R.string.setting_default_tiprate_value)))));
	}

	protected void onStop() {
		super.onStop();
		SharedPreferences settings = getSharedPreferences(Predefined.PREF_NAME,
				0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(Predefined.PREF_TIPRATE, getTipRate());
		editor.putFloat(Predefined.PREF_TAXRATE, (float) getTaxRate());
		editor.commit();
	}

	private double getTaxRate() {
		try {
			double rate = Double.parseDouble(et_taxRate.getText().toString());
			return rate;
		} catch (Exception e) {
			return Double.parseDouble(getResources().getString(
					R.string.setting_default_taxrate_value));
		}
	}

	private int getTipRate() {
		try {
			int tipRate = Integer.parseInt(et_tipRate.getText().toString());
			return tipRate;
		} catch (Exception e) {
			return Integer.parseInt(getResources().getString(
					R.string.setting_default_tiprate_value));
		}
	}
}
