package com.asep.embedded;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.asep.embedded.R;

public class CustomAdapterAlarm extends BaseAdapter{

	private List<Alarm> rowItems = Collections.emptyList();
	private final Context context;
	
	public CustomAdapterAlarm(Context context)
	{
		this.context = context;
	}
	
	public void updateList(List<Alarm> list)
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
	public Alarm getItem(int arg0) {
		// TODO Auto-generated method stub
		return rowItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return rowItems.indexOf(getItem(arg0));
	}

	public List<Alarm> getList()
	{
		return rowItems;
	}
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(arg1 == null)
		{
			arg1 = LayoutInflater.from(context).inflate(R.layout.itemlistalarm, arg2, false);
		}

		//TextView textId = ViewHolder.get(arg1, R.id.noAlarm);
		TextView textClock = ViewHolder.get(arg1, R.id.clockAlarm);
		//TextView textName = ViewHolder.get(arg1, R.id.nameAlarm);
		//Switch switchBTN = ViewHolder.get(arg1, R.id.switchAlarm);
		final Button switchBTN = ViewHolder.get(arg1, R.id.switchAlarm);
		Alarm item = getItem(position);
		//textId.setText(""+item.getId());
		textClock.setText(Method.getTimeFormat(item.getHour(), item.getMinute()));
		//textName.setText(item.getName());
		Drawable d = switchBTN.getBackground();
		PorterDuffColorFilter filter;
		if(item.getStatus())
		{
			switchBTN.setText("ON");
			filter = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
		}
		else
		{
			switchBTN.setText("OFF");	
	        filter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		}
        d.setColorFilter(filter);
		//switchBTN.setChecked(item.getStatus());
		//switchBTN.setClickable(false);
		/*a.setTag(position);
		a.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Method.toastShort("Clock Alarm "+a.getTag());
			}
		});*/
		switchBTN.setTag(item.getId());
		//Method.addButtonStatusAlarm(switchBTN);
		Method.listButton.set(position, switchBTN);
		switchBTN.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Method.toastShort("Button "+switchBTN.getTag());
				char statusAlarm;
				if(switchBTN.getText().toString().equalsIgnoreCase("on")) statusAlarm=0;
				else statusAlarm=1;
				if(MainActivity.btc.getStatusConnect())Method.sendStatusAlarm((Integer) switchBTN.getTag(), statusAlarm);
				else Method.toastShort(MainActivity.textFailedConnect);
			}
		});
		return arg1;
	}
	
	public void setAdapterItem(int position, Alarm item)
	{
		ThreadPreconditions.checkOnMainThread();
		rowItems.set(position, item);
		notifyDataSetChanged();
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
