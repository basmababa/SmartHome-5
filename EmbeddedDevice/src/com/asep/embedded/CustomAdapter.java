package com.asep.embedded;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.asep.embedded.R;


public class CustomAdapter extends BaseAdapter{

	private List<DummyClass> rowItems = Collections.emptyList();
	private final Context context;
	
	public CustomAdapter(Context context)
	{
		this.context = context;
	}
	
	public void updateList(List<DummyClass> list)
	{
		ThreadPreconditions.checkOnMainThread();
		this.rowItems =list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rowItems.size();
	}

	@Override
	public DummyClass getItem(int arg0) {
		// TODO Auto-generated method stub
		return rowItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return rowItems.indexOf(getItem(arg0));
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewgroup) {
		// TODO Auto-generated method stub
		if(view == null)
		{
			view = LayoutInflater.from(context).inflate(R.layout.itemlistdevice, viewgroup, false);
		}
		
		TextView textName = ViewHolder.get(view, R.id.textName);
		final Switch switchBTN = ViewHolder.get(view, R.id.switchBTN);
		final DummyClass item = getItem(position);
		textName.setText(item.getName());
		switchBTN.setChecked(item.getStatus());
		switchBTN.setTag(position);
		switchBTN.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				item.setStatus(switchBTN.isChecked());
				int buttonPosition = Integer.parseInt(switchBTN.getTag().toString());
				rowItems.set(buttonPosition, item);
				ThreadPreconditions.checkOnMainThread();
				notifyDataSetChanged();
				notifyDataSetInvalidated();
				if(ListDevice.mode.equalsIgnoreCase(Method.alarm)) 
				{
					ListAlarm.itemGlobal.setDeviceStatus(buttonPosition, switchBTN.isChecked());
				}
				else Method.saveOneSettingDevice(buttonPosition, switchBTN.isChecked());
			}
		});	
		return view;
	}
	
	public static class ViewHolder
	{
		@SuppressWarnings("unchecked")
		public static <T extends View> T get(View view, int id) {
	        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
	        if (viewHolder == null) {
	            viewHolder = new SparseArray<View>();
	            view.setTag(viewHolder);
	        }
	        View childView = viewHolder.get(id);
	        if (childView == null) {
	            childView = view.findViewById(id);
	            viewHolder.put(id, childView);
	        }
	        return (T) childView;
	    }
	}
}
