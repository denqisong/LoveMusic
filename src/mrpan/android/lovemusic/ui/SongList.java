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
import android.widget.TextView;
import android.widget.Toast;

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
	
	public static Button playButton;
	
	int play_state, pesition;//����״̬
	
	public static TextView song_nameView, singer_nameView;
	
	View mini_playview;
	
	Bitmap bm;
	public static ImageView mini_play_image;
	
	URL album;
	long musicAlbum_ID;// ����ר��ID
	long musicID;// ����ID
	
	private TextView typetx;
//	private String normalType = "����ģʽ";
//	private String eidtextType = "�༭ģʽ";
	
	private Button black;
	/** �ж��Ƿ񳤰�״̬*/
	private boolean isLongState = false;
	/** ��¼ѡ��listviwѡ����*/
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
		mini_playview=this.findViewById(R.id.mini_playview);
		mini_playview.setOnClickListener(this);
		//typetx.setText(normalType);
		playButton=(Button) this.findViewById(R.id.play_or_pause);
		mini_play_image = (ImageView) findViewById(R.id.mini_image);
		//song_num = (TextView) findViewById(R.id.allmusic_text2);
		song_nameView = (TextView) findViewById(R.id.song_name);
		singer_nameView = (TextView) findViewById(R.id.singer_name);
		
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
		// ���͹㲥�������ڷ����и��¸�ҳUI
				Intent intent = new Intent();
				intent.setAction("UI");
				intent.putExtra("UISTATE", "1");
				sendBroadcast(intent);

				IntentFilter filter = new IntentFilter();
				filter.addAction("isplay");
				filter.addAction("info");
				registerReceiver(receiver, filter);
	}
	
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
				// ��һ�θ��ݸ�����������UI
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
	
	void init(){
		
		songItems=AudioUtils.getAllSongs(context);
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
	 * @Function:���ظ��࣬ˢ�����
	 * */
	private void onLoad() {
		musiclistview.stopRefresh();
		musiclistview.stopLoadMore();
		musiclistview.setRefreshTime("");// �ո�
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
		Logger.i("ID:"+id);
		switch (id) {
		case R.id.black:
			//blackNormalState();
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
				playButton.setBackgroundResource(R.drawable.mini_play_button_pressed);
			}
			break;
//		case R.id.play_or_pause:
//			
//			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		if(isLongState){
//			Logger.i("�༭״̬�µ���¼�");
//			putCheckedItemMap((int)arg3);
//			updateListviewByDataSourceNoChange(true);
//		}else{
//			Logger.i("����״̬�µ���¼�");
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
			//typetx.setText(normalType);
			black.setVisibility(View.INVISIBLE);
			checkedItemMap.clear();
			musiclistview.setEnableSlidingDelete(true);
			updateListviewByDataSourceNoChange(false);
		}
	}
	
	/**
	 * @Function:�ռ�ѡ��listview item ״̬
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
	 * @Function:ǿ��ˢ��listview(����Դû�б仯�����)
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
	 * @Function:ɾ������
	 * */
	private class onDeleteListen implements myAdapter.OnDeleteListener {
		@Override
		public void onDelete(View view, int position) {
			songItems.remove(position);
			slideAdapter.notifyDataSetChanged();
		}

	}
	
	/**
	 * @Function:����ѡ��listview item ��,��֤ѡ��ֻ��һ���Ǵ��ڴ�״̬
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
