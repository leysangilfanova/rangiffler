package com.leisan.rangiffler.gql;

public enum FriendshipActionType {
    ADD("ADD"),
    ACCEPT("ACCEPT"),
    DELETE("DELETE"),
    REJECT("REJECT");

    private final String action;

    FriendshipActionType(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}