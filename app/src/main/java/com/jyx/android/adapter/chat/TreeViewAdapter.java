package com.jyx.android.adapter.chat;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jyx.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;

/**
 * 树形列表
 * Author : yiyi
 * Date : 2015-11-01
 */
public class TreeViewAdapter {
	public TreeNode mRoot = null;
	public TreeNode mSelectedNode = null;
	private TreeListAdapter mTreeListAdapter;
	
	private Context mContext;

    private LinearLayout mContainTreeLayout;
    
    private ListView mTreeListView;

	public void NotifyDataChange()
	{
		mTreeListAdapter.InitNodeListToShow();
	}

    public void InitTreeViewData(TreeNode tn)
    {
    	TreeNode t = null;
    	List<TreeNode> lt = null;
    
    	if(tn==null)
    	{
    		return;
    	}
    	
    	lt = tn.getChildren();
    	if(lt==null)
    	{
    		tn = null;
    		return;
    	}
    	for(int i=0; i<lt.size(); i++)
    	{
    		t = lt.get(i);
    		InitTreeViewData(t);
    	}
    	tn.clearChildren();
    	tn = null;    	    	
    }
    
    public void setTreeListViewPosition(String id)
    {
    	mTreeListAdapter.setSelectorNodeByPosition(mTreeListAdapter.getTreeListPosition(id));
        mTreeListView.setSelection(mTreeListAdapter.getTreeListPosition(id));        
    }
    
    public void setTreeExpand(String id)
    {
    	TreeNode t,n;
    	
    	if((t = FindNodeById(mRoot, id))==null)
    	{
    		return;
    	}
    	
    	n = t;
    	while(n!=null)
    	{
    		if(n!=t)
    		{
    			n.setExpanded(true);
    		}
    		n = n.getParent();    		
    	}
    }

	//扩展所有节点
	public void setAllNodeExpand(TreeNode r)
	{
		TreeNode rt;
		List<TreeNode> tnl;

		if(r!=null)
		{
			r.setExpanded(true);

			tnl = r.getChildren();

			if(tnl==null)
			{
				return;
			}

			if(tnl.size()>0)
			{
				for(int i=0; i<tnl.size(); i++)
				{
					rt = tnl.get(i);
					setAllNodeExpand(rt);
				}
			}
		}

		mTreeListAdapter.InitNodeListToShow();
	}

	public TreeNode FindNodeById(TreeNode r, String id)
	{
		TreeNode t,rt;
		List<TreeNode> tnl;
		
		if(r!=null)
		{
			if(r.getId().equals(id))
			{
				return r;
			}
			
			tnl = r.getChildren();
			
			if(tnl==null)
			{
				return null;
			}
			
			if(tnl.size()>0)
			{
				for(int i=0; i<tnl.size(); i++)
				{
					t = tnl.get(i);
					if((rt = FindNodeById(t, id))!=null)
					{
						return rt;
					}					
				}
			}
		}
		
		return null; 
	}
	
	public boolean addATreeNode(String id, String parentid, String name, String imageurl, boolean attention, boolean isgroup)
	{
		TreeNode ft;
		TreeNode node;
		Map<String, Object> mp;

		//根节点的parentid必须为空，且不允许有两个根节点
		if (parentid.equals("") && mRoot!=null)
		{
			return false;
		}

		if (parentid.equals("") && mRoot==null)
		{
			node = new TreeNode(null, id, parentid, name, -1, imageurl, attention, isgroup);

			mRoot = node;
			mRoot.setRoot(true);
			mRoot.setExpanded(true);

			return true;
		}

		if((ft=FindNodeById(mRoot, parentid))==null)
		{
			return false;
		}
		
		if(FindNodeById(mRoot, id)==null)
		{
			node = new TreeNode(ft, id, parentid, name, -1, imageurl, attention, isgroup);
			ft.addChildNode(node);

			return true;
		}
		
		return false;
	}
	
	public void InitTreeAdapter()
	{
		mTreeListAdapter = new TreeListAdapter(mContext, mRoot, true);

        mTreeListAdapter.setOnTreeCallBack(new TreeListAdapter.OnTreeCallBack() {
            @Override
            public boolean onSelectNode(TreeNode node)
            {
                if (node != null)
                {
                    mSelectedNode = node;
					Log.e("TreeViewAdapter","tree Node select"+node.getId()+ node.getIsGroup());
					if(node.getIsGroup()){
						RongIM.getInstance().startGroupChat(mContext,node.getId(),node
								.getDescription());
					}else{
						RongIM.getInstance().startPrivateChat(mContext,node.getId(),
								node.getDescription());
					}
				}
				return true;
            }

            @Override
            public void onNodeExpand(TreeNode node)
            {                
            }

            @Override
            public void onNodeExpandOrNot(TreeNode node)
            {                
            }

            @Override
            public ArrayList<String> getParentIDList(String id)
            {
                return new ArrayList<String>();
            }

        });
	}
	
	public void LoadTreeView(Context context, LinearLayout l)
    {
        mContext = context;
        mContainTreeLayout = l;
        mTreeListView = (ListView) mContainTreeLayout
                .findViewById(R.id.lv_treelay_tree);

        InitTreeAdapter();
        
        mTreeListView.setAdapter(mTreeListAdapter);
    }

	public void setShowRoot(boolean show)
	{
		mTreeListAdapter.setShowRoot(show);
	}


}
