package com.blogspot.gambitgeoff.ggeve;

import java.util.Vector;

import com.blogspot.gambitgeoff.ggeve.eveapi.EveAPI;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageAdapter extends BaseAdapter {
	
	private GGEveOverviewActivity myContext;
	private Vector<EveCharacter> myCharacters;
	
	public ImageAdapter(GGEveOverviewActivity inContext)
	{
		myContext = inContext;
		myCharacters = GGEveApplicationRunner.getDatabaseAdapter().getEveCharacters();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myCharacters.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return myCharacters.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return myCharacters.get(position).getCharacterID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView b = new ImageView(myContext);
		final EveCharacter character = myCharacters.get(position);
		b.setImageDrawable(EveAPI.getCharacterImage(character.getCharacterID()));
		b.setLayoutParams(new Gallery.LayoutParams(130,130));
		b.setPadding(20, 20, 20, 20);
		b.setScaleType(ScaleType.FIT_XY);
//		b.setBackgroundResource(style.Widget_Gallery);
		return b;
	}

}
