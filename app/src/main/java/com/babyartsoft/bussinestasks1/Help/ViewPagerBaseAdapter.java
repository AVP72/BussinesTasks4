package com.babyartsoft.bussinestasks1.Help;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.babyartsoft.bussinestasks1.R;

public class ViewPagerBaseAdapter extends PagerAdapter {

    private Context context;
    private Integer [] images;

    ViewPagerBaseAdapter(Context context, Integer[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (layoutInflater != null){
            View view = layoutInflater.inflate(R.layout.help_adapter_layout, null);
            ImageView imageView = view.findViewById(R.id.help_adapter_view_page);
            imageView.setImageResource(images[position]);

            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;
        }
        return new View(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager)container;
        View view = (View) object;
        vp.removeView(view);
    }
}
