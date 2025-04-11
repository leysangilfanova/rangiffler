package com.leisan.rangiffler.gql.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LikeResponse extends GraphQlResponse {

    @JsonProperty("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        @JsonProperty("photo")
        private Photo photo;

        public Photo getPhoto() {
            return photo;
        }

        public void setPhoto(Photo photo) {
            this.photo = photo;
        }
    }

    public static class Photo {
        @JsonProperty("id")
        private String id;

        @JsonProperty("country")
        private Country country;

        @JsonProperty("description")
        private String description;

        @JsonProperty("likes")
        private Likes likes;

        @JsonProperty("__typename")
        private String typename;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Country getCountry() {
            return country;
        }

        public void setCountry(Country country) {
            this.country = country;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Likes getLikes() {
            return likes;
        }

        public void setLikes(Likes likes) {
            this.likes = likes;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }
    }

    public static class Country {
        @JsonProperty("code")
        private String code;

        @JsonProperty("name")
        private String name;

        @JsonProperty("flag")
        private String flag;

        @JsonProperty("__typename")
        private String typename;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }
    }

    public static class Likes {
        @JsonProperty("total")
        private int total;

        @JsonProperty("__typename")
        private String typename;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }
    }
}

