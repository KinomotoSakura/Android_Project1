package com.example.lixiang.threekingdoms;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import java.io.File;

import org.greenrobot.eventbus.EventBus;

public class editCharacter extends AppCompatActivity{
    ImageView characterIcon;
    TextView characterName,characterOrigin,characterDate,characterInfo,characterForce;
    RadioGroup characterSex;
    Button confirm,cancel;
    String nameData,originData,infoData,forceData,sexData,dateData;
    DrawerLayout drawerLayout;
    int iconID,imgID;
    int ACTION_TYPE,listPosition;
    int []recycler_res;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_character);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("编辑人物信息");

        findView();
        getAction();
        setListener();

        drawerLayout = (DrawerLayout)findViewById(R.id.activity_layout);
        ActionBarDrawerToggle toggle
                = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.navigation_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        initRecyclerRes();
        recyclerView.setAdapter(new recyclerViewAdapter());

        click_image(characterIcon);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(this, "com.zz.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        Toast.makeText(this,"设备没有SD卡！",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this,"请允许打开相机！！",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    Toast.makeText(this,"请允许打操作SDCard！！",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    //自动获取Storage权限
    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, "com.zz.fileprovider", new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        Toast.makeText(this,"设备没有SD卡！",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        characterIcon.setImageBitmap(bitmap);
                    }
                    break;
                default:
            }
        }
    }

    //自动获取相机权限
    void TakePhoto(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this,"您已经拒绝过一次",Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(this, "com.zz.fileprovider", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                Toast.makeText(this,"设备没有SD卡！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void click_image(View img){
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(editCharacter.this);
                dialog.setTitle("更换头像");
                final String[] items = {"从相册选择照片", "拍照"};
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplication(), "你选择了"
                            + items[which], Toast.LENGTH_SHORT).show();
                        if(which == 1){
                            TakePhoto();
                        }else{
                            autoObtainStoragePermission();
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });
    }

    private void findView(){
        characterIcon=(ImageView)findViewById(R.id.edit_icon);
        characterName=(TextView)findViewById(R.id.edit_name);
        characterOrigin=(TextView)findViewById(R.id.edit_origin);
        characterForce=(TextView)findViewById(R.id.edit_force);
        characterDate=(TextView)findViewById(R.id.edit_date);
        characterInfo=(TextView)findViewById(R.id.edit_moreinfo);
        characterSex=(RadioGroup)findViewById(R.id.edit_sex);
        confirm=(Button)findViewById(R.id.edit_confirm);
        cancel=(Button)findViewById(R.id.edit_cancel);
    }
    private void setListener(){
        //characterIcon.setOnClickListener(new iconOnClickListener());
        confirm.setOnClickListener(new confirmOnClickListener());
        cancel.setOnClickListener(new cancelOnClickListener());
    }

    private class confirmOnClickListener implements View.OnClickListener{
        public void onClick(View view){
            //点击确认按钮事件
            readData();
            switch (ACTION_TYPE){
                case EditEvent.EDIT_ACTION:
                    EventBus.getDefault().post(new EditEvent(imgID,iconID,nameData,sexData,dateData,originData,forceData,infoData,listPosition));
                    break;
                case EditEvent.NEW_ACTION:
                    EventBus.getDefault().post(new EditEvent(imgID,iconID,nameData,sexData,dateData,originData,forceData,infoData));
                    break;
            }
            finish();
        }
    }
    private class cancelOnClickListener implements View.OnClickListener{
        public void onClick(View view){
            //点击确认按钮事件
            Toast.makeText(getApplicationContext(),"已取消",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void readData(){
        nameData=characterName.getText().toString();
        originData=characterOrigin.getText().toString();
        infoData=characterInfo.getText().toString();
        sexData=characterSex.getCheckedRadioButtonId()== R.id.edit_sex_male?"男":"女";
        forceData=characterForce.getText().toString();
        dateData=characterDate.getText().toString();
        //获得bg图片
        getImgByIcon();
    }

    //获取当前操作类型
    private void getAction(){
        ACTION_TYPE=getIntent().getIntExtra("ACTION", EditEvent.NEW_ACTION);//默认为新建
        switch (ACTION_TYPE){
            case EditEvent.EDIT_ACTION:
                //获取到位置信息
                listPosition=getIntent().getIntExtra("listPosition",-1);
                CharacterInfo characterInfo_=getIntent().getParcelableExtra("charactersInfo");
                //其他信息的设置 图片、性别、人物生平等等
                characterIcon.setImageResource(characterInfo_.getResId());
                iconID=characterInfo_.getResId();
                imgID=characterInfo_.getBgId();
                if(characterInfo_.getSex().equals("男")){
                    characterSex.check(R.id.edit_sex_male);
                }
                else{
                    characterSex.check(R.id.edit_sex_female);
                }
                characterName.setText(characterInfo_.getName());
                characterOrigin.setText(characterInfo_.getOrigin());
                characterDate.setText(characterInfo_.getDate());
                characterInfo.setText(characterInfo_.getInfo());
                characterForce.setText(characterInfo_.getForce());
                break;
        }
    }

    private void getImgByIcon(){
        switch (iconID){
            case R.drawable.avatar_caiyan:
                imgID = R.drawable.bg_caiyan;
                break;
            case R.drawable.avatar_caocao:
                imgID = R.drawable.bg_caocao;
                break;
            case R.drawable.avatar_daqiao:
                imgID = R.drawable.bg_daqiao;
                break;
            case R.drawable.avatar_dianwei:
                imgID = R.drawable.bg_dianwei;
                break;
            case R.drawable.avatar_diaochan:
                imgID = R.drawable.bg_diaochan;
                break;
            case R.drawable.avatar_guanyu:
                imgID = R.drawable.bg_guanyu;
                break;
            case R.drawable.avatar_liubei:
                imgID = R.drawable.bg_liubei;
                break;
            case R.drawable.avatar_lvbu:
                imgID = R.drawable.bg_lvbu;
                break;
            case R.drawable.avatar_simayi:
                imgID = R.drawable.bg_simayi;
                break;
            case R.drawable.avatar_sunce:
                imgID = R.drawable.bg_sunce;
                break;
            case R.drawable.avatar_sunquan:
                imgID = R.drawable.bg_sunquan;
                break;
            case R.drawable.avatar_sunshangxiang:
                imgID = R.drawable.bg_sunshangxiang;
                break;
            case R.drawable.avatar_wenzhaohuanghou:
                imgID = R.drawable.bg_wenzhaohuanghou;
                break;
            case R.drawable.avatar_zhangfei:
                imgID = R.drawable.bg_zhangfei;
                break;
            case R.drawable.avatar_zhouyu:
                imgID = R.drawable.bg_zhouyu;
                break;
            case R.drawable.avatar_zhugeliang:
                imgID = R.drawable.bg_zhugeliang;
                break;
            case R.drawable.avatar_nan:
                imgID = R.drawable.bg_nan;
                break;
            case R.drawable.avatar_nv:
                imgID = R.drawable.bg_nv;
                break;
            default:
                switch (sexData) {
                    case "男":
                        iconID = R.drawable.avatar_nan;
                        imgID = R.drawable.bg_nan;
                        break;
                    case "女":
                        iconID = R.drawable.avatar_nv;
                        imgID = R.drawable.bg_nv;
                        break;
                }
                break;
        }
    }
    private void initRecyclerRes(){
        recycler_res=new int[]{
                R.drawable.avatar_caiyan, R.drawable.avatar_caocao,
                R.drawable.avatar_daqiao, R.drawable.avatar_dianwei,
                R.drawable.avatar_diaochan, R.drawable.avatar_guanyu,
                R.drawable.avatar_liubei, R.drawable.avatar_lvbu,
                R.drawable.avatar_simayi, R.drawable.avatar_sunce,
                R.drawable.avatar_sunquan, R.drawable.avatar_sunshangxiang,
                R.drawable.avatar_wenzhaohuanghou, R.drawable.avatar_zhangfei,
                R.drawable.avatar_zhouyu, R.drawable.avatar_zhugeliang,
                R.drawable.avatar_nan, R.drawable.avatar_nv
        };
    }

    //RecyclerView Adapter
    private class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_recycler_item,viewGroup,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position){
            viewHolder.imageView.setImageResource(recycler_res[position]);
        }

        @Override
        public int getItemCount(){return recycler_res.length;}


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public ImageView imageView;
            public ViewHolder(View view){
                super(view);
                imageView=(ImageView)view.findViewById(R.id.edit_recycler_icon);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view){
                int pos=getPosition();
                characterIcon.setImageResource(recycler_res[pos]);
                iconID=recycler_res[pos];
                drawerLayout.closeDrawers();
            }
        }

    }

}
