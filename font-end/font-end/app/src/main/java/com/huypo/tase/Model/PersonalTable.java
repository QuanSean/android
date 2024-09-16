package com.huypo.tase.Model;

import java.io.Serializable;
import java.util.Date;

public class PersonalTable implements Serializable {
    private String txtTableName;
    private String txtTableDescription;
    private Date txtTableDeadline;
    private String token;
    private String idProject;
    private String idTask;
    private String title;
    private Boolean done;

    public String getTitle() {
        return title;
    }

    public PersonalTable(String txtTableName, String txtTableDescription, Date txtTableDeadline, String token, String idProject, String title, Boolean done) {
        this.txtTableName = txtTableName;
        this.txtTableDescription = txtTableDescription;
        this.txtTableDeadline = txtTableDeadline;
        this.token = token;
        this.idProject = idProject;
        this.idTask = idTask;
        this.title = title;
        this.done = done;
    }

    public PersonalTable(String txtTableName, String txtTableDescription, Date txtTableDeadline, String token, String idProject, String title) {
        this.txtTableName = txtTableName;
        this.txtTableDescription = txtTableDescription;
        this.txtTableDeadline = txtTableDeadline;
        this.token = token;
        this.idProject = idProject;
        this.title=title;
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

    public void setTxtTableName(String txtTableName) {
        this.txtTableName = txtTableName;
    }

    public void setTxtTableDescription(String txtTableDescription) {
        this.txtTableDescription = txtTableDescription;
    }

    public void setTxtTableDeadline(Date txtTableDeadline) {
        this.txtTableDeadline = txtTableDeadline;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public String getIdTask() {
        return idTask;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public PersonalTable(String txtTableName, String txtTableDescription, Date txtTableDeadline){

        this.txtTableName = txtTableName;
        this.txtTableDescription = txtTableDescription;
        this.txtTableDeadline = txtTableDeadline;
    }


    public String getTxtTableName() {
        return txtTableName;
    }

    public String getTxtTableDescription() {
        return txtTableDescription;
    }

    public Date getTxtTableDeadline() {
        return txtTableDeadline;
    }
}
