package com.huypo.tase.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huypo.tase.Activity.DashBoard;
import com.huypo.tase.Model.Item;
import com.huypo.tase.R;
import com.huypo.tase.Retrofit.IMyService;
import com.huypo.tase.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;

public class CardPagerAdapterS extends PagerAdapter implements CardAdapter {
    private List<CardView> mViews;
    private List<CardItemString> mData;
    private float mBaseElevation;

    public CardPagerAdapterS() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItemS(CardItemString item) {
        mViews.add(null);
        mData.add(item);
    }




    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }
    @Override
    public Object instantiateItem(View collection, final int pos) { //have to make final so we can see it inside of onClick()
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View page = inflater.inflate(R.layout.adapter, null);

        page.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //this will log the page number that was click
                Toast.makeText(DashBoard.getAppContext(),"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });


        ((ViewPager) collection).addView(page, 0);
        return page;
    }
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    String name;


    private void bind(CardItemString item, View view) {
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
//        TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);
        List<Item> a= new ArrayList<>();

        a.add(new Item(1,"a",false,false));
        a.add(new Item(2,"b",false,false));
        a.add(new Item(3,"c",false,false));

        ListView listView= (ListView) view.findViewById(R.id.listTask);
        ItemAdapterDemo i = new ItemAdapterDemo(DashBoard.getAppContext(),R.layout.item_tasks,a);

        ArrayList<String> itemTitle=new ArrayList<>();

        for (Item item1:item.getItems())
        {
            itemTitle.add(item1.getTitle());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(DashBoard.getAppContext(), android.R.layout.simple_list_item_1, itemTitle);
        listView.setAdapter(arrayAdapter);

        titleTextView.setText(item.getTitle());
//        contentTextView.setText(item.getText());
    }


}
