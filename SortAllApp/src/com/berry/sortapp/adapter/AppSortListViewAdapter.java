package com.berry.sortapp.adapter;

import java.util.List;

import com.berry.sortapp.R;
import com.berry.sortapp.bean.AppInfo;
import com.berry.sortapp.bean.SortModel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class AppSortListViewAdapter extends BaseAdapter {

	private Context context;
	private List<SortModel> sortModels;

	public AppSortListViewAdapter(Context context, List<SortModel> sortModels) {
		this.context = context;
		this.sortModels = sortModels;
	}

	@Override
	public int getCount() {
		return sortModels.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sortModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.app_sort_listview_item, null);
            holder = new Holder();
            holder.letterTv = (TextView) convertView
                    .findViewById(R.id.letter_tv);
            holder.appGv = (GridView) convertView.findViewById(R.id.app_gv);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        
        SortModel sortModel = sortModels.get(position);
        holder.letterTv.setText(sortModel.getSortLetters());
        
        AppGridViewAdapter adapter = new AppGridViewAdapter(context, sortModel.getApps());
        holder.appGv.setAdapter(adapter);
        holder.appGv.setOnItemClickListener(new OnItemClickListener() {

          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            if (view!=null) {
              AppInfo info = (AppInfo) view.getTag(R.id.gridview_item_app);
              Intent intent = new Intent();
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              intent.setComponent(info.getCn());
              context.startActivity(intent);
            }
          }
          
        });
        // 根据position获取分类的首字母的Char ascii值
//      int section = getSectionForPosition(position);
//
//      // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
//      if (position == getPositionForSection(section)) {
//          holder.letterTv.setVisibility(View.VISIBLE);
//          holder.letterTv.setText(sortModel.getSortLetters());
//      } else {
//          holder.letterTv.setVisibility(View.GONE);
//      }

        //viewHolder.tvTitle.setText(this.list.get(position).getName());
        return convertView;
    }

    class Holder {
        TextView letterTv;
        GridView appGv;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = sortModels.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return sortModels.get(position).getSortLetters().charAt(0);
    }

    public void updateListView(List<SortModel> filterDateList) {
        // TODO Auto-generated method stub
        this.sortModels = filterDateList;
        notifyDataSetChanged();
    }

}
