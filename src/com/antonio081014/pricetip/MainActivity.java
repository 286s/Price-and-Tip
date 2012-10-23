package com.antonio081014.pricetip;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private double taxRate;
	private int tipRate;

	private TextView tv_tipTitle;
	private TextView tv_taxTitle;
	private EditText et_singlePrice;
	private EditText et_quantity;
	private TextView tv_tip;
	private TextView tv_afterTax;
	private TextView tv_total;
	private Button btn_addItem;

	private ListView itemList;

	private List<ListItem> list;

	static class ViewHolder {
		public TextView price;
		public TextView quantity;
	}

	BaseAdapter MyAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				convertView = inflater.inflate(R.layout.row_layout, null);
				holder = new ViewHolder();
				holder.price = (TextView) convertView
						.findViewById(R.id.row_view_textview_price);
				holder.quantity = (TextView) convertView
						.findViewById(R.id.row_view_textview_quantity);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();
			holder.price.setText(String.format("%.2f", list.get(position)
					.getPrice()));
			holder.quantity.setText(String.format("%d", list.get(position)
					.getQuantity()));
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		tv_tipTitle = (TextView) findViewById(R.id.activity_main_textview_tipTitle);
		tv_taxTitle = (TextView) findViewById(R.id.activity_main_textview_afterTaxTotal);
		itemList = (ListView) findViewById(R.id.activity_main_listview);
		et_singlePrice = (EditText) findViewById(R.id.activity_main_edittext_singlePrice);
		et_quantity = (EditText) findViewById(R.id.activity_main_edittext_quantity);
		tv_tip = (TextView) findViewById(R.id.activity_main_textview_tipContent);
		tv_afterTax = (TextView) findViewById(R.id.activity_main_textview_afterTaxContent);
		tv_total = (TextView) findViewById(R.id.activity_main_textview_totalContent);
		btn_addItem = (Button) findViewById(R.id.activity_main_button_add);

		list = new ArrayList<ListItem>();
		itemList.setAdapter(MyAdapter);

		itemList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				list.remove(arg2);
				updated();
			}
		});

		btn_addItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String singlePrice = et_singlePrice.getText().toString();
				String quantity = et_quantity.getText().toString();

				// clear the view;
				et_singlePrice.setText("");
				et_quantity.setText("");

				double price = getPrice(singlePrice);
				int quan = getQuantity(quantity);
				list.add(new ListItem(price, quan));
				updated();
			}
		});
	}

	protected void onStart() {
		super.onStart();
		SharedPreferences settings = getSharedPreferences(Predefined.PREF_NAME,
				0);
		taxRate = settings.getFloat(Predefined.PREF_TAXRATE, (float) 8.75);
		tipRate = settings.getInt(Predefined.PREF_TIPRATE, 10);
		// Log.i("Main", String.format("Update: %.2f, %d", taxRate, tipRate));
		tv_tipTitle.setText(String.format("%s:(%d %%)", getResources()
				.getString(R.string.layout_activity_main_textview_tipTitle),
				tipRate));
		tv_taxTitle.setText(String.format(
				"%s:(%.2f %%)",
				getResources().getString(
						R.string.layout_activity_main_textview_aftertaxTitle),
				taxRate));
		updated();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_clear:
			if (list != null) {
				list.clear();
				updated();
			}
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(getApplicationContext(), Setting.class));
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void updated() {
		MyAdapter.notifyDataSetChanged();
		setAll();
	}

	private void setAll() {
		double sum = getTotal();
		double total = getTotalAfterTax(sum, taxRate);
		double tip = getTip(sum, tipRate);
		setAfterTax(total);
		setTip(tip);
		setTotal(total + tip);
	}

	private void setTip(double total) {
		tv_tip.setText(String.format("$%.2f", total));
	}

	private void setAfterTax(double total) {
		tv_afterTax.setText(String.format("$%.2f", total));
	}

	private void setTotal(double sum) {
		tv_total.setText(String.format("$%.2f", sum));
	}

	private double getTip(double sum, int tipRate) {
		return sum * tipRate / 100.0;
	}

	private double getTotal() {
		double sum = 0.0;
		for (ListItem item : list) {
			sum += item.getPrice() * item.getQuantity();
		}
		return sum;
	}

	private double getTotalAfterTax(double sum, double taxRate) {
		return sum * (100.0 + taxRate) / 100.0;
	}

	private double getPrice(String price) {
		try {
			double p = Double.parseDouble(price);
			return p;
		} catch (Exception e) {
			return 3.15;
		}
	}

	private int getQuantity(String quantity) {
		try {
			int q = Integer.parseInt(quantity);
			return q;
		} catch (Exception e) {
			return 1;
		}
	}

}
