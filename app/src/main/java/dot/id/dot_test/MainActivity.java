package dot.id.dot_test;

import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dot.id.dot_test.Model.CityResponse;
import dot.id.dot_test.Model.CityResult;
import dot.id.dot_test.Model.ProvinceResponse;
import dot.id.dot_test.Model.ProvinceResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ClickListener {
    TextView tvProvinsiAsal, tvKotaAsal, tvProvinsiTujuan, tvKotaTujuan, tvUbahPAsal, tvUbahKAsal, tvUbahPTujuan, tvUbahKTujuan;
    TextView tvKotaAsalOption, tvKotaTujuanOption;
    RelativeLayout rlPAsal, rlKAsal, rlPTujuan, rlKTujuan;
    EditText etBerat;
    RadioButton rbJne, rbPos, rbTiki;
    Button bCekTarif;
    BottomSheetDialog bsdDialog;
    SearchView searchView;
    RadioGroup radioGroup;
    List<ProvinceResult> provinceResults = new ArrayList<>();
    List<ProvinceResult> provinceResultsCopy = new ArrayList<>();
    List<CityResult> cityResults = new ArrayList<>();
    List<CityResult> cityResultsCopy = new ArrayList<>();
    CityAdapter cityAdapter = new CityAdapter(cityResultsCopy);
    ProvinceAdapter provinceAdapter = new ProvinceAdapter(provinceResultsCopy);
    int temporaryValue = 0;
    String idProvinceAsal, idProvinceTujuan, idKotaAsal, idKotaTujuan;
    String courier;
    String token = "0df6d5bf733214af6c6644eb8717c92c";
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getProvince(token);
        rlPAsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temporaryValue =1;
                modalBottomSheet(temporaryValue);
            }
        });

        tvUbahPAsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temporaryValue =1;
                modalBottomSheet(temporaryValue);
            }
        });

        rlPTujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temporaryValue = 3;
                modalBottomSheet(temporaryValue);
            }
        });

        tvUbahPTujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temporaryValue = 3;
                modalBottomSheet(temporaryValue);
            }
        });

        rlKAsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temporaryValue = 2;
                modalBottomSheet(temporaryValue);
            }
        });

        tvUbahKAsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temporaryValue = 2;
                modalBottomSheet(temporaryValue);
            }
        });

        rlKTujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temporaryValue = 4;
                modalBottomSheet(temporaryValue);
            }
        });

        tvUbahKTujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temporaryValue = 4;
                modalBottomSheet(temporaryValue);
            }
        });

        bCekTarif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroup.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(MainActivity.this, "Silahkan pilih salah satu kurir", Toast.LENGTH_LONG).show();
                }
                else
                {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(selectedId);
                    courier = radioButton.getText().toString();
                    String weight = etBerat.getText().toString();
                    if (weight.matches("")){
                        Toast.makeText(MainActivity.this, "Silahkan masukkan berat barang", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, CostActivity.class);
                        intent.putExtra(ConstantPref.TOKEN, token);
                        intent.putExtra(ConstantPref.ORIGIN, idKotaAsal);
                        intent.putExtra(ConstantPref.DESTINATION, idKotaTujuan);
                        intent.putExtra(ConstantPref.WEIGHT, weight);
                        intent.putExtra(ConstantPref.COURIER, courier);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void initView() {
        radioGroup = findViewById(R.id.radioGroup);
        tvKotaAsalOption = findViewById(R.id.tv_kota_asal);
        tvKotaTujuanOption = findViewById(R.id.tv_kota_tujuan);
        tvProvinsiAsal = findViewById(R.id.tv_p_asal);
        tvKotaAsal = findViewById(R.id.tv_k_asal);
        tvProvinsiTujuan = findViewById(R.id.tv_p_tujuan);
        tvKotaTujuan = findViewById(R.id.tv_k_tujuan);
        tvUbahPAsal = findViewById(R.id.tv_ubah_p_asal);
        tvUbahPTujuan = findViewById(R.id.tv_ubah_p_tujuan);
        tvUbahKAsal = findViewById(R.id.tv_ubah_k_asal);
        tvUbahKTujuan = findViewById(R.id.tv_ubah_k_tujuan);
        rlPAsal = findViewById(R.id.rl_provinsi_asal);
        rlPTujuan = findViewById(R.id.rl_provinsi_tujuan);
        rlKAsal = findViewById(R.id.rl_kota_asal);
        rlKTujuan = findViewById(R.id.rl_kota_tujuan);
        etBerat = findViewById(R.id.et_berat);
        rbJne = findViewById(R.id.rb_jne);
        rbPos = findViewById(R.id.rb_pos);
        rbTiki = findViewById(R.id.rb_tiki);
        bCekTarif = findViewById(R.id.b_tarif);
        rlKAsal.setEnabled(false);
        tvKotaAsalOption.setTextColor(getResources().getColor(R.color.colorGrey));
        rlKTujuan.setEnabled(false);
        tvKotaTujuanOption.setTextColor(getResources().getColor(R.color.colorGrey));
    }

    public void getProvince(String token){
        BaseApps.getServices().getProvince(token).enqueue(new Callback<ProvinceResponse>() {
            @Override
            public void onResponse(Call<ProvinceResponse> call, Response<ProvinceResponse> response) {
                if (response.isSuccessful()){
                    provinceResults.addAll(response.body().getRajaongkir().getResults());
                    provinceResultsCopy.addAll(response.body().getRajaongkir().getResults());
                    provinceAdapter.setClickListener(MainActivity.this);
                    provinceAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProvinceResponse> call, Throwable t) {

            }
        });
    }

    public void getCity(String token, String id){
        BaseApps.getServices().getCity(token, id).enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                if (response.isSuccessful()){
                    cityResults.addAll(response.body().getRajaongkir().getResults());
                    cityResultsCopy.addAll(response.body().getRajaongkir().getResults());
                    cityAdapter.notifyDataSetChanged();
                    cityAdapter.setClickListener(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, "Periksa Koneksi Internet Anda", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {

            }
        });
    }

    private void modalBottomSheet(int code) {
        View modalbottomsheet = getLayoutInflater().inflate(R.layout.bottomsheet_layout, null);
        bsdDialog = new BottomSheetDialog(this);
        bsdDialog.setContentView(modalbottomsheet);
        bsdDialog.show();
        bsdDialog.setCanceledOnTouchOutside(false);
        bsdDialog.setCancelable(false);

        searchView = modalbottomsheet.findViewById(R.id.sv_pilihan);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        TextView batal = modalbottomsheet.findViewById(R.id.tv_batal);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsdDialog.hide();
            }
        });

        //Add list into Bottom Sheet
        RecyclerView rvPilihan = modalbottomsheet.findViewById(R.id.rv_pilihan);
        rvPilihan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (code == 1 || code == 3){
            searchProvince();
            rvPilihan.setAdapter(provinceAdapter);
        } else if (code == 2 || code == 4){
            searchCity();
            rvPilihan.setAdapter(cityAdapter);
        }
    }

    public void searchProvince(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("Search... "+newText);
                provinceResultsCopy.clear();
                for (int i = 0; i<provinceResults.size(); i++){
                    if (provinceResults.get(i).getProvince().toLowerCase().contains(newText.toLowerCase())){
                        provinceResultsCopy.add(provinceResults.get(i));
                        provinceAdapter.notifyDataSetChanged();
                        provinceAdapter.setClickListener(MainActivity.this);
                    }
                }
                return false;
            }
        });
    }

    public void searchCity(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("Search... "+newText);
                cityResultsCopy.clear();
                for (int i = 0; i<cityResults.size(); i++){
                    if (cityResults.get(i).getCityName().toLowerCase().contains(newText.toLowerCase())){
                        cityResultsCopy.add(cityResults.get(i));
                        cityAdapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onProvinceItemClick(ProvinceResult provinceResult) {
        bsdDialog.dismiss();
        if (temporaryValue == 1){
            idProvinceAsal = provinceResult.getProvinceId();
            tvProvinsiAsal.setText(provinceResult.getProvince());
            tvProvinsiAsal.setTextSize(18);
            tvUbahPAsal.setVisibility(View.VISIBLE);
            tvUbahPAsal.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvKotaAsalOption.setTextColor(getResources().getColor(R.color.colorPrimary));
            rlPAsal.setVisibility(View.GONE);
            rlKAsal.setEnabled(true);
            cityResultsCopy.clear();
            getCity(token, idProvinceAsal);
        } else {
            idProvinceTujuan = provinceResult.getProvinceId();
            tvProvinsiTujuan.setText(provinceResult.getProvince());
            tvProvinsiTujuan.setTextSize(18);
            tvUbahPTujuan.setVisibility(View.VISIBLE);
            rlPTujuan.setVisibility(View.GONE);
            rlKTujuan.setEnabled(true);
            tvUbahPTujuan.setTextColor(getResources().getColor(R.color.colorPrimary));
            tvKotaTujuanOption.setTextColor(getResources().getColor(R.color.colorPrimary));
            cityResultsCopy.clear();
            getCity(token, idProvinceTujuan);
        }
    }

    @Override
    public void onCityItemClick(CityResult cityResult) {
        bsdDialog.dismiss();
        if (temporaryValue == 2){
            idKotaAsal = cityResult.getCityId();
            tvKotaAsal.setText(cityResult.getCityName());
            tvKotaAsal.setTextSize(18);
            tvUbahKAsal.setVisibility(View.VISIBLE);
            rlKAsal.setVisibility(View.GONE);
            tvUbahKAsal.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            idKotaTujuan = cityResult.getCityId();
            tvKotaTujuan.setText(cityResult.getCityName());
            tvKotaTujuan.setTextSize(18);
            tvUbahKTujuan.setVisibility(View.VISIBLE);
            rlKTujuan.setVisibility(View.GONE);
            tvUbahKTujuan.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}
