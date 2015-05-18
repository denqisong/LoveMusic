package mrpan.android.lovemusic.ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mrpan.android.lovemusic.R;
import mrpan.android.lovemusic.bean.Logger;
import mrpan.android.lovemusic.bean.Song;
import mrpan.android.lovemusic.service.AudioUtils;
import mrpan.android.lovemusic.service.MediaPlayService;
import mrpan.android.lovemusic.service.MusicUtils;
import mrpan.android.lovemusic.ui.SlidingDeleteListView.MyListViewListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author MrPan
 * @home www.mrpann.com
 */
public class SongList extends Activity implements MyListViewListener,
		AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener,
		OnClickListener {

	private SlidingDeleteListView musiclistview;

	Context context;

	private List<Song> songItems = new ArrayList<Song>();
	private SlidingDeleteSlideView mLastSlideViewWithStatusOn;
	private myAdapter slideAdapter;
	private LayoutInflater mInflater;
	private String TAG = "SongList";

	View view;
	ImageView imageView;

	public static Button playButton;

	int play_state, pesition;// 播放状态

	public static TextView song_nameView, singer_nameView;

	View mini_playview,mini_view;

	Bitmap bm;
	public static ImageView mini_play_image;

	URL album;
	long musicAlbum_ID;// 歌曲专辑ID
	long musicID;// 歌曲ID

	private TextView typetx;
	// private String normalType = "正常模式";
	// private String eidtextType = "编辑模式";

	private Button black;
	/** 判断是否长按状态 */
	private boolean isLongState = false;
	/** 记录选中listviw选中项 */
	private HashMap<Integer, Boolean> checkedItemMap = new HashMap<Integer, Boolean>();

	private Handler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song_list);
		context = this;
		musiclistview = (SlidingDeleteListView) findViewById(R.id.song_listview);
		typetx = (TextView) this.findViewById(R.id.typetx);
		mini_playview = this.findViewById(R.id.mini_playview);
		mini_playview.setOnClickListener(this);
		playButton = (Button) this.findViewById(R.id.play_or_pause);
		mini_play_image = (ImageView) findViewById(R.id.mini_image);
		song_nameView = (TextView) findViewById(R.id.song_name);
		singer_nameView = (TextView) findViewById(R.id.singer_name);
		mini_view=findViewById(R.id.click_to_playing);
		mini_view.setOnClickListener(this);

		black = (Button) this.findViewById(R.id.black);
		black.setVisibility(View.INVISIBLE);
		black.setOnClickListener(this);

		isLongState = false;

		mHandler = new Handler();
		mInflater = getLayoutInflater();
		init();
		// getMetaData("sdcard");

		// scanSdCard();
		// 发送广播给服务在服务中跟新该页UI
		Intent intent = new Intent();
		intent.setAction("UI");
		intent.putExtra("UISTATE", "1");
		sendBroadcast(intent);

		IntentFilter filter = new IntentFilter();
		filter.addAction("isplay");
		filter.addAction("info");
		registerReceiver(receiver, filter);
	};

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("isplay")) {
				play_state = intent.getIntExtra("state", 0);
				if (play_state == 1) {
					playButton
							.setBackgroundResource(R.drawable.mini_play_button);

				} else {
					playButton
							.setBackgroundResource(R.drawable.mini_pause_button_pressed);
				}
			}
			if (intent.getAction().equals("info")) {
				// 第一次根据歌曲索引跟新UI
				pesition = intent.getIntExtra("pesition", -1);
				song_nameView.setText(songItems.get(pesition).getTitle()
						.toString());
				singer_nameView.setText(songItems.get(pesition).getSinger()
						.toString());

				musicID = songItems.get(pesition).getSongid();
				musicAlbum_ID = songItems.get(pesition).getAlbumid();

				bm = MusicUtils.getArtWork(SongList.this, musicID,
						musicAlbum_ID, true);
				mini_play_image.setImageBitmap(bm);
			}
		}
	};

	void init() {

		songItems = AudioUtils.getAllSongs(context);
		song_nameView.setMovementMethod(ScrollingMovementMethod.getInstance());
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
		Logger.i("ID:" + id);
		switch (id) {
		case R.id.black:
			// blackNormalState();
			break;
		case R.id.mini_playview:
			if (play_state == 1) {
				Intent intent = new Intent(SongList.this,
						MediaPlayService.class);
				intent.putExtra("what", "pause");
				intent.putExtra("pesition", pesition);
				startService(intent);
				play_state = 0;
				playButton.setBackgroundResource(R.drawable.mini_pause_button);
			} else if (play_state == 0) {
				Intent intent = new Intent(SongList.this,
						MediaPlayService.class);
				intent.putExtra("what", "restart");
				intent.putExtra("pesition", pesition);
				startService(intent);
				play_state = 1;
				playButton
						.setBackgroundResource(R.drawable.mini_play_button_pressed);
			}
			break;
		case R.id.click_to_playing:
			Intent intent = new Intent(SongList.this,
					MainPlaying.class);
			startActivity(intent);
			break;
		// case R.id.play_or_pause:
		//
		// break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(SongList.this, MediaPlayService.class);
		intent.putExtra("pesition", arg2 - 1);
		if (pesition != arg2) {
			intent.putExtra("what", "play");
		}
		if (pesition == arg2) {
			intent.putExtra("what", "pause");
		}
		if (pesition == arg2 && play_state == 0) {
			intent.putExtra("what", "restart");
		}
		startService(intent);

		if (view == null) {
			view = arg1;
			imageView = (ImageView) view.findViewById(R.id.isplay);
			imageView.setVisibility(View.VISIBLE);
		} else {
			imageView = (ImageView) view.findViewById(R.id.isplay);
			imageView.setVisibility(View.GONE);

			imageView = (ImageView) arg1.findViewById(R.id.isplay);
			imageView.setVisibility(View.VISIBLE);

			view = arg1;

		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (!isLongState) {
			// isLongState = true;
			// black.setVisibility(View.VISIBLE);
			// typetx.setText(eidtextType);
			// musiclistview.setEnableSlidingDelete(false);
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
			// typetx.setText(normalType);
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
	private void updateListviewByDataSourceNoChange(boolean isLongState) {
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
	private class onSlideListener implements
			SlidingDeleteSlideView.OnSlideListener {
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
}
