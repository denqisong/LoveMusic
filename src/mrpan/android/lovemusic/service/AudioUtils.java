package mrpan.android.lovemusic.service;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import mrpan.android.lovemusic.bean.Song;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

public class AudioUtils {
	public static ArrayList<Song> getAllSongs(Context context) {

		ArrayList<Song> songs = null;

		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media._ID,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media.ALBUM,
						MediaStore.Audio.Media.YEAR,
						MediaStore.Audio.Media.MIME_TYPE,
						MediaStore.Audio.Media.SIZE,
						MediaStore.Audio.Media.DATA,
						MediaStore.Audio.Media.ALBUM_ID },
				MediaStore.Audio.Media.MIME_TYPE + "=? or "
						+ MediaStore.Audio.Media.MIME_TYPE + "=?",
				new String[] { "audio/mpeg", "audio/x-ms-wma" }, null);

		songs = new ArrayList<Song>();

		if (cursor.moveToFirst()) {

			Song song = null;
			do {
				song = new Song();
				song.setSongid(cursor.getLong(0));
				// 文件名
				song.setFileName(cursor.getString(1));
				// 歌曲名
				song.setTitle(cursor.getString(2));
				// 时长
				song.setDuration(cursor.getInt(3));
				// 歌手名
				song.setSinger(cursor.getString(4));
				// 专辑名
				song.setAlbum(cursor.getString(5));
				// 年代
				if (cursor.getString(6) != null) {
					song.setYear(cursor.getString(6));
				} else {
					song.setYear("未知");
				}
				// 歌曲格式
				if ("audio/mpeg".equals(cursor.getString(7).trim())) {
					song.setType("mp3");
				} else if ("audio/x-ms-wma".equals(cursor.getString(7).trim())) {
					song.setType("wma");
				}
				// 文件大小
				if (cursor.getString(8) != null) {
					float size = cursor.getInt(8) / 1024f / 1024f;
					song.setSize((size + "").substring(0, 4) + "M");
				} else {
					song.setSize("未知");
				}
				// 文件路径
				if (cursor.getString(9) != null) {
					song.setFileUrl(cursor.getString(9));
				}
				song.setAlbumid(cursor.getLong(10));
				// 图片
				if (cursor.getString(0) != null && cursor.getString(10) != null) {
					Bitmap img = getMusicBitemp(context, cursor.getLong(0),
							cursor.getLong(10));
					song.setPhoto(img);
				}
				songs.add(song);
			} while (cursor.moveToNext());

			cursor.close();

		}
		return songs;
	}

	// 获取音频文件专辑图片
	private static Bitmap getMusicBitemp(Context context, Long songid,
			Long albumid) {
		Bitmap bm = null;
		if (albumid < 0 && songid < 0) {
			throw new IllegalArgumentException(
					"Must specify an album or a song id");
		}
		try {
			if (albumid < 0) {
				Uri uri = Uri.parse("content://media/external/audio/media/"
						+ songid + "/albumart");
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);
				}
			} else {
				Uri uri = ContentUris.withAppendedId(
						Uri.parse("content://media/external/audio/albumart"),
						albumid);
				ParcelFileDescriptor pfd = context.getContentResolver()
						.openFileDescriptor(uri, "r");
				if (pfd != null) {
					FileDescriptor fd = pfd.getFileDescriptor();
					bm = BitmapFactory.decodeFileDescriptor(fd);
				} else {
					return null;
				}
			}
		} catch (FileNotFoundException ex) {
			return null;
		}
		return bm;
	}

}