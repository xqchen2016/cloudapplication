package edu.zju.sa.dao;

/**
 * Created by chen on 2016/2/15.
 */
public class FileItem {
    private String name;

    public FileItem(){
    }

    public FileItem(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
