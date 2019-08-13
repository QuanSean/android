package com.huypo.tase.Adapter;

import com.huypo.tase.Model.Item;

import java.util.ArrayList;

public class CardItemString {
    private String mTextResource;
    private String mTitleResource;
    private Integer idTask;
    private ArrayList<Item> items;


    public CardItemString(String title, String text) {
        mTitleResource = title;
        mTextResource = text;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Integer getIdTask() {
        return idTask;
    }

    public void setIdTask(Integer idTask) {
        this.idTask = idTask;
    }

    public String getmTextResource() {
        return mTextResource;
    }

    public void setmTextResource(String mTextResource) {
        this.mTextResource = mTextResource;
    }

    public String getmTitleResource() {
        return mTitleResource;
    }

    public void setmTitleResource(String mTitleResource) {
        this.mTitleResource = mTitleResource;
    }

    public ArrayList<Item> getListTask() {
        return items;
    }

    public void setListTask(ArrayList<Item> listTask) {
        this.items = listTask;
    }

    public CardItemString(String title, String text, ArrayList<Item> listTask) {
        mTitleResource = title;
        mTextResource = text;
        this.items = listTask;
    }


    public CardItemString(String title, String text, ArrayList<Item> listTask, Integer idTask) {
        mTitleResource = title;
        mTextResource = text;
        this.items = listTask;
        this.idTask=idTask;
    }

    public String getText() {
        return mTextResource;
    }

    public String getTitle() {
        return mTitleResource;
    }
}
