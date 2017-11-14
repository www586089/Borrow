package com.jyx.android.activity.me.mypersonalcenter.mysettings;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.base.BaseActivity;

import br.com.dina.ui.widget.UITableView;
import butterknife.Bind;

/**
 * Created by Administrator on 2016/3/4.
 */
public class SettingActivity extends BaseActivity {
    @Bind(R.id.setting_tableview)
    UITableView tableView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_setting;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        createList();
        tableView.commit();
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void createList() {
        CustomClickListener listener = new CustomClickListener();
        tableView.setClickListener(listener);
        tableView.addBasicItem("修改密码");
        tableView.addBasicItem("建议反馈");
        tableView.addBasicItem("关于我们");
        tableView.addBasicItem("软件版本：" + getVersion());
    }

    private class CustomClickListener implements UITableView.ClickListener {
        @Override
        public void onClick(int index) {
            switch (index)
            {
                case 0:
                    ActivityHelper.goChangePassword(SettingActivity.this);
                    break;
                case 1:
                    ActivityHelper.goSuggestion(SettingActivity.this);
                    break;
                case 2:
                    ActivityHelper.goAbout(SettingActivity.this);
                    break;
            }
        }
    }
}
