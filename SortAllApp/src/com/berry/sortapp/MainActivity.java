package com.berry.sortapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.berry.sortapp.adapter.AppSortListViewAdapter;
import com.berry.sortapp.bean.AppInfo;
import com.berry.sortapp.bean.SortModel;
import com.berry.sortapp.utils.AppCollector;
import com.berry.sortapp.utils.CharacterParser;
import com.berry.sortapp.utils.PinyinComparator;
import com.berry.sortapp.views.SearchEditText;
import com.berry.sortapp.views.SideBar;
import com.berry.sortapp.views.SideBar.OnTouchingLetterChangedListener;
import com.jui.material.widgets.ProgressDialog;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {

    // 搜索框
    private SearchEditText searchEt;
    // 排序listview
    private ListView appSortLv;

    // 中间字母提示
    private TextView dialog;

    // 右边字母
    private SideBar sideBar;

    private SearchEditText mClearEditText;

    // 根据拼音来排列ListView里面的数据类
    private PinyinComparator pinyinComparator;

    private AppSortListViewAdapter appSortListViewAdapter;

    private List<SortModel> sortModels;

    /**
     * 汉字转换成拼音的类
     */
//    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
//        this.initData();
//        this.initView();
        new MyTask().execute("");
    }

    class MyTask extends AsyncTask<String, Void, Void>{
      ProgressDialog dialog;
      @Override
      protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(MainActivity.this, null, "正在加载");
        dialog.show();
      }
      @Override
      protected Void doInBackground(String... params) {
        initData();
        return null;
      }
      @Override
      protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        initView();
        dialog.dismiss();
      }
      
    }
    private void initData() {
        // TODO Auto-generated method stub
        // 实例化汉字转拼音类
//        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        // sortModels = this.filledData(AppManager.getAppList(this));
        sortModels = AppCollector.collect(AppManager.getAppList(this));
        // 根据a-z进行排序源数据
        Collections.sort(sortModels, pinyinComparator);
    }

    private void initView() {
        searchEt = (SearchEditText) findViewById(R.id.search_et);
        appSortLv = (ListView) findViewById(R.id.app_sort_lv);

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        appSortListViewAdapter = new AppSortListViewAdapter(this, sortModels);
        appSortLv.setAdapter(appSortListViewAdapter);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = appSortListViewAdapter.getPositionForSection(s
                        .charAt(0));
                if (position != -1) {
                    appSortLv.setSelection(position);
                }

            }
        });

        mClearEditText = (SearchEditText) findViewById(R.id.search_et);

        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * 
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = sortModels;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : sortModels) {

                

                List<AppInfo> newAppInfos = new ArrayList<AppInfo>();

                // 获取该sortmodel中的app列表
                List<AppInfo> apps = sortModel.getApps();
                for (AppInfo appInfo : apps) {
                    String appName = appInfo.getAppName();
//                    if (appName.indexOf(filterStr.toString()) != -1
//                            || characterParser.getSelling(appName).toLowerCase().startsWith(
//                                    filterStr.toString().toLowerCase())) {
//                        newAppInfos.add(appInfo);
//                    }
                    
                    if (appName.indexOf(filterStr.toString()) != -1
                      || AppCollector.getPinYin(appName).toLowerCase(Locale.getDefault()).startsWith(
                              filterStr.toString().toLowerCase(Locale.getDefault()))) {
                  newAppInfos.add(appInfo);
              }
                }
                if (newAppInfos.size() > 0) {
                    SortModel newSortModel = new SortModel();
                    newSortModel.setSortLetters(sortModel.getSortLetters());
                    newSortModel.setApps(newAppInfos);
                    
                    filterDateList.add(newSortModel);
                }
                /*
                 * if(sortModel.getSortLetters().startsWith(filterStr.toString().
                 * toUpperCase())){ filterDateList.add(sortModel); }
                 */
            }
        }
        appSortListViewAdapter.updateListView(filterDateList);
    }
}
