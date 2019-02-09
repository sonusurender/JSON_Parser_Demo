package com.json_demo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JSONData_Adapter extends BaseAdapter {
	private Context context;
	private ArrayList<JSONData_Model> jsonList;
	private String from = "";// "from" variable is used to check from which
								// parser the adapter is called

	public JSONData_Adapter(Context context,
			ArrayList<JSONData_Model> arrayList, String from) {
		this.context = context;
		this.jsonList = arrayList;
		this.from = from;
	}

	@Override
	public int getCount() {

		return jsonList.size();
	}

	@Override
	public JSONData_Model getItem(int position) {

		return jsonList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.custom_listview, parent, false);
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.location = (TextView) view.findViewById(R.id.location);
			holder.phone = (TextView) view.findViewById(R.id.phone_number);

			// If adapter is called from local json parser then get the data
			// from local json parser getter
			if (from.equalsIgnoreCase("Local")) {
				holder.name.setText(jsonList.get(position).getName());
				holder.location.setText(jsonList.get(position).getLocation());
				String[] contactNumber = jsonList.get(position)
						.getPhoneNumber();
				String contact = "";
				for (int i = 0; i < contactNumber.length; i++) {
					contact += contactNumber[i] + "  ";
				}
				holder.phone.setText(contact);

			}

			// Else If adapter is called from server json parser then get the
			// data from server json parser getter
			else if (from.equalsIgnoreCase("Server")) {
				holder.name.setText(jsonList.get(position).getTitle());
				holder.location.setText(jsonList.get(position).getStatus());
				holder.phone.setText(jsonList.get(position).getDate());
			}

			// Else display error
			else {
				holder.name.setText("Error in parsing.");
			}

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		return view;
	}

	// ViewHolder to hold the views
	private class ViewHolder {
		TextView name, location, phone;
	}

}
