package com.jyx.android.model.provider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.jyx.android.R;
import com.jyx.android.activity.chat.LocationActivity;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * Created by Administrator on 1/25/2016.
 */

public class LocationProvider extends InputProvider.ExtendProvider {

    HandlerThread mWorkThread;
    Handler mUploadHandler;
    private int REQUEST_LOCATION = 20;

    public LocationProvider(RongContext context) {
        super(context);
        mWorkThread = new HandlerThread("RongDemo");
        mWorkThread.start();
        mUploadHandler = new Handler(mWorkThread.getLooper());
    }

    /**
     * 设置展示的图标
     * @param context
     * @return
     */
    @Override
    public Drawable obtainPluginDrawable(Context context) {
        //R.drawable.de_contacts 通讯录图标
        return context.getResources().getDrawable(R.mipmap.icon_location);
    }

    /**
     * 设置图标下的title
     * @param context
     * @return
     */
    @Override
    public CharSequence obtainPluginTitle(Context context) {
        //R.string.add_contacts 通讯录
        return context.getString(R.string.location);
    }

    /**
     * click 事件
     * @param view
     */
    @Override
    public void onPluginClick(View view) {
        Log.e("hongbao","location onclick");
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_PICK);
//            intent.setData(ContactsContract.Contacts.CONTENT_URI);
//            startActivityForResult(intent, REQUEST_CONTACT);
        startActivityForResult(new Intent(getContext(), LocationActivity.class),REQUEST_LOCATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK)
            return;

        BDLocation location =  data.getParcelableExtra("location");
        Log.e("Location ","activity result "+location.getLocationDescribe());
        if (location!= null) {
            mUploadHandler.post(new MyRunnable(data.getData()));

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    class MyRunnable implements Runnable {

        Uri mUri;

        public MyRunnable(Uri uri) {
            mUri = uri;
        }

        @Override
        public void run() {
//            String[] contact = getPhoneContacts(mUri);
//
            String showMessage = "message content hahah ";
            final TextMessage content = TextMessage.obtain(showMessage);
            Message message = new Message();
            message.setContent(content);

            if (RongIM.getInstance().getRongIMClient() != null) {
                /**
                 * 发送消息。
                 *
                 * @param type        会话类型。
                 * @param targetId    目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
                 *
                 * @param content     消息内容。
                 * @param pushContent push 时提示内容，为空时提示文本内容。
                 * @param callback    发送消息的回调。
                 * @return
                 */
                RongIM.getInstance().getRongIMClient().sendMessage(message,
                        "hhah", "pushdata", new RongIMClient.SendImageMessageCallback() {
                            @Override
                            public void onAttached(Message message) {
                                Log.e("provider", "on Attaced");
                            }

                            @Override
                            public void onError(Message message, RongIMClient.ErrorCode
                                    errorCode) {
                                Log.e("provider", "on error");
                            }

                            @Override
                            public void onSuccess(Message message) {
                                Log.e("provider", "on success");
                            }

                            @Override
                            public void onProgress(Message message, int i) {

                            }
                        });
            }
        }
    }

    private String[] getPhoneContacts(Uri uri) {

        String[] contact = new String[2];
        ContentResolver cr = getContext().getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);

            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);

            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        }
        return contact;
    }
}
