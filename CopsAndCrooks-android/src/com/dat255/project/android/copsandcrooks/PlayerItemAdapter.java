package com.dat255.project.android.copsandcrooks;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dat255.project.android.copsandcrooks.model.Role;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;

/**
 * This class represents a BaseAdapter that handles PlayerItems.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class PlayerItemAdapter extends BaseAdapter{

	private static LayoutInflater inflater = null;
	private List<PlayerItem> data;

	/**
	 * Create a new PlayerItemAdapter.
	 * @param context the activity that uses it
	 * @param list a list of PlayerItems
	 */
	public PlayerItemAdapter(Context context, List<PlayerItem> list) {
        this.data = list;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	/**
	 * Add a PlayerItem to the adapter.
	 * @param playerItem the PlayerItem to be added
	 */
	public void add(PlayerItem playerItem){
		data.add(playerItem);
	    notifyDataSetChanged();
	}
	
	/**
	 * Get the PlayerItem-list.
	 * @return the PlayerItem-list
	 */
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
        
        //TextView that displays a player's name
        final TextView text = (TextView) vi.findViewById(R.id.playerNameTextView);
        text.setText(data.get(position).getName());
        
        //TextView that displays a player's role.
        TextView roleText = (TextView) vi.findViewById(R.id.roleTextView);
        roleText.setText("Role: " + data.get(position).getRole().toString());
        
        
        ImageButton button = (ImageButton) vi.findViewById(R.id.setCopImageButton);
        button.setClickable(getItem(position).getRole() != Role.Cop);
        button.setEnabled(getItem(position).getRole() != Role.Cop);
        
        button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				((LobbyActivity)v.getRootView().getContext()).changeRole(getItem(position));
			}
			
        });
        
        return vi;
    }
}
