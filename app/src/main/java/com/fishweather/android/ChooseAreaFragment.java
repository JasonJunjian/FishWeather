package com.fishweather.android;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fishweather.android.db.City;
import com.fishweather.android.db.County;
import com.fishweather.android.db.Province;
import com.fishweather.android.util.HttpUtil;
import com.fishweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yujj on 2017/8/18.
 */

public class ChooseAreaFragment extends Fragment {
    private static  final int LEVEL_PROVINCE = 0;
    private static  final int LEVEL_CITY = 1;
    private static  final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ImageView backButton;
    private ListView areaListView;

    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<String>();

    /*省份列表*/
    private List<Province> provinceList = new ArrayList<Province>();
    /*市列表*/
    private List<City> cityList = new ArrayList<City>();
    /*县列表*/
    private List<County> countyList = new ArrayList<County>();

    /*选中的省份*/
    private Province selectedProvince = new Province();
    /*选中的城市*/
    private City selectedCity = new City();
    /*当前级别*/
    private int currentLevel ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView)  view.findViewById(R.id.title_text);
        backButton = (ImageView) view.findViewById(R.id.back_button);
        areaListView = (ListView) view.findViewById(R.id.area_listview);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        areaListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        areaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCity();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounty();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel == LEVEL_COUNTY){
                    queryCity();
                }else if(currentLevel == LEVEL_CITY){
                    queryProvince();
                }
            }
        });
        queryProvince();
    }


    /*查询绑定省份数据*/
    private void queryProvince(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size() > 0){
            dataList.clear();
            for (Province province: provinceList ) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            areaListView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else{
            queryFromServer("http://guolin.tech/api/china",LEVEL_PROVINCE);
        }
    }
    /*查询绑定城市数据*/
    private void queryCity(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("ProvinceCode= ? ",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size() > 0){
            dataList.clear();
            for (City city: cityList ) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            areaListView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            queryFromServer("http://guolin.tech/api/china/"+selectedProvince.getProvinceCode(),LEVEL_CITY);
        }
    }
    /*查询绑定县数据*/
    private void queryCounty(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("CtiyId = ? ",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size() > 0){
            dataList.clear();
            for (County county: countyList ) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            areaListView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else{
            queryFromServer("http://guolin.tech/api/china/"+selectedProvince.getProvinceCode()+"/"+selectedCity.getCityCode(),LEVEL_COUNTY);
        }
    }
    private void queryFromServer(String url ,final int level){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                boolean result = false;
                switch (level) {
                    case LEVEL_PROVINCE:
                        result = Utility.convertProvinceResponse(body);
                        break;
                    case LEVEL_CITY:
                        result = Utility.convertCityResponse(body,selectedProvince.getId());
                        break;
                    case LEVEL_COUNTY:
                        result = Utility.convertCountyResponse(body,selectedCity.getId());
                        break;
                }
                Log.d("chooseFragment", "convert result: "+result+" level " +level);
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (level) {
                                case LEVEL_PROVINCE:
                                   queryProvince();
                                    break;
                                case LEVEL_CITY:
                                    queryCity();
                                    break;
                                case LEVEL_COUNTY:
                                    queryCounty();
                                    break;
                            }
                            closeProgressDialog();
                        }
                    });
                }
            }
        });


    }

    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    public boolean back(){
        if(currentLevel == LEVEL_COUNTY){
            queryCity();
            return false;
        }else if(currentLevel == LEVEL_CITY){
            queryProvince();
            return false;
        }else{
            return true;
        }
    }
}
