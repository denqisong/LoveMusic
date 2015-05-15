package mrpan.android.lovemusic.ui;

import java.io.File;
import java.util.ArrayList;

import mrpan.android.lovemusic.R;
import mrpan.android.lovemusic.bean.Song;
import mrpan.android.lovemusic.service.AudioUtils;
import android.app.Activity;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SongList extends Activity {

	private ListView musiclistview;
	//private ScanSdFilesReceiver scanReceiver;

	Context context;
	
	private static final int STARTED = 1001;
	private static final int FINISHED = 1002;

	//private MyHandler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song_list);
		musiclistview = (ListView) findViewById(R.id.song_listview);
//		mHandler = new MyHandler();
		context=this;
		// SimpleAdapter adapter=new ImageSimpleAdapter(this, null, 0, null,
		// null);
		// musiclistview.setAdapter(adapter);
		//getMetaData("sdcard");
		
	//	scanSdCard();
	}
	
	//δ���
	void BindSongList(){
		ArrayList<Song> songs=AudioUtils.getAllSongs(context);
		SimpleAdapter adapter=new ImageSimpleAdapter(context,null,0,null,null);
		musiclistview.setAdapter(adapter);
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

	/**
	 * ����ϵͳapiɨ��sd��
	 */
//	@SuppressWarnings("unused")
//	private void scanSdCard() {
//		Toast.makeText(context, "111111", Toast.LENGTH_LONG).show();
//		IntentFilter intentFilter = new IntentFilter(
//				Intent.ACTION_MEDIA_SCANNER_STARTED);
//		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
//		intentFilter.addDataScheme("file");
//		scanReceiver = new ScanSdFilesReceiver();
//		registerReceiver(scanReceiver, intentFilter);
//		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_STARTED,
//				Uri.parse("file://"+ Environment.getExternalStorageDirectory().toString())));
//		//sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory().toString())));
//	}

	/**
	 * @author ע��ɨ�迪ʼ/��ɵĹ㲥
	 */
//	private class ScanSdFilesReceiver extends BroadcastReceiver {
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
//				// ��ϵͳ��ʼɨ��sd��ʱ��Ϊ���û����飬���Լ���һ���ȴ���
//				mHandler.sendEmptyMessage(STARTED);
//			}
//			if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
//				// ��ϵͳɨ�����ʱ��ֹͣ��ʾ�ȴ��򣬲����²�ѯContentProvider
//				mHandler.sendEmptyMessage(FINISHED);
//			}
//		}
//	}
  
//	class MyHandler extends Handler {
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case STARTED:
//				ScannerMusic();
//				Toast.makeText(context, "111111", Toast.LENGTH_LONG).show();
//				break;
//			case FINISHED:
//				ScannerMusic();
//				Toast.makeText(context, "2222", Toast.LENGTH_LONG).show();
//				break;
//			}
//		}
//	}
}
