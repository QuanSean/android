package com.huypo.tase.Model;

import java.util.Date;

public class Project_Partner {
    private String token;
    private String _id;
    private String _idUser;
    private String title;
    private String description;
    private Boolean done;
    private Date deadline;
    private Boolean delete;
    private String idProject;

    public Project_Partner(String token, String _id, String _idUser, String title, String description, Boolean done, Date deadline, Boolean delete) {
        this.token = token;
        this._id = _id;

        this._idUser = _idUser;
        this.title = title;
        this.description = description;
        this.done = done;
        this.deadline = deadline;
        this.delete = delete;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public Project_Partner(String token, String _id, String _idUser, String title, String description, Boolean done, Date deadline, Boolean delete, String idProject) {
        this.token = token;
        this._id = _id;
        this._idUser = _idUser;
        this.title = title;
        this.description = description;
        this.done = done;
        this.deadline = deadline;
        this.delete = delete;
        this.idProject = idProject;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
