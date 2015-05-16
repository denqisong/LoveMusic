package mrpan.android.lovemusic.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.maxwin.Logger;
import mrpan.android.lovemusic.R;
import mrpan.android.lovemusic.bean.Song;
import mrpan.android.lovemusic.service.AudioUtils;
import mrpan.android.lovemusic.ui.SlidingDeleteListView.MyListViewListener;
import android.app.Activity;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author MrPan
 * @home www.mrpann.com
 */
public class SongList extends Activity implements MyListViewListener,
AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener,OnClickListener{

	private SlidingDeleteListView musiclistview;

	Context context;
	
	private List<Song> songItems = new ArrayList<Song>();
	private SlidingDeleteSlideView mLastSlideViewWithStatusOn;
	private myAdapter slideAdapter;
	private LayoutInflater mInflater;
	private String TAG = "SongList";
	
	private TextView typetx;
	private String normalType = "正常模式";
	private String eidtextType = "编辑模式";
	
	private Button black;
	/** 判断是否长按状态*/
	private boolean isLongState = false;
	/** 记录选中listviw选中项*/
	private HashMap<Integer, Boolean> checkedItemMap = new HashMap<Integer, Boolean>();

	private Handler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song_list);
//		mHandler = new MyHandler();
		context=this;
		musiclistview = (SlidingDeleteListView) findViewById(R.id.song_listview);
		typetx = (TextView) this.findViewById(R.id.typetx);
		typetx.setText(normalType);
		
		black = (Button) this.findViewById(R.id.black);
		black.setVisibility(View.INVISIBLE);
		black.setOnClickListener(this);
		
		isLongState = false;
		

		mHandler = new Handler();
		mInflater = getLayoutInflater();
		init();
		// SimpleAdapter adapter=new ImageSimpleAdapter(this, null, 0, null,
		// null);
		// musiclistview.setAdapter(adapter);
		//getMetaData("sdcard");
		
	//	scanSdCard();
	}
	void init(){
		
		songItems=AudioUtils.getAllSongs(context);
		
		slideAdapter = new myAdapter(this, songItems, mInflater,
				new onSlideListener(), new onDeleteListen());

		musiclistview.setPullLoadEnable(true);
		musiclistview.setEnableSlidingDelete(true);
		musiclistview.setAdapter(slideAdapter);
		musiclistview.setOnItemClickListener(this);
		musiclistview.setOnItemLongClickListener(this);
		musiclistview.setMyListViewListener(this);
	}
	
	
	/**
	 * @Function:加载更多，刷新完成
	 * */
	private void onLoad() {
		musiclistview.stopRefresh();
		musiclistview.stopLoadMore();
		musiclistview.setRefreshTime("");// 刚刚
	}
	
	public void mediaScan(File file) {
	    MediaScannerConnection.scanFile(this,
	            new String[] { file.getAbsolutePath() }, null,
	            new OnScanCompletedListener() {
	                @Override
	                public void onScanCompleted(String path, Uri uri) {
	                    Log.v("MediaScanWork", "file " + path
	                            + " was scanned seccessfully: " + uri);
	                }
	            });
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.black:
			//blackNormalState();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		if(isLongState){
//			Logger.i("编辑状态下点击事件");
//			putCheckedItemMap((int)arg3);
//			updateListviewByDataSourceNoChange(true);
//		}else{
//			Logger.i("正常状态下点击事件");
//		
//		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (!isLongState) {
//			isLongState = true;
//			black.setVisibility(View.VISIBLE);
//			typetx.setText(eidtextType);
//			musiclistview.setEnableSlidingDelete(false);
		}
		return false;
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				songItems.clear();
				init();
				slideAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				init();
				slideAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	private void blackNormalState() {
		if (isLongState) {
			isLongState = false;
			typetx.setText(normalType);
			black.setVisibility(View.INVISIBLE);
			checkedItemMap.clear();
			musiclistview.setEnableSlidingDelete(true);
			updateListviewByDataSourceNoChange(false);
		}
	}
	
	/**
	 * @Function:收集选中listview item 状态
	 * */
	private void putCheckedItemMap(int key) {
		// checkedItemMap
		if (checkedItemMap.containsKey(key)) {
			boolean value = checkedItemMap.get(key);
			if (value) {
				checkedItemMap.remove(key);
			}
		} else {
			checkedItemMap.put(key, true);
		}
	}
	/**
	 * @Function:强制刷新listview(数据源没有变化的情况)
	 * */
	private void updateListviewByDataSourceNoChange(boolean isLongState){
		slideAdapter.setIsLongState(isLongState);
		slideAdapter.setCheckItemMap(checkedItemMap);
		slideAdapter.notifyDataSetChanged();
		musiclistview.setAdapter(slideAdapter);
		musiclistview.setSelectionFromTop();
	}
	
	@Override
	public void onScrollListener(int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollListenerBottom() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @Function:删除监听
	 * */
	private class onDeleteListen implements myAdapter.OnDeleteListener {
		@Override
		public void onDelete(View view, int position) {
			songItems.remove(position);
			slideAdapter.notifyDataSetChanged();
		}

	}
	
	/**
	 * @Function:监听选中listview item 项,保证选中只有一项是处于打开状态
	 * */
	private class onSlideListener implements SlidingDeleteSlideView.OnSlideListener {
		@Override
		public void onSlide(View view, int status) {
			if (mLastSlideViewWithStatusOn != null
					&& mLastSlideViewWithStatusOn != view) {
				mLastSlideViewWithStatusOn.shrink();
			}

			if (status == SLIDE_STATUS_ON) {
				mLastSlideViewWithStatusOn = (SlidingDeleteSlideView) view;
			}
		}
	}
}
