package com.example.lixiang.threekingdoms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<CharacterInfo> character_list;
    private List<CharacterInfo> character_favo;
    private Fragment[] fragments;
    private int selectedIndex;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchContent(0);
                    return true;
                case R.id.navigation_search:
                    switchContent(1);
                    return true;
                case R.id.navigation_favorite:
                    switchContent(2);
                    return true;
            }
            return false;
        }

    };

    private void switchContent(int index){
        if(selectedIndex!=index) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, fragments[index])
                    .commit();
            selectedIndex = index;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] Name = new String[]{
                "蔡琰",
                "曹操",
                "大乔",
                "典韦",
                "貂蝉",
                "关羽",
                "刘备",
                "吕布",
                "司马懿",
                "孙策",
                "孙权",
                "孙尚香",
                "文昭皇后",
                "张飞",
                "周瑜",
                "诸葛亮"
        };
        final String[] Sex = new String[]{
                "女",
                "男",
                "女",
                "男",
                "女",
                "男",
                "男",
                "男",
                "男",
                "男",
                "男",
                "女",
                "女",
                "男",
                "男",
                "男"
        };
        final String[] Date = new String[]{
                "不详",
                "(155 - 220)",
                "不详",
                "(? - 197)",
                "虚构人物",
                "(? - 219)",
                "(161 - 223)",
                "(? - 198)",
                "(179 - 251)",
                "(175 - 200)",
                "(182 - 252)",
                "不详",
                "(183 - 221)",
                "不详",
                "(175 - 210)",
                "(181 - 234)"
        };
        final String[] Origin = new String[]{
                "兖州陈留郡圉（河南开封市杞县南二十五公里）",
                "豫州沛国谯（安徽亳州市亳县）",
                "扬州庐江郡皖（安徽安庆市潜山县）",
                "兖州陈留郡己吾（河南商丘市宁陵县西南二十五公里）",
                "虚构人物",
                "司隶河东郡解（山西运城市临猗县西南）",
                "幽州涿郡涿（河北保定市涿州）",
                "并州五原郡九原（内蒙古包头市九原区麻池镇西北古城遗址）",
                "司隶河内郡温（河南焦作市温县西）",
                "扬州吴郡富春（浙江杭州市富阳）",
                "扬州吴郡富春（浙江杭州市富阳）",
                "扬州吴郡富春（浙江杭州市富阳）",
                "冀州中山国毋极（河北石家庄市无极县西、滋水北岸）",
                "幽州涿郡（河北保定市涿州）",
                "扬州庐江郡舒（安徽合肥市庐江县西南）",
                "徐州琅邪国阳都（山东临沂市沂南县南）"
        };
        final String[] Force = new String[]{
                "魏",
                "魏",
                "吴",
                "魏",
                "东汉",
                "蜀",
                "蜀",
                "东汉",
                "魏",
                "吴",
                "吴",
                "吴",
                "魏",
                "蜀",
                "吴",
                "蜀"
        };

        character_list = new ArrayList<CharacterInfo>();
        character_favo = new ArrayList<CharacterInfo>();

        for (int i = 0; i < Name.length; i++) {
            character_list.add(new CharacterInfo(Name[i],  Sex[i], Date[i], Origin[i], Force[i]));
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment homeFragment = HomeFragment.newInstance(character_list);
        fragments = new Fragment[]{homeFragment, homeFragment, homeFragment};
        selectedIndex = -1;
        switchContent(0);
    }

}
