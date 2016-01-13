package com.example.lenovo.ganhuoapp.Bean;

import java.util.ArrayList;

/**
 * Created by lenovo on 2015/11/5.
 */
public class MeiTuData {
    public ArrayList<ImgSource> results;
    public String error;

    public MeiTuData() {

    }

    public ArrayList<ImgSource> getResults() {
        return results;
    }

    public void setResults(ArrayList<ImgSource> results) {
        this.results = results;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public class ImgSource{
        @Override
        public String toString() {
            return "ImgSource{" +
                    "updatedAt='" + updatedAt + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", desc='" + desc + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", objectId='" + objectId + '\'' +
                    ", used='" + used + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    ", who='" + who + '\'' +
                    '}';
        }

        public ImgSource(String updatedAt, String publishedAt, String desc, String createdAt, String objectId, String used, String type, String url, String who) {
            this.updatedAt = updatedAt;
            this.publishedAt = publishedAt;
            this.desc = desc;
            this.createdAt = createdAt;
            this.objectId = objectId;
            this.used = used;
            this.type = type;
            this.url = url;
            this.who = who;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public String updatedAt;
     public String publishedAt;
     public String desc;
     public String createdAt;
     public String objectId;
     public String used;
     public String type;
     public String url;
     public String who;
    }


}
