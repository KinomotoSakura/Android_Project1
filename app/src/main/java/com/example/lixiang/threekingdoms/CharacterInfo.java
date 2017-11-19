package com.example.lixiang.threekingdoms;

public class CharacterInfo {
    private String name;
    private String sex;
    private String date;
    private String origin;
    private String force;
    private int resId;
    public CharacterInfo(String name, String sex, String date, String origin, String allegiance_force){
        this.name = name;
        this.sex = sex;
        this.date = date;
        this.origin = origin;
        this.force = allegiance_force;
        switch (name){
            case "蔡琰":
                resId = R.drawable.avatar_caiyan;
                break;
            case "曹操":
                resId = R.drawable.avatar_caocao;
                break;
            case "大乔":
                resId = R.drawable.avatar_daqiao;
                break;
            case "典韦":
                resId = R.drawable.avatar_dianwei;
                break;
            case "貂蝉":
                resId = R.drawable.avatar_diaochan;
                break;
            case "关羽":
                resId = R.drawable.avatar_guanyu;
                break;
            case "刘备":
                resId = R.drawable.avatar_liubei;
                break;
            case "吕布":
                resId = R.drawable.avatar_lvbu;
                break;
            case "司马懿":
                resId = R.drawable.avatar_simayi;
                break;
            case "孙策":
                resId = R.drawable.avatar_sunce;
                break;
            case "孙权":
                resId = R.drawable.avatar_sunquan;
                break;
            case "孙尚香":
                resId = R.drawable.avatar_sunshangxiang;
                break;
            case "文昭皇后":
                resId = R.drawable.avatar_wenzhaohuanghou;
                break;
            case "张飞":
                resId = R.drawable.avatar_zhangfei;
                break;
            case "周瑜":
                resId = R.drawable.avatar_zhouyu;
                break;
            case "诸葛亮":
                resId = R.drawable.avatar_zhugeliang;
                break;
            default:
                switch (sex) {
                    case "男":
                        resId = R.drawable.avatar_nan;
                        break;
                    case "女":
                        resId = R.drawable.avatar_nv;
                        break;
                }
                break;
        }
    }
    public String getName(){
        return name;
    }
    public String getSex(){
        return sex;
    }
    public String getDate(){
        return date;
    }
    public String getOrigin(){
        return origin;
    }
    public String getForce(){
        return force;
    }
    public int getResId(){
        return resId;
    }
    public void setResId(int resId) {
        this.resId = resId;
    }
}
