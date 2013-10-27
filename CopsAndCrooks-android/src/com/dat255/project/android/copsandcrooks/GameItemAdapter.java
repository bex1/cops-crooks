package com.dat255.project.android.copsandcrooks;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dat255.project.android.copsandcrooks.network.GameItem;

/**
 * This class represents a BaseAdapter used for handling GameItems.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class GameItemAdapter extends BaseAdapter{
	
	private static LayoutInflater inflater = null;
	private List<GameItem> data;

	/**
	 * Create a new GameItemAdapter.
	 * @param context the activity that this adapter belongs to
	 * @param data a list of the items in the adapter
	 */
	public GameItemAdapter(Context context, List<GameItem> data) {
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	/**
	 * Add a GameItem to the adapterlist.
	 * @param gameItem the GameItem to add
	 */
	public void add(GameItem gameItem){
		data.add(gameItem);
	    notifyDataSetChanged();
	}
	
	/**
	 * Get the list of GameItems.
	 * @return the GameItem-list
	 */
	public List<GameItem> getData(){
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
        
        //The textview that contains the game's name
        TextView text = (TextView) vi.findViewById(R.id.nameTextView);
        text.setText(data.get(position).getName());
        
        vi.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				//calls the itemAnswer method in GameBrowseActivity, updating the item
				((GameBrowseActivity)arg0.getContext()).itemAnswer(getItem(position));
			}
        	
        });
        
        return vi;
    }
}
