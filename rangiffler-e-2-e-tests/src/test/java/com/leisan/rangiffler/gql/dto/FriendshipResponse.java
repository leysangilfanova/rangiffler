package com.leisan.rangiffler.gql.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FriendshipResponse extends GraphQlResponse{
    @JsonProperty("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        @JsonProperty("friendship")
        private Friendship friendship;

        public Friendship getFriendship() {
            return friendship;
        }

        public void setFriendship(Friendship friendship) {
            this.friendship = friendship;
        }
    }

    public static class Friendship {
        @JsonProperty("id")
        private String id;
        @JsonProperty("username")
        private String username;
        @JsonProperty("friendStatus")
        private String friendStatus;
        @JsonProperty("__typename")
        private String typename;

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

        public String getFriendStatus() {
            return friendStatus;
        }

        public void setFriendStatus(String friendStatus) {
            this.friendStatus = friendStatus;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }
    }
}