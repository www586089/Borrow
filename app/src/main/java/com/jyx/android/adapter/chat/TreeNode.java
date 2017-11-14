package com.jyx.android.adapter.chat;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形列表的节点定义
 * Author : yiyi
 * Date : 2015-11-01
 */
public class TreeNode
{
    //父节点
    private TreeNode mParent;
    //子节点列表
    private List<TreeNode> mChildList;
    //节点图片
    private String mImageURL = "";
    //节点描述
    private String mName;
    //节点是否已关注
    private boolean mHasAttention = false;
    //节点是否为群节点
    private boolean misGroup = true;
    //节点的扩展状态，false为未扩展，true为已扩展
    private boolean mIsExpanded = false;

    //节点的扩展标志图标
    private int mIcon = -1;

    //节点ID值
    private String mId = "";
    //节点的父节点ID值
    private String mParentid = "";
    //节点是否为根节点
    private boolean misRoot = false;

    public TreeNode(TreeNode parent, String id, String parentid, String description, int icon, String ImageUrl, boolean attention, boolean isgroup)
    {
        this.mParent = parent;
        this.mId = id;
        this.mParentid = parentid;
        this.mName = description;
        this.mIcon = icon;
        this.mImageURL = ImageUrl;
        this.mHasAttention = attention;
        this.misGroup = isgroup;
    }

    public void setIcon(int icon)
    {
        this.mIcon = icon;
    }

    public int getIcon()
    {
        return this.mIcon;
    }

    public String getmImageURL()
    {
        return this.mImageURL;
    }

    public void setmImageURL(String ImageUrl)
    {
        this.mName = ImageUrl;
    }

    public String getDescription()
    {
        return this.mName;
    }

    public void setDescription(String name)
    {
        this.mName = name;
    }

    public boolean isLeaf()
    {
        return mChildList == null || mChildList.size() == 0;
    }

    //根节点层级为0
    public int getLevel()
    {
        return mParent == null ? 0 : mParent.getLevel() + 1;
    }

    public void setExpanded(boolean isExpanded)
    {
        this.mIsExpanded = isExpanded;
    }

    public boolean getExpanded()
    {
        return this.mIsExpanded;
    }

    public void setAttention(boolean attention)
    {
        this.mHasAttention = attention;
    }

    public boolean getAttention()
    {
        return this.mHasAttention;
    }

    public void addChildNode(TreeNode child)
    {
        if (mChildList == null)
        {
            mChildList = new ArrayList<TreeNode>();
        }
        mChildList.add(child);
    }

    public void clearChildren()
    {
        if (!mChildList.equals(null))
        {
            mChildList.clear();
        }
    }

    public void setRoot(boolean isRoot)
    {
        misRoot = isRoot;
    }

    public boolean isRoot()
    {
        return misRoot;
    }

    public final List<TreeNode> getChildren()
    {
        return mChildList;
    }

    public TreeNode getParent()
    {
        return mParent;
    }

    public String getId() { return mId; }

    public String getParentId() { return mParentid; }

    public boolean getIsGroup() { return misGroup; }

    public void setIsGroup(boolean isGroup)
    {
        misGroup = isGroup;
    }
}
