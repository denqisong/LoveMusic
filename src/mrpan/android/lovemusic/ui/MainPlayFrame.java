package mrpan.android.lovemusic.ui;

import mrpan.android.lovemusic.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;

public class MainPlayFrame extends Activity {

	private ListView musiclistview;
	private ScanSdFilesReceiver scanReceiver;

	private static final int STARTED = 1001;
	private static final int FINISHED = 1002;

	private MyHandler mHandler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song_list);
		musiclistview = (ListView) findViewById(R.id.song_listview);
		mHandler = new MyHandler();
		// SimpleAdapter adapter=new ImageSimpleAdapter(this, null, 0, null,
		// null);
		// musiclistview.setAdapter(adapter);
		ScannerMusic();

	}

	public void ScannerMusic() {
		// ��ѯý�����ݿ�
		// MediaScannerConnection.scanFile(this, new String[]{"/sdcard"}, null,
		// null);

		Cursor cursor = this.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		System.out.println("size:" + cursor.getCount());
		// ����ý�����ݿ�
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				// �������
				int id = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
				// ����id
				int trackId = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
				// ��������
				String title = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				// ������ר������MediaStore.Audio.Media.ALBUM
				String album = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
				// �����ĸ������� MediaStore.Audio.Media.ARTIST
				String artist = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
				// �����ļ���·�� ��MediaStore.Audio.Media.DATA
				String url = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				// �������ܲ���ʱ����MediaStore.Audio.Media.DURATION
				int duration = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
				// �����ļ��Ĵ�С ��MediaStore.Audio.Media.SIZE
				Long size = cursor.getLong(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
				// �����ļ���ʾ����
				String disName = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
				Log.e("music disName=", disName);// ��ӡ����������
				cursor.moveToNext();
			}
			cursor.close();
		}
	}

	/**
	 * ����ϵͳapiɨ��sd��
	 */
	@SuppressWarnings("unused")
	private void scanSdCard() {
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addDataScheme("file");
		scanReceiver = new ScanSdFilesReceiver();
		registerReceiver(scanReceiver, intentFilter);
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.parse("file://"
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath())));
	}

	/**
	 * @author ע��ɨ�迪ʼ/��ɵĹ㲥
	 */
	private class ScanSdFilesReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
				// ��ϵͳ��ʼɨ��sd��ʱ��Ϊ���û����飬���Լ���һ���ȴ���
				mHandler.sendEmptyMessage(STARTED);
			}
			if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
				// ��ϵͳɨ�����ʱ��ֹͣ��ʾ�ȴ��򣬲����²�ѯContentProvider
				mHandler.sendEmptyMessage(FINISHED);
			}
		}
	}

	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STARTED:
				break;
			case FINISHED:
				// initData();
				break;
			}
		}
	}
}
