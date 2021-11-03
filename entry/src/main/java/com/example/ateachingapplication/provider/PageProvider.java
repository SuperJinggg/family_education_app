package com.example.ateachingapplication.provider;

import com.example.ateachingapplication.domain.ImageItem;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;
import java.util.List;

public class PageProvider extends PageSliderProvider {
    private List<ImageItem> pages;
    private AbilitySlice context;

    public PageProvider(List<ImageItem> pages,AbilitySlice context) {
        this.pages = pages;
        this.context=context;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public Object createPageInContainer(ComponentContainer componentContainer, int i) {
        ImageItem item=pages.get(i);
        //创建图片组件
        Image image=new Image(context);

        //设置图片平铺Image组件的所有宽高
        image.setScaleMode(Image.ScaleMode.STRETCH);
        //设置图片的高宽
        image.setLayoutConfig(
                new StackLayout.LayoutConfig(
                        StackLayout.LayoutConfig.MATCH_PARENT,
                        StackLayout.LayoutConfig.MATCH_PARENT));
        //添加图片
        image.setPixelMap(pages.get(i).getImage());
        StackLayout.LayoutConfig lc=new StackLayout.LayoutConfig(
                StackLayout.LayoutConfig.MATCH_PARENT,
                StackLayout.LayoutConfig.MATCH_CONTENT);

        StackLayout sl=new StackLayout(context);
        sl.setLayoutConfig(new StackLayout.LayoutConfig(StackLayout.LayoutConfig.MATCH_PARENT,
                StackLayout.LayoutConfig.MATCH_PARENT));
        //将图片和文本组件添加至层布局
        sl.addComponent(image);
        //将层布局放入滑页组件中
        componentContainer.addComponent(sl);
        return sl;
    }

    @Override
    public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
        //滑出屏幕的组件进行移除
        componentContainer.removeComponent((Component) o);
    }

    @Override
    public boolean isPageMatchToObject(Component component, Object o) {
        //判断滑页上的每一页的组件和内容是否保持一致
        return true;
    }

}
