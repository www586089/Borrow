package com.jyx.android.adapter.chat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.activity.chat.NewSubGroupActivity;
import com.jyx.android.activity.me.UserInfoActivity;
import com.jyx.android.adapter.contact.ContactListAdapter;
import com.jyx.android.fragment.contact.FriendFragment;
import com.jyx.android.fragment.me.MeFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 树形列表
 * Author : yiyi
 * Date : 2015-11-01
 */
public class TreeListAdapter extends BaseAdapter
{

    private Context mContext;

    private int mJumpToPosition;

    private TreeNode mRoot;

    private List<TreeNode> mAllNodeList = new ArrayList<TreeNode>();

    private List<TreeNode> mShowNodeList = new ArrayList<TreeNode>();

    private List<String> mShowNodeIDList = new LinkedList<String>();

    private TreeNode mSelectedNode;

    private OnTreeCallBack mOnTreeCallBack;

    private LayoutInflater mInflater;

    private int mExpandOnIcon = R.drawable.tree_expand_on_nr;

    private int mExpandOffIcon = R.drawable.tree_expand_off_nr;

    private boolean mShowRoot = true;

    public TreeListAdapter(Context con, TreeNode root, boolean bShowRoot)
    {
        mContext = con;
        mInflater = (LayoutInflater) con
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = root;
        mShowRoot = bShowRoot;
        establishNodeList(mRoot);
        setNodeListToShow();
    }

    public boolean getShowRoot()
    {
        return mShowRoot;
    }

    public List<TreeNode> getAllLoadNodeList()
    {
        return mAllNodeList;
    }

    public void setShowRoot(boolean show)
    {
        if (mShowRoot != show)
        {
            mShowRoot = show;
            setNodeListToShow();
            notifyDataSetChanged();
        }
    }

    public void InitNodeListToShow()
    {
        setNodeListToShow();
        notifyDataSetChanged();
    }

    public void setOnTreeCallBack(OnTreeCallBack onTreeCallBack)
    {
        mOnTreeCallBack = onTreeCallBack;
    }

    //添加节点及节点下的所有子节点数据
    public void establishNodeList(TreeNode node)
    {
        if (node == null)
            return;
        mAllNodeList.add(node);
        if (node.isLeaf())
            return;
        List<TreeNode> children = node.getChildren();
        int size = children.size();
        for (int i = 0; i < size; i++)
        {
            establishNodeList(children.get(i));
        }
    }

    //初始化显示列表的数据
    private void setNodeListToShow()
    {
        this.mShowNodeList.clear();
        mShowNodeIDList.clear();
        establishNodeListToShow(this.mRoot);
    }

    private void establishNodeListToShow(TreeNode node)
    {
        //如果掩藏根节点，则需要把根节点的Expand值设置为true，否则无法获取根节点下的子节点数据
        boolean rf = false;

        if (mShowRoot || mRoot != node)
        {
            mShowNodeList.add(node);
            mShowNodeIDList.add(node.getId());
        }

        if (!mShowRoot && mRoot == node)
        {
            rf = true;
        }
        else
        {
            rf = node.getExpanded();
        }

        if (node != null && rf && !node.isLeaf()
                && node.getChildren() != null)
        {
            List<TreeNode> children = node.getChildren();
            int size = children.size();
            for (int i = 0; i < size; i++)
            {
                establishNodeListToShow(children.get(i));
            }
        }
    }

    private void changeNodeExpandOrFold(TreeNode node)
    {
        boolean flag = node.getExpanded();
        node.setExpanded(!flag);
    }

    public TreeNode getSelectorNode()
    {
        return mSelectedNode;
    }

    public int getCount()
    {
        return mShowNodeList.size();
    }

    public Object getItem(int arg0)
    {
        return mShowNodeList.get(arg0);
    }

    public long getItemId(int arg0)
    {
        return arg0;
    }

    public View getView(final int position, View view, ViewGroup parent)
    {
        Holder holder = null;
        String imageUrl = "";
        boolean hasattention = false;
        boolean isgroup = false;

        if (view != null)
        {
            holder = (Holder) view.getTag();
        }
        else
        {
            holder = new Holder();
            view = this.mInflater.inflate(R.layout.tree_item, null);
            holder.layBlank = (LinearLayout) view.findViewById(R.id.lv_treeitem_blank);
            //holder.nodeIcon = (SimpleDraweeView) view.findViewById(R.id.iv_treeitem_user_img);
            holder.expandOrNot = (ImageView) view
                    .findViewById(R.id.iv_treeitem_expanded);
            holder.layTextContent = (LinearLayout) view
                    .findViewById(R.id.lv_treeview_tv_content);
            holder.userGIcon = (SimpleDraweeView)view.findViewById(R.id.iv_treeitem_user_img);
            holder.description = (TextView) view
                    .findViewById(R.id.tv_treeitem_description);
            holder.btnAttention = (Button)view.findViewById(R.id.btn_treeitem_attention);
            view.setTag(holder);
        }

        TreeNode node = this.mShowNodeList.get(position);
        if (node == null)
            return null;

        imageUrl = node.getmImageURL();
        Uri imageUri;
        if (imageUrl != null) {
            if (!imageUrl.equals("")) {
                imageUri = Uri.parse(imageUrl);
                holder.userGIcon.setImageURI(imageUri);
            }
            else
            {
                if (node.getIsGroup())
                {
                    imageUri = Uri.parse("res://com.jyx.android/" + R.mipmap.tree_group_node);
                    holder.userGIcon.setImageURI(imageUri);
                }
                else
                {
                    imageUri = Uri.parse("res://com.jyx.android/" + R.mipmap.tree_user_node);
                    holder.userGIcon.setImageURI(imageUri);
                }
            }
        }
        else
        {
            if (node.getIsGroup())
            {
                Uri uri = Uri.parse("res://com.jyx.android/" + R.drawable.ic_launcher);
                holder.userGIcon.setImageURI(uri);
            }
            else
            {
                holder.userGIcon.setImageResource(R.mipmap.tree_user_node);
            }
        }

        View.OnClickListener itemlistener= new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TreeNode node = mShowNodeList.get(position);

                if (v.getId() == R.id.btn_treeitem_attention)
                {
                    if(node.getIsGroup()){
                        Intent intent = new Intent(mContext, NewSubGroupActivity.class);
                        intent.putExtra(NewSubGroupActivity.KEY_GROUP_LOGO, node.getmImageURL());
                        intent.putExtra(NewSubGroupActivity.KEY_GROUP_ID, node.getId());
                        intent.putExtra(NewSubGroupActivity.KEY_GROUP_NAME, node.getDescription());
                        mContext.startActivity(intent);
                    }else {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FriendFragment.KEY_PAGE_TYPE, node.getAttention() ? ContactListAdapter.PAGE_FOLLOW : ContactListAdapter.PAGE_FAN);
                        bundle.putString(MeFragment.KEY_USER_ID, node.getId());
                        Intent intent = new Intent(mContext, UserInfoActivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }

                }
                else
                {
                    onClickNodeExpandOrFoldIcon(node);
                }
            }
        };

        view.setOnClickListener(itemlistener);
        holder.btnAttention.setOnClickListener(itemlistener);

        if (!node.isLeaf())
        {
            holder.expandOrNot
                    .setImageResource(node.getExpanded() ? mExpandOnIcon
                            : mExpandOffIcon);
            holder.expandOrNot.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.expandOrNot.setVisibility(View.GONE);
        }
        holder.expandOrNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TreeNode node = mShowNodeList.get(position);
                onClickNodeExpandOrFoldIcon(node);
            }
        });

        // holder.nodeIcon.setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View v)
        // {
        // // TODO Auto-generated method stub
        // TreeNode node = mShowNodeList.get(position);
        // onClickNodeIcon(node);
        // }
        // });

        hasattention = node.getAttention();
        isgroup = node.getIsGroup();
        if(isgroup){
            holder.btnAttention.setText("建子群");
        }else {
            if (hasattention)
            {
                holder.btnAttention.setText("已关注");
            }
            else
            {
                holder.btnAttention.setText("未关注");
            }
        }

        if (mShowRoot && node.isRoot())
        {
            holder.btnAttention.setVisibility(View.GONE);
        }
        else
        {
//            if (isgroup)
//                holder.btnAttention.setVisibility(View.GONE);
//            else
                holder.btnAttention.setVisibility(View.VISIBLE);
        }

        holder.description.setText(node.getDescription());
        
        holder.layTextContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                TreeNode node = mShowNodeList.get(position);
                doOnClick(node);
            }
        });

        //计算并画出虚线的横向偏移
        LayoutParams lp = (LayoutParams) holder.layBlank.getLayoutParams();

        int n = node.getLevel();
        int nf = n + (node.isLeaf() ? 1 : 0);
        Resources res = mContext.getResources();
        int nw = res.getDimensionPixelSize(R.dimen.treenode_icon_width);
        if (!mShowRoot)
        {
            if (nf > 0)
            {
                nf--;
            }
            n--;
        }
        nw = nf * nw + res.getDimensionPixelSize(R.dimen.treenode_offset);
        lp.width = nw;
        holder.layBlank.setLayoutParams(lp);
        Bitmap bitmap = createBitmap(node, n, nw, mContext.getResources()
                .getDimensionPixelSize(R.dimen.treenode_height));
        if (bitmap != null)
        {
            holder.layBlank.setBackgroundDrawable(new BitmapDrawable(bitmap));            
        }
        else
        {
            holder.layBlank.setBackgroundColor(Color.TRANSPARENT);
        }
        if (node == mSelectedNode)
        {
            view.findViewById(R.id.lv_treeview_tv_content).setBackgroundColor(
                    mContext.getResources().getColor(R.color.tree_node_bg));
        }
        else
        {
            view.findViewById(R.id.lv_treeview_tv_content).setBackgroundResource(
                    R.drawable.treelist_selector_bg);
        }
        return view;
    }

    public Bitmap createBitmap(TreeNode node, int lev, int w, int h)
    {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        if (lev <= 0 || w <= 0 || h <= 0)
        {
            return null;
        }
        Resources res = mContext.getResources();
        int offW = res.getDimensionPixelSize(R.dimen.treenode_offset);
        int iconW = res.getDimensionPixelSize(R.dimen.treenode_icon_width);
        int color = res.getColor(R.color.tree_black);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        PathEffect effect = new DashPathEffect(new float[] { 2, 2 }, 1);
        paint.setPathEffect(effect);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        paint.setAntiAlias(true);
        int start = offW + iconW / 2;

        int y = h;
        int x = start;
        for (int i = 0; i < lev; ++i)
        {
            y = h;
            x = start + i * iconW;
            if (i == lev - 1)
            {
                y = isLastChildNode(node, 1) ? (h / 2) : h;
                canvas.drawLine(x, 0, x, y, paint);
            }
            else
            {
                if (!isLastChildNode(node, lev - i))
                {
                    canvas.drawLine(x, 0, x, y, paint);
                }
            }
        }
        int line = (node.isLeaf() && node.getIcon() == -1) ? res
                .getDimensionPixelSize(R.dimen.treenode_line_offset) : 0;
        canvas.drawLine(x, h / 2, w - line, h / 2, paint);
        return bitmap;
    }

    private boolean isLastChildNode(TreeNode node, int lev)
    {
        boolean last = true;
        TreeNode child = node;
        TreeNode parent = child;
        int i = 0;
        for (i = 0; i < lev; ++i)
        {
            parent = child.getParent();
            if (parent == null)
            {
                break;
            }
            if (i == lev - 1)
            {
                List<TreeNode> childList = parent.getChildren();
                if (childList != null && childList.size() > 0)
                {
                    last = (childList.get(childList.size() - 1) == child);
                }
            }
            else
            {
                child = parent;
            }
        }

        return last;
    }

    public void doOnClick(TreeNode node)
    {
        setSelectorNode(node);
        if (!node.isLeaf())
        {
            setNodeExpandOrNot(node);
        }
    }

    public void imitateDoOnClick(TreeNode node)
    {

        if (!node.getExpanded())
        {
            if (mOnTreeCallBack != null)
            {
                mOnTreeCallBack.onNodeExpand(node);
            }
            setNodeListToShow();
            notifyDataSetChanged();
        }
    }

    public void setSelectorNode(TreeNode node)
    {
        if (mSelectedNode != node)
        {
            mSelectedNode = node;
            // notifyDataSetChanged();

            if (mOnTreeCallBack != null)
            {
                if (!mOnTreeCallBack.onSelectNode(node))
                {
                    return;
                }

                if (!mShowNodeIDList.contains(node.getId()))
                {
                    establishNodeListToShow(node);
                }
                notifyDataSetChanged();
            }
        }
    }

    public void setSelectorNodeByPosition(int position)
    {
    	if(mShowNodeList==null)
    	{
    		return;
    	}
    	if(position<0 || position>=mShowNodeList.size())
    	{
    		return;
    	}    	    	
    	
        if (mSelectedNode != mShowNodeList.get(position))
        {
            mSelectedNode = mShowNodeList.get(position);
            // notifyDataSetChanged();

            if (mOnTreeCallBack != null)
            {
                if (!mOnTreeCallBack.onSelectNode(mShowNodeList.get(position)))
                {
                    return;
                }
                
                if (!mShowNodeIDList.contains((String) mShowNodeList.get(position)
                        .getId()))
                {
                    establishNodeListToShow(mShowNodeList.get(position));
                }
                notifyDataSetChanged();
            }
        }
    }
    
    public boolean calulateListViewPosition(String lastid)
    {
    	int nodeSize = mShowNodeList.size();
        for (int i = 0; i < nodeSize; i++)
        {
            String nodeid = mShowNodeList.get(i).getId();

            if (nodeid.equals(lastid))
            {
                mJumpToPosition = i;

                return true;
            }
        }
           
        return false;
    }

    public int getTreeListPosition(String id)

    {
        if (calulateListViewPosition(id))
            return mJumpToPosition;
        else
            return -1;
    }

    public void setNodeExpandOrNot(TreeNode node)
    {
        if (!node.isLeaf())
        {
            changeNodeExpandOrFold(node);
            if (mOnTreeCallBack != null)
            {
                mOnTreeCallBack.onNodeExpandOrNot(node);
            }
            setNodeListToShow();
            notifyDataSetChanged();
        }
    }

    public int setNodeShow(TreeNode node)
    {
        TreeNode parent = node.getParent();
        while (parent != null)
        {
            parent.setExpanded(true);
            parent = parent.getParent();
        }
        setNodeListToShow();
        int i = 0;
        int size =  mShowNodeList.size();
        for (i = 0; i < size; ++i)
        {
            if (node == mShowNodeList.get(i))
            {
                break;
            }
        }
        if (i >= mShowNodeList.size())
        {
            i = -1;
        }
        notifyDataSetChanged();

        return i;
    }

    public boolean onClickNodeIcon(TreeNode node)
    {

        return onClickNodeExpandOrFoldIcon(node);
    }

    public boolean onClickNodeExpandOrFoldIcon(TreeNode node)
    {
        if (!node.isLeaf())
        {
            this.changeNodeExpandOrFold(node);
            this.setNodeListToShow();
            this.notifyDataSetChanged();
        }
        return true;
    }

    public class Holder
    {
        LinearLayout layBlank;

        ImageView nodeIcon;

        ImageView expandOrNot;

        SimpleDraweeView userGIcon;

        TextView description;

        Button btnAttention;

        LinearLayout layTextContent;

    }

    public interface OnTreeCallBack
    {
        public boolean onSelectNode(TreeNode node);

        //点击节点进行扩展或收缩
        public void onNodeExpandOrNot(TreeNode node);

        //模拟扩展节点
        public void onNodeExpand(TreeNode node);

        public ArrayList<String> getParentIDList(String id);
    }
}
