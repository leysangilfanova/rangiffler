package com.leisan.rangiffler.gql.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendsResponse extends GraphQlResponse{

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User {
        private Friends friends;

        public Friends getFriends() {
            return friends;
        }

        public void setFriends(Friends friends) {
            this.friends = friends;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Friends {
        private List<Edge> edges;
        private PageInfo pageInfo;

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
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Edge {
        private Node node;

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
            this.node = node;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Node {
        private String id;
        private String username;
        private String firstname;
        private String surname;
        private String avatar;
        private Location location;
        private String friendStatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getFriendStatus() {
            return friendStatus;
        }

        public void setFriendStatus(String friendStatus) {
            this.friendStatus = friendStatus;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        private String code;
        private String name;
        private String flag;

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
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageInfo {
        private Boolean hasPreviousPage;
        private Boolean hasNextPage;

        public Boolean getHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(Boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public Boolean getHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(Boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }
    }
}
