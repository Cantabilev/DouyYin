package org.cantabile.douyin.activity.music;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.facebook.common.file.FileUtils;

import org.cantabile.douyin.R;

public class LocalSearchAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_local_search);
    }

    /**
     * 模糊查找音乐
     * @param key
     */
    private void quertMusic(String key) {
        String[] musics = new String[]{};
        if (!TextUtils.isEmpty(key)){
            //musics = FileUtils.queryMusic(this, key);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, musics);
        //mLvMusic.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        if (requestCode == REQ_PERMISSION) {
//            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(this, "读取SD卡权限被拒绝了", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}
