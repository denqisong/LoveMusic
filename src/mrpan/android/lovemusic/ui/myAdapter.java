package mrpan.android.lovemusic.ui;

import java.util.HashMap;
import java.util.List;

import mrpan.android.lovemusic.R;
import mrpan.android.lovemusic.bean.Song;
import mrpan.android.lovemusic.ui.SlidingDeleteSlideView.OnSlideListener;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author MrPan
 * @home www.mrpann.com
 */
public class myAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
	private List<Song> songInfos;
	private OnSlideListener onSlideListener;
	private OnDeleteListener onDeleteListen;
	private boolean isLongState;
	private HashMap<Integer, Boolean> checkedItemMap = new HashMap<Integer, Boolean>();
	@SuppressWarnings("unused")
	private Drawable longStateItemCheckedBg, longStateItemNormalBg;

	public myAdapter(Context context, List<Song> songInfos,
			LayoutInflater mInflater, OnSlideListener onSlideListener,
			OnDeleteListener onDeleteListen) {
		super();
		this.context = context;
		this.mInflater = mInflater;
		this.songInfos = songInfos;
		this.onSlideListener = onSlideListener;
		this.onDeleteListen = onDeleteListen;

		longStateItemCheckedBg = context.getResources().getDrawable(
				R.drawable.xlistview_check_bg);
		longStateItemNormalBg = context.getResources().getDrawable(
				R.drawable.xlistview_bg);
	}

	@Override
	public int getCount() {
		return songInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return songInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int arg0, final View arg1, ViewGroup arg2) {
		ViewHolder holder;
		SlidingDeleteSlideView slideView = (SlidingDeleteSlideView) arg1;

		if (slideView == null) {
			View itemView = mInflater.inflate(R.layout.list_item, null);
			slideView = new SlidingDeleteSlideView(context);
			slideView.setContentView(itemView);

			holder = new ViewHolder(slideView);
			slideView.setOnSlideListener(onSlideListener);
			slideView.setTag(holder);
		} else {
			holder = (ViewHolder) slideView.getTag();
		}
		Song item = songInfos.get(arg0);
		item.slideView = slideView;
		item.slideView.shrinkByFast();

		if (isLongState) {
			if (checkedItemMap.containsKey(arg0)) {
				holder.xlist_item_relayout
						.setBackgroundDrawable(longStateItemCheckedBg);
			} else {
				holder.xlist_item_relayout.setBackgroundColor(0xFFFFFFFF);
			}
		}
		if(item.getPhoto()!=null)
			holder.icon.setImageBitmap(item.getPhoto());
		else
			holder.icon.setImageResource(R.drawable.icon);
		holder.title.setText(item.getTitle());
		holder.msg.setText(item.getSinger());
		// holder.res.
		holder.deleteHolder.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onDeleteListen.onDelete(arg1, arg0);
			}
		});

		return slideView;
	}

	private static class ViewHolder {
		public RelativeLayout xlist_item_relayout;
		public ImageView icon;
		public TextView title;
		public TextView msg;
		@SuppressWarnings("unused")
		public CheckBox res;
		public ViewGroup deleteHolder;

		ViewHolder(View view) {
			xlist_item_relayout = (RelativeLayout) view
					.findViewById(R.id.xlist_item_relayout);
			icon = (ImageView) view.findViewById(R.id.icon);
			title = (TextView) view.findViewById(R.id.title);
			msg = (TextView) view.findViewById(R.id.msg);
			res = (CheckBox) view.findViewById(R.id.res);
			deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
		}
	}

	public interface OnDeleteListener {
		public void onDelete(View view, int position);
	}

	public void setIsLongState(boolean isLongState) {
		this.isLongState = isLongState;
	}

	public void setCheckItemMap(HashMap<Integer, Boolean> checkedItemMap) {
		this.checkedItemMap = checkedItemMap;
	}

}
