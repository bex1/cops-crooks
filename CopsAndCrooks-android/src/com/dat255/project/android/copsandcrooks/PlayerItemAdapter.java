package com.dat255.project.android.copsandcrooks;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;

public class PlayerItemAdapter extends BaseAdapter{
	
	Context context;
	private static LayoutInflater inflater = null;
	private List<PlayerItem> data;

	public PlayerItemAdapter(Context context, List<PlayerItem> list) {
        this.context = context;
        this.data = list;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	public void add(PlayerItem playerItem){
		data.add(playerItem);
	    notifyDataSetChanged();
	}
	
	public List<PlayerItem> getData(){
		return data;
	}

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public PlayerItem getItem(int position) {
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
            vi = inflater.inflate(R.layout.player_item, null);
        }
        
        final TextView text = (TextView) vi.findViewById(R.id.playerNameTextView);
        text.setText(data.get(position).getName());
        
        TextView roleText = (TextView) vi.findViewById(R.id.roleTextView);
        roleText.setText("Role: " + data.get(position).getRole().toString());
        
        
        final ImageButton button = (ImageButton) vi.findViewById(R.id.setCopImageButton);
        
        button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				((LobbyActivity)v.getRootView().getContext()).changeRole(getItem(position));
			}
			
        });
        
        return vi;
    }
}
