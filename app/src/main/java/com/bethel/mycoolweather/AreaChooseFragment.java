package com.bethel.mycoolweather;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bethel.mycoolweather.db.City;
import com.bethel.mycoolweather.db.County;
import com.bethel.mycoolweather.db.Province;
import com.bethel.mycoolweather.util.HttpUtil;
import com.bethel.mycoolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AreaChooseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AreaChooseFragment extends Fragment {
    private static final String TAG = "AreaChooseFragment";
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;

    private TextView titleText;
    private Button backBtn;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    private int currentLevel;

    private ProgressDialog mProgressDialog;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AreaChooseFragment.
     */
    public static AreaChooseFragment newInstance() {
        AreaChooseFragment fragment = new AreaChooseFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_area_choose, container, false);
        titleText = view.findViewById(R.id.title_text);
        backBtn = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (currentLevel){
                    case LEVEL_PROVINCE:
                        selectedProvince = provinceList.get(i);
                        queryCities();
                        break;
                    case LEVEL_CITY:
                        selectedCity = cityList.get(i);
                        queryCounties();
                        break;
                    case LEVEL_COUNTY:
                        selectedCounty = countyList.get(i);
                        String weatherId = selectedCounty.getWeatherId();

                        if (getActivity() instanceof AreaChooseActivity) {
                            WeatherActivity.startMe(getActivity(), weatherId);
                            getActivity().finish();
                        } else if (getActivity() instanceof WeatherActivity) {
                            WeatherActivity activity = (WeatherActivity) getActivity();
                            activity.onAreaChanged(weatherId);
                        }
                        break;
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentLevel) {
                    case LEVEL_COUNTY:
                        queryCities();
                        break;
                    case LEVEL_CITY:
                        queryProvinces();
                        break;
                }
            }
        });
        
        queryProvinces();
    }

    private void queryProvinces() {
        titleText.setText(" 中国🇨🇳 ");
        backBtn.setVisibility(View.GONE);

        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province p: provinceList) {
                dataList.add(p.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            fecthFromServer(address, "province");
            Log.i(TAG, "queryProvinces#fecthFromServer");
//            testHttp(address);
        }
    }

    private void testHttp(final String address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkhttpRequestTest(address);
            }
        }).start();
    }

    private void fecthFromServer(String address, final String tag) {
        showProgressDialog();

        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dismissProgressDialog();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext()," 网络加载失败 ", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                switch (tag) {
                    case "province":
                        result = Utility.handleProvinceResponse(responseText);
                        break;
                    case "city":
                        result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                        break;
                    case "county":
                        result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                        break;
                }
                final boolean respResult = result;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        if (respResult) {
                            switch (tag) {
                                case "province":
                                    queryProvinces();
                                    break;
                                case "city":
                                    queryCities();
                                    break;
                                case "county":
                                    queryCounties();
                                    break;
                            }
                        } else {
                            Toast.makeText(getContext()," 加载失败 ", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    private void dismissProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("loading......");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backBtn.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",
                String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County p: countyList) {
                dataList.add(p.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int pcode = selectedProvince.getProvinceCode();
            int ccode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + pcode+"/"+ccode;
            fecthFromServer(address, "county");
        }
    }

    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backBtn.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?",
                String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City p: cityList) {
                dataList.add(p.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int pcode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + pcode;
            fecthFromServer(address, "city");
        }
    }
}
