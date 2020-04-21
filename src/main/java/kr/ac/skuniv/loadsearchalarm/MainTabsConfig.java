package kr.ac.skuniv.loadsearchalarm;

import android.os.Bundle;

import kr.ac.skuniv.loadsearchalarm.UI.Alarm.UserLstFragment;
import kr.ac.skuniv.loadsearchalarm.UI.Tabs.Tab2Fragment;

public final class MainTabsConfig {

    private static final TabInfo[] TABINFOS = {
            new TabInfo( "알람", R.drawable.ic_alarm, R.drawable.ic_alarm, UserLstFragment.class, null),
            new TabInfo( "지도", R.drawable.mqp6, R.drawable.mqp6, Tab2Fragment.class, null )
    };

    public static final class TABINDEX {
       	public static final int USERLIST = 0;
       	public static final int CHANNELLIST = 1;
       	public static final int SETTINGS = 2;
       	
       	public static final int FIRST = 0;
       	public static final int LAST = TABINFOS.length;
    };
    
    public static final int COUNT_TABS() {
    	return TABINFOS.length;
    }
    
    public static final TabInfo TABINFO( int index ) {
    	return ( index < 0 || index >= COUNT_TABS() )  ? null : TABINFOS[ index ];
    }
    
    public static final class TabInfo {
		public final String tag;
		public final int drawableNormal;
		public final int drawableSelected;
        public final Class<?> klass;
        public final Bundle bundle;
        TabInfo( String tag, int drawableNormal, int drawableSelected, Class<?> klass, Bundle bundle ) {
            this.tag = tag;
            this.drawableNormal = drawableNormal;
            this.drawableSelected = drawableSelected;
            this.klass = klass;
            this.bundle = bundle;
        }
    }
}