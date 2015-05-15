package mrpan.android.lovemusic.ui;

import java.io.File;
import java.util.ArrayList;

import mrpan.android.lovemusic.R;
import mrpan.android.lovemusic.bean.Song;
import mrpan.android.lovemusic.service.AudioUtils;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class MainPlayFrame extends Activity {

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
		init();
		ArrayList<Song> songs=AudioUtils.getAllSongs(context);
		Toast.makeText(context, "Size:"+songs.size(),Toast.LENGTH_LONG).show();
	//	scanSdCard();
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
	public void ScannerMusic() {
		// 查询媒体数据库
		// MediaScannerConnection.scanFile(this, new String[]{"/sdcard"}, null,
		// null);

		Cursor cursor = this.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		//System.out.println("size:" + );
		Toast.makeText(context, cursor.getCount(), Toast.LENGTH_LONG).show();
		// 遍历媒体数据库
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				// 歌曲编号
				int id = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
				// 歌曲id
				int trackId = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
				// 歌曲标题
				String title = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				// 歌曲的专辑名：MediaStore.Audio.Media.ALBUM
				String album = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				// 歌曲的歌手名： MediaStore.Audio.Media.ARTIST
				String artist = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				// 歌曲文件的路径 ：MediaStore.Audio.Media.DATA
				String url = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				// 歌曲的总播放时长：MediaStore.Audio.Media.DURATION
				int duration = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
				// 歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
				Long size = cursor.getLong(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
				// 歌曲文件显示名字
				String disName = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
				Log.e("music disName=", disName);// 打印出歌曲名字
				
				cursor.moveToNext();
			}
			cursor.close();
		}
	}

	void init(){
		MediaScannerConnection.scanFile(this, new String[] { Environment
		        .getExternalStorageDirectory().getAbsolutePath() }, null, null);
	}
	/**
	 * 调用系统api扫描sd卡
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
	 * @author 注册扫描开始/完成的广播
	 */
//	private class ScanSdFilesReceiver extends BroadcastReceiver {
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
//				// 当系统开始扫描sd卡时，为了用户体验，可以加上一个等待框
//				mHandler.sendEmptyMessage(STARTED);
//			}
//			if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
//				// 当系统扫描完毕时，停止显示等待框，并重新查询ContentProvider
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
