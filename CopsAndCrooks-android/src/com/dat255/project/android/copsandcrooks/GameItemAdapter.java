package com.dat255.project.android.copsandcrooks;

import java.util.List;

import com.dat255.project.android.copsandcrooks.network.GameItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GameItemAdapter extends BaseAdapter{
	
	Context context;
	private static LayoutInflater inflater = null;
	private List<GameItem> data;

	public GameItemAdapter(Context context, List<GameItem> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	public void add(GameItem gameItem){
		data.add(gameItem);
	    notifyDataSetChanged();
	}
	
	public List getData(){
		return data;
	}

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public GameItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        
        if (vi == null){
            vi = inflater.inflate(R.layout.game_item, null);
        }
        
        TextView text = (TextView) vi.findViewById(R.id.nameTextView);
        text.setText(data.get(position).getName());
        
        vi.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				((GameBrowseActivity)arg0.getContext()).itemAnswer(getItem(position));
			}
        	
        });
        
        return vi;
    }
}
