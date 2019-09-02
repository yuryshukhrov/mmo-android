package com.yuri.game.ui.adapters;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.yuri.game.ui.activities.MainActivity.TabInfo;

public class TabsPager extends FragmentStatePagerAdapter {
	
	SparseArray<WeakReference<Fragment>> registeredFragments = new SparseArray<WeakReference<Fragment>>();

	private FragmentManager fm;
	private List<TabInfo> tabs;
	private Context context;

	public TabsPager(Context context, FragmentManager fm, List<TabInfo> tabs) {
		super(fm);
		
		this.fm = fm;
		this.tabs = tabs;
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		Log.e("getItem", "CALLED");
		TabInfo info = tabs.get(position);
		return Fragment.instantiate(context, info.fragment.getName(),
				info.args);
	}
	
	@Override
	public int getCount() {
		return tabs.size();
	}
	
	/**
	 * This method can return two predefined types:
	 * 1) PagerAdapter.POSITION_NONE
	 * 2) PagerAdapter.POSITION_UNCHANGED
	 * First type tells system that it should destroy all fragments 
	 * and recreate them according to the getItem specification
	 * Second type tells system that there were no changes in target
	 * collection and implies that fragments won't be destroyed and recreated
	 */
	@Override
	public int getItemPosition(Object object){
		Log.e("getItemPosition", "CALLED");
	    return TabsPager.POSITION_NONE;
	}
	
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, new WeakReference<Fragment>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    	Fragment f = registeredFragments.get(position).get();
    	if (f != null) {
    		registeredFragments.remove(position);
    		f = null;
    	}
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position).get();
    }	
}
