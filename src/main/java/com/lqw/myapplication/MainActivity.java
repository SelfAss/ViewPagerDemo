package com.lqw.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

    private TextView mTextView;
    private ViewPager mViewPager;
    private int mPreviousPosition;//记录上一个小圆点的位置
    private LinearLayout mLinearLayout;
    /**
     * 图片id
     */
    private int[] mImageIds = new int[]{R.mipmap.a,R.mipmap.b,R.mipmap.c,R.mipmap.
            d,R.mipmap.e,R.mipmap.f,R.mipmap.g};

    /**
     * 文字数组
     */
    private String[] mText = new String[]{"数百头母驴为何半夜惨叫?","小卖部安全套为何屡遭黑手?", "女生宿舍内裤为何频频失窃？",
            "连环弓虽女干母猪案，究竟是何人所为？", "老尼姑的门夜夜被敲，究竟是人是鬼？", "数百只小母狗意外身亡的背后又隐藏着什么？",
            "这一切的背后，是人性的扭曲还是道德的沦丧？"};

    /**
     * 广告条的自动轮播效果,将页面自动切换到下一个页面
     * 实现方法之一：可以利用handler发送一条延时消息
     * 实现方法之二：可以利用定时器
     */
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
           int currentItem = mViewPager.getCurrentItem();//获取viewPager当前页面的位置
            mViewPager.setCurrentItem(++currentItem);

            //继续发送延时两秒的消息，类似递归的效果，使广告一直切换
            mHandler.sendEmptyMessageDelayed(0, 2000);

            /**
             * 当用户触摸的时候，自动轮播就应该停止下来
             */
            mViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            mHandler.sendEmptyMessageDelayed(0, 2000);
                            break;
                        default:
                            break;
                    }
                    /**
                     * 如果这里返回true，viewPager的事件将会被消耗掉，ViewPager将会响应不了
                     * 所以这里要返回false，让viewPager原生的触摸效果正常运行
                     */
                    return false;
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.vp_viewpager);
        mTextView = (TextView) findViewById(R.id.tv_text);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_container);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 滑动过程回调事件
             * @param position
             * @param positionOffset
             * @param positionOffsetPixels
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {}

            /**
             * 页面选中回调事件
             * @param position
             */
            @Override
            public void onPageSelected(int position) {
                int pos = position % mImageIds.length;
                mTextView.setText(mText[pos]);//更新新闻标题
                Log.d("TAG", "当前位置：" + pos);

                /**
                 * 更新小圆点
                 * 将当前小圆点设置为红色，同时将上一个小圆点设置成灰色
                 */
                mLinearLayout.getChildAt(pos).setEnabled(true);
                mLinearLayout.getChildAt(mPreviousPosition).setEnabled(false);
                mPreviousPosition = pos;

            }

            /**
             * 滑动状态发生改变回调事件
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /**
         * 动态添加5个小圆点
         */
        for(int i = 0; i < mImageIds.length; i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.shape_point_selector);
            /**
             * 给小圆点之间设置间距，获取圆点的父布局的布局参数，然后给其设置左边距
             */
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            /**
             * 从第二个圆点开始设置左边距
             */
            if(i != 0) {
                params.leftMargin = 8;
                imageView.setEnabled(false);//设置除第一个圆点外的圆点都为黑色
            }
            imageView.setLayoutParams(params);
            mLinearLayout.addView(imageView);
        }

        //延时两秒发送消息
        mHandler.sendEmptyMessageDelayed(0, 2000);

        mViewPager.setAdapter(new MyViewPagerAdapter());
        /**
         * 设置ViewPager起始页为第一页，并且可以向左滑动
         */
        mViewPager.setCurrentItem(mImageIds.length * 5000);
    }
    /**
     * PagerAdapter：ViewPager的适配器
     * ViewPager适配器必须要实现的几个方法
     * getCount
     * isViewFromObject
     * instantiateItem
     * destroyItem
     */
    class MyViewPagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            /**
             * 返回图片的数量
             * 如果要使viewpager循环，直接返回比mImageIds.length大的值就可以了
             */
            return Integer.MAX_VALUE;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 初始化ViewPager中的View
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int pos = position % mImageIds.length;
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setBackgroundResource(mImageIds[pos]);
            container.addView(imageView);
            return imageView;
        }

        /**
         * 移除ViewPager中的View
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View) object);
        }
    }
}
