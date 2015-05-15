package mrpan.android.lovemusic.ui;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.util.*;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

public class Example 
{
	
//	public static final String[] IMAGE_COLUMN = { MediaStore.Images.Media.DATA,
//			MediaStore.Images.Media.SIZE };
//	public static final String[] AUDIO_COLUMN = { MediaStore.Audio.Media.DATA,
//			MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DURATION,
//			MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ALBUM_ID };
//	public static final String[] VIDEO_COLUMN = { MediaStore.Video.Media.DATA,
//			MediaStore.Video.Media.SIZE, MediaStore.Audio.Media.DURATION };
//	private static final Uri sArtworkUri = Uri
//			.parse("content://media/external/audio/albumart");
//
//	// /创建文件夹
//	public static void createFile(String path) {
//		File file = new File(path);
//		if (!file.exists()) {
//			file.mkdirs();
//		}
//	}
//
//	// 删除文件，并刷新媒体库
//	public static void deleteFile(String path, final Context context,
//			final int type) {
//		File file = new File(path);
//		if (file.exists()) {
//			if (file.isFile()) {
//				file.delete();
//			} else if (file.isDirectory()) {
//				deleteDirs(file);
//			}
//
//		}
//
//		// /监听媒体库刷新情况
//		MediaScannerConnection.scanFile(context,
//				new String[] { "/storage/sdcard0" }, null,
//				new OnScanCompletedListener() {
//
//					@Override
//					public void onScanCompleted(String path, Uri uri) {// //刷新完后做相应处理
//						if (type == 1) {
//							String lists = Example.getVideoFile(context);
//							SentMessage.SendToBluetooth(context,
//									SentMessage.WATCH_PATH, lists);
//						} else if (type == 2) {
//							String lists = Example.getAudioFiles(context);
//							SentMessage.SendToBluetooth(context,
//									SentMessage.WATCH_PATH, lists);
//						} else if (type == 3) {
//							String lists = Example.getImageFiles(context);
//							SentMessage.SendToBluetooth(context,
//									SentMessage.WATCH_PATH, lists);
//						}
//					}
//
//				});// ///
//	}
//
//	public static void deleteDirs(File file) {
//		if (file.isDirectory()) {
//			File[] files = file.listFiles();
//			if (files == null || files.length == 0) {
//				file.delete();
//			} else {
//				for (int i = 0; i < files.length; i++) {
//					if (files[i].isDirectory()) {
//						deleteDirs(files[i]);
//					} else {
//						files[i].delete();
//					}
//				}
//			}
//			file.delete();
//		}
//	}
//
//	// /获取相应目录下的文件、文件夹
//	public static String getFileInfoList(String path) {
//		File file = new File(path);
//		String lists = "";
//		File[] files = file.listFiles();
//		if (files != null) {
//			if (files.length > 0) {
//				for (int i = 0; i < files.length; i++) {
//					if (file.exists()) {
//						if (files[i].isFile()) {
//							lists = lists
//									+ "1,"
//									+ Util.encodeBase64(files[i].getName())
//									+ ","
//									+ Util.encodeBase64(files[i]
//											.getAbsolutePath()) + ","
//									+ getFileSize(files[i]) + "}";
//						} else if (files[i].isDirectory()) {
//							lists = lists
//									+ "0,"
//									+ Util.encodeBase64(files[i].getName())
//									+ ","
//									+ Util.encodeBase64(files[i]
//											.getAbsolutePath()) + ",0}";
//						}
//					}
//				}
//			}
//		}
//		return lists;
//	}
//
//	// //查询视频文件
//	public static String getVideoFile(Context context) {
//		ContentResolver resolver = context.getContentResolver();
//		Cursor cursor = resolver.query(
//				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_COLUMN,
//				null, null, null);
//		String tp = "video}";
//		if (cursor != null) {
//			if (cursor.moveToFirst()) {
//				String path = cursor.getString(cursor
//						.getColumnIndex(VIDEO_COLUMN[0]));
//				String size = cursor.getString(cursor
//						.getColumnIndex(VIDEO_COLUMN[1]));
//				String duration = cursor.getString(cursor
//						.getColumnIndex(VIDEO_COLUMN[2]));
//				// String thumbnail =
//				// Util.bitmaptoString(getVideoThumbnail(path, 96, 96,
//				// MediaStore.Images.Thumbnails.MICRO_KIND));
//				// tp =
//				// tp+Util.encodeBase64(path)+","+size+","+duration+","+thumbnail+"}";
//				tp = tp + Util.encodeBase64(path) + "," + size + "," + duration
//						+ "}";
//				while (cursor.moveToNext()) {
//					path = cursor.getString(cursor
//							.getColumnIndex(VIDEO_COLUMN[0]));
//					size = cursor.getString(cursor
//							.getColumnIndex(VIDEO_COLUMN[1]));
//					duration = cursor.getString(cursor
//							.getColumnIndex(VIDEO_COLUMN[2]));
//					// tp =
//					// tp+Util.encodeBase64(path)+","+size+","+duration+","+thumbnail+"}";
//					tp = tp + Util.encodeBase64(path) + "," + size + ","
//							+ duration + "}";
//				}
//			}
//		}
//		return tp;
//	}
//
//	// 查询音频文件
//	public static String getAudioFiles(Context context) {
//		ContentResolver resolver = context.getContentResolver();
//		Cursor cursor = resolver.query(
//				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, AUDIO_COLUMN,
//				null, null, null);
//		String tp = "audio}";
//		if (cursor != null) {
//			if (cursor.moveToFirst()) {
//				String path = cursor.getString(cursor
//						.getColumnIndex(AUDIO_COLUMN[0]));
//				String size = cursor.getString(cursor
//						.getColumnIndex(AUDIO_COLUMN[1]));
//				String duration = cursor.getString(cursor
//						.getColumnIndex(AUDIO_COLUMN[2]));
//				/*
//				 * String id =
//				 * cursor.getString(cursor.getColumnIndex(AUDIO_COLUMN[3]));
//				 * String albumid =
//				 * cursor.getString(cursor.getColumnIndex(AUDIO_COLUMN[4]));
//				 * Bitmap bt =
//				 * getMusicBitemp(context,Long.valueOf(id),Long.valueOf
//				 * (albumid)); String thumbnail = "audio"; if(bt != null){
//				 * thumbnail = Util.bitmaptoString(getAudioThumbnail(bt, 96,
//				 * 96)); } tp =
//				 * tp+Util.encodeBase64(path)+","+size+","+duration+
//				 * ","+thumbnail+"}";
//				 */
//				tp = tp + Util.encodeBase64(path) + "," + size + "," + duration
//						+ "}";
//				while (cursor.moveToNext()) {
//					path = cursor.getString(cursor
//							.getColumnIndex(AUDIO_COLUMN[0]));
//					size = cursor.getString(cursor
//							.getColumnIndex(AUDIO_COLUMN[1]));
//					duration = cursor.getString(cursor
//							.getColumnIndex(AUDIO_COLUMN[2]));
//					/*
//					 * id =
//					 * cursor.getString(cursor.getColumnIndex(AUDIO_COLUMN[3]));
//					 * albumid =
//					 * cursor.getString(cursor.getColumnIndex(AUDIO_COLUMN[4]));
//					 * bt =
//					 * getMusicBitemp(context,Long.valueOf(id),Long.valueOf(
//					 * albumid)); thumbnail = "audio"; if(bt != null){ thumbnail
//					 * = Util.bitmaptoString(getAudioThumbnail(bt, 96, 96)); }
//					 * tp =
//					 * tp+Util.encodeBase64(path)+","+size+","+duration+","+
//					 * thumbnail+"}";
//					 */
//					tp = tp + Util.encodeBase64(path) + "," + size + ","
//							+ duration + "}";
//				}
//			}
//		}
//		return tp;
//	}
//
//	// //查询图片
//	public static String getImageFiles(Context context) {
//		ContentResolver resolver = context.getContentResolver();
//		Cursor cursor = resolver.query(
//				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_COLUMN,
//				null, null, null);
//		String tp = "image}";
//		if (cursor != null) {
//			if (cursor.moveToFirst()) {
//				String path = cursor.getString(cursor
//						.getColumnIndex(IMAGE_COLUMN[0]));
//				String size = cursor.getString(cursor
//						.getColumnIndex(IMAGE_COLUMN[1]));
//				/*
//				 * String thumbnail =
//				 * Util.bitmaptoString(getImageThumbnail(path, 96, 96)); tp =
//				 * tp+Util.encodeBase64(path)+","+size+","+thumbnail+"}";
//				 */
//				tp = tp + Util.encodeBase64(path) + "," + size + "}";
//				while (cursor.moveToNext()) {
//					path = cursor.getString(cursor
//							.getColumnIndex(AUDIO_COLUMN[0]));
//					size = cursor.getString(cursor
//							.getColumnIndex(AUDIO_COLUMN[1]));
//					/*
//					 * thumbnail = Util.bitmaptoString(getImageThumbnail(path,
//					 * 96, 96)); tp =
//					 * tp+Util.encodeBase64(path)+","+size+","+thumbnail+"}";
//					 */
//					tp = tp + Util.encodeBase64(path) + "," + size + "}";
//				}
//			}
//		}
//		return tp;
//	}
//
//	// /获取音频文件专辑图片
//	public static Bitmap getMusicBitemp(Context context, Long songid,
//			Long albumid) {
//		Bitmap bm = null;
//		if (albumid < 0 && songid < 0) {
//			throw new IllegalArgumentException(
//					"Must specify an album or a song id");
//		}
//		try {
//			if (albumid < 0) {
//				Uri uri = Uri.parse("content://media/external/audio/media/"
//						+ songid + "/albumart");
//				ParcelFileDescriptor pfd = context.getContentResolver()
//						.openFileDescriptor(uri, "r");
//				if (pfd != null) {
//					FileDescriptor fd = pfd.getFileDescriptor();
//					bm = BitmapFactory.decodeFileDescriptor(fd);
//				}
//			} else {
//				Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
//				ParcelFileDescriptor pfd = context.getContentResolver()
//						.openFileDescriptor(uri, "r");
//				if (pfd != null) {
//					FileDescriptor fd = pfd.getFileDescriptor();
//					bm = BitmapFactory.decodeFileDescriptor(fd);
//				} else {
//					return null;
//				}
//			}
//		} catch (FileNotFoundException ex) {
//			return null;
//		}
//		return bm;
//	}
//
//	// //获取图片缩略图
//	public static Bitmap getImageThumbnail(String imagePath, int width,
//			int height) {
//		Bitmap bitmap = null;
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		bitmap = BitmapFactory.decodeFile(imagePath, options);
//		options.inJustDecodeBounds = false;
//
//		int h = options.outHeight;
//		int w = options.outWidth;
//		int beWidth = w / width;
//		int beHeight = h / height;
//		int be = 1;
//		if (beWidth < beHeight) {
//			be = beWidth;
//		} else {
//			be = beHeight;
//		}
//		if (be <= 0) {
//			be = 1;
//		}
//		options.inSampleSize = be;
//
//		bitmap = BitmapFactory.decodeFile(imagePath, options);
//
//		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//		return bitmap;
//	}
//
//	// //获取音频专辑图片的缩略图
//	public static Bitmap getAudioThumbnail(Bitmap bitmap, int width, int height) {
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		int h = bitmap.getHeight();
//		int w = bitmap.getWidth();
//		int beWidth = w / width;
//		int beHeight = h / height;
//		int be = 1;
//		if (beWidth < beHeight) {
//			be = beWidth;
//		} else {
//			be = beHeight;
//		}
//		if (be <= 0) {
//			be = 1;
//		}
//		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//		return bitmap;
//	}
//
//	// ///获取视频文件缩略图
//	public static Bitmap getVideoThumbnail(String videoPath, int width,
//			int height, int kind) {
//		Bitmap bitmap = null;
//
//		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
//		System.out.println("w" + bitmap.getWidth());
//		System.out.println("h" + bitmap.getHeight());
//		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//		return bitmap;
//	}
//
//	// /获取文件大小
//	public static int getFileSize(File file) {
//		InputStream is = null;
//		int size = 0;
//		try {
//			is = new FileInputStream(file);
//			size = is.available();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			if (is != null) {
//				try {
//					is.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		return size;
//	}
}