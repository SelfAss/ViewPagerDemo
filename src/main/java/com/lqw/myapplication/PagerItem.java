package com.lqw.myapplication;

/**
 * Created by Administrator on 2016/6/14.
 */
public class PagerItem
{
    private int imageResId;
    private String intro;

    public PagerItem(int imageResId, String intro) {
        this.imageResId = imageResId;
        this.intro = intro;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
