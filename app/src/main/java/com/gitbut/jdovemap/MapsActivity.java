package com.gitbut.jdovemap; //패키지의 이름

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements //접근에 제약이 없는 클래스 MapsActivity을 FragmentActivity에서 extends (상속)(@Override(재정의)할필요 없음)받아서 정의한다
        GoogleMap.OnMyLocationButtonClickListener,  //GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback 상속(implements 는 재정의 필요)받는다
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button;
    private EditText editText;
    public Double latitude;
    public Double longitude;
    public Double Jlatitude = 36.12066870686918;
    public Double Jlongitude = 128.3691367506981;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        editText = (EditText) findViewById(R.id.editText);
        button=(Button)findViewById(R.id.gotitle);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {   //위치권한의 접근 권한이 있을경우 실행되는 매소드.
        if (requestCode == MY_LOCATION_REQUEST_CODE) { //requestCode의 값이 MY_LOCATION_REQUEST_CODE와 같으면
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkCallingPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        checkCallingPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{        //위치정보에 접근 할 수 있는 권한이 없을경우, 위치정보에 접근할수 있는 권한을줌
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                mMap.setMyLocationEnabled(true); //내위치에 접근할수있게 허용
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)  //해당 함수가 마쉬멜로 버전에서 호출 되어야함
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);
        Intent intent = getIntent();
        String slatitude= intent.getStringExtra("위도");
        String slongitude= intent.getStringExtra("경도");
        if (slatitude!=null) {
            Jlatitude = Double.parseDouble(slatitude);
            Jlongitude = Double.parseDouble(slongitude);
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {                //구글지도가 실행될때 위치정보 접근권한이 있는지 확인 후 접근권한이 없으면 권한을 주는 코드
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);  //내 위치 버튼을 클릭했을때 내 위치로 이동하는 코드.
        mMap.setOnMyLocationClickListener(this);

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("search result");
                latitude = point.latitude; // 위도
                longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);
            }
        });
        ////////////////////

        // 버튼 이벤트
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str=editText.getText().toString();
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(addressList.get(0).toString());
                // 콤마를 기준으로 split
                String []splitStr = addressList.get(0).toString().split(",");
                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                System.out.println(address);

                String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                System.out.println(latitude);
                System.out.println(longitude);

                // 좌표(위도, 경도) 생성
                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                // 마커 생성
                MarkerOptions mOptions2 = new MarkerOptions();
                mOptions2.title("search result");
                mOptions2.snippet(address);
                mOptions2.position(point);
                // 마커 추가
                mMap.addMarker(mOptions2);
                // 해당 좌표로 화면 줌
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));

            }
        });


        LatLng hear = new LatLng(Jlatitude, Jlongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hear));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hear,16));
        MarkerOptions mOptions = new MarkerOptions();
        // 마커 타이틀
        mOptions.title("hear");
        latitude = Jlatitude; // 위도
        longitude = Jlongitude; // 경도
        // 마커의 스니펫(간단한 텍스트) 설정
        mOptions.snippet("당신이 찾은곳은 이곳입니다.");
        // LatLng: 위도 경도 쌍을 나타냄
        mOptions.position(new LatLng(latitude, longitude));
        // 마커(핀) 추가
        googleMap.addMarker(mOptions);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker){
                if (marker.getTitle().equals("search result")) {
                    Intent intent = new Intent(getApplicationContext(), create_room.class);
                    intent.putExtra("위도" ,latitude);
                    intent.putExtra("경도", longitude);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) { //onMyLocationClick 를 pubic으로 재정의
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }    //자신의 위치 (파란점) 누를시 텍스트 출력 "Current location:\n" + 해당위치, Toast.LENGTH_LONG
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "내위치 이동 버튼을 누르셨습니다.", Toast.LENGTH_SHORT).show();
        return false;       //내 위치이동 버튼 누를시 뜨는 텍스트출력
    }
}