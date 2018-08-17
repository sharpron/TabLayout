# TabLayout

![avatar](https://github.com/heyun1413/TabLayout/blob/master/demonstration.gif?raw=true)

# 使用
下载项目，编译

在你的布局文件中添加
```xml

<pub.ron.tablayout.widget.TabLayout
        android:id="@+id/tablayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:tabTextColor="@android:color/black"
        app:tabSelectedTextColor="@color/colorPrimary"
        app:lineHeight="1dp"
        app:lineColor="@color/colorPrimary"/>
```
在activity中

```java
List<Fragment> fragments = Arrays.<Fragment>asList(new TestFragment(), new TestFragment(), new TestFragment());
List<String> titles = Arrays.asList("全部", "未处理", "已处理");
TitledFragmentPagerAdapter adapter = new TitledFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles);
viewPager.setAdapter(adapter);
this.<TabLayout>findViewById(R.id.tablayout).setup(viewPager);
```
