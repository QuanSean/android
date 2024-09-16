package com.huypo.tase.Model;

public class Item {
    private Integer id;
    private String title;
    private Boolean done;
    private Boolean deleted;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Item(Integer id, String title, Boolean done, Boolean deleted) {
        this.id = id;
        this.title = title;
        this.done = done;
        this.deleted = deleted;
    }

    public Item() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
