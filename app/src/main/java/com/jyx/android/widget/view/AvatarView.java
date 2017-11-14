package com.jyx.android.widget.view;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.utils.AvatarUtils;


public class AvatarView extends SimpleDraweeView {
	private static final String PGIF = "portrait.gif";
	private int id;
	private String name;
	private String url;

	public AvatarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public AvatarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AvatarView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		try {
			//setBackgroundResource(R.drawable.ic_default_avatar);
			GenericDraweeHierarchyBuilder builder =
					new GenericDraweeHierarchyBuilder(getResources());
			RoundingParams rp = new RoundingParams();
			rp.setRoundAsCircle(true);
			GenericDraweeHierarchy hierarchy = builder
					.setFadeDuration(300)
					.setFailureImage(getResources().getDrawable(R.mipmap.ic_default_avatar))
					.setPlaceholderImage(getResources().getDrawable(R.mipmap.ic_default_avatar))
					.setRoundingParams(rp)
					.build();
			setHierarchy(hierarchy);

			setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!TextUtils.isEmpty(name) && id != 0) {
						//UIHelper.showUserCenter(getContext(), id, name ,url);
					}
				}
			});
		}
		catch (Exception ignored) {
		}
	}

	public void setUserInfo(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public void setAvatarUrl(String url) {
		this.url = url;
		//if (url.endsWith(PGIF) || StringUtils.isEmpty(url))
		//	setImageBitmap(null);
		//else {
			//ImageLoader.getInstance().displayImage(
			//		AvatarUtils.getMiddleAvatar(url), this);
		//}
		//setImageURI(Uri.parse(AvatarUtils.getMiddleAvatar(url)));

			setImageURI(Uri.parse(AvatarUtils.getMiddleAvatar(url)));
	}
}
