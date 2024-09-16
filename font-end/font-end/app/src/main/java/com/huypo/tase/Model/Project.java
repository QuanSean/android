package com.huypo.tase.Model;

import java.util.ArrayList;
import java.util.Date;

public class Project {
    private String _id;
    private String _idUser;
    private String title;
    private String description;
    private Boolean done;
    private Date deadline;
    private Boolean delete;
    private ArrayList<Task> task;

    public Project(String _id, String _idUser, String title, String description, Boolean done, Date deadline, Boolean delete, ArrayList task) {
        this._id = _id;
        this._idUser = _idUser;
        this.title = title;
        this.description = description;
        this.done = done;
        this.deadline = deadline;
        this.delete = delete;
        this.task = task;
    }

    public ArrayList getTask() {
        return task;
    }

    public void setTask(ArrayList task) {
        this.task = task;
    }

    public Project() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_idUser() {
        return _idUser;
    }

    public void set_idUser(String _idUser) {
        this._idUser = _idUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDone() {
        return done;
    }


    public void setDone(Boolean done) {
        this.done = done;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return "Project{" +
                "_id='" + _id + '\'' +
                ", _idUser='" + _idUser + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", done=" + done +
                ", deadline=" + deadline +
                ", delete=" + delete +
                ", task=" + task +
                '}';
    }
}
