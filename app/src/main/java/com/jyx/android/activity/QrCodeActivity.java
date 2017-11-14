package com.jyx.android.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;

import java.util.Hashtable;

import butterknife.Bind;

/**
 * 二维码生成
 * Created by Dell on 2016/4/16.
 */
public class QrCodeActivity extends BaseActivity {
    @Bind(R.id.iv_user_avatar)
    SimpleDraweeView mAvatar;
    @Bind(R.id.tv_nick_name)
    TextView mNikeName;
    @Bind(R.id.qe_code)
    ImageView imageView;
    private int QR_WIDTH = 1000, QR_HEIGHT = 700;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qr_code;
    }
    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_me_qe_code;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }


    @Override
    protected boolean hasBackButton() {
        return true;
    }

    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("qrcode");
        createQRImage(bundle.getString("userid"));
        mAvatar.setImageURI(Uri.parse(bundle.getString("img")));
        mNikeName.setText(bundle.getString("username"));
    }

    //Edited by mythou
//http://www.cnblogs.com/mythou/
    //要转换的地址或字符串,可以是中文
    public void createQRImage(String url)
    {
        try
        {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++)
            {
                for (int x = 0; x < QR_WIDTH; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            imageView.setImageBitmap(bitmap);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }
}
