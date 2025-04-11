package com.leisan.rangiffler.gql.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FeedResponse extends GraphQlResponse{
    @JsonProperty("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        @JsonProperty("feed")
        private Feed feed;

        public Feed getFeed() {
            return feed;
        }

        public void setFeed(Feed feed) {
            this.feed = feed;
        }
    }

    public static class Feed {
        @JsonProperty("photos")
        private Photos photos;
        @JsonProperty("stat")
        private List<Stat> stat;
        @JsonProperty("__typename")
        private String typename;

        public Photos getPhotos() {
            return photos;
        }

        public void setPhotos(Photos photos) {
            this.photos = photos;
        }

        public List<Stat> getStat() {
            return stat;
        }

        public void setStat(List<Stat> stat) {
            this.stat = stat;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }
    }

    public static class Photos {
        @JsonProperty("edges")
        private List<Edge> edges;
        @JsonProperty("pageInfo")
        private PageInfo pageInfo;
        @JsonProperty("__typename")
        private String typename;

        public List<Edge> getEdges() {
            return edges;
        }

        public void setEdges(List<Edge> edges) {
            this.edges = edges;
        }

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }
    }

    public static class Edge {
        @JsonProperty("node")
        private PhotoNode node;
        @JsonProperty("__typename")
        private String typename;

        public PhotoNode getNode() {
            return node;
        }

        public void setNode(PhotoNode node) {
            this.node = node;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }
    }

    public static class PhotoNode {
        @JsonProperty("id")
        private String id;
        @JsonProperty("src")
        private String src;
        @JsonProperty("country")
        private CountryResponse.Country country;
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

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public CountryResponse.Country getCountry() {
            return country;
        }

        public void setCountry(CountryResponse.Country country) {
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

    public static class Likes {
        @JsonProperty("total")
        private int total;
        @JsonProperty("likes")
        private List<Like> likes;
        @JsonProperty("__typename")
        private String typename;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<Like> getLikes() {
            return likes;
        }

        public void setLikes(List<Like> likes) {
            this.likes = likes;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }
    }

    public static class Like {
        @JsonProperty("user")
        private String user;
        @JsonProperty("__typename")
        private String typename;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }
    }

    public static class PageInfo {
        @JsonProperty("hasPreviousPage")
        private boolean hasPreviousPage;
        @JsonProperty("hasNextPage")
        private boolean hasNextPage;
        @JsonProperty("__typename")
        private String typename;

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }
    }

    public static class Stat {
        @JsonProperty("count")
        private int count;
        @JsonProperty("country")
        private CountryResponse.Country country;
        @JsonProperty("__typename")
        private String typename;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public CountryResponse.Country getCountry() {
            return country;
        }

        public void setCountry(CountryResponse.Country country) {
            this.country = country;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }
    }
}