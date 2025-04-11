package com.leisan.rangiffler.gql;

public enum FriendsQueryType {
    FRIENDS("GetFriends", "friends"),
    INCOME_INVITATIONS("GetInvitations", "incomeInvitations"),
    OUTCOME_INVITATIONS("GetOutcomeInvitations", "outcomeInvitations"),
    PEOPLE("GetPeople", "users");

    private final String operationName;
    private final String fieldName;

    FriendsQueryType(String operationName, String fieldName) {
        this.operationName = operationName;
        this.fieldName = fieldName;
    }

    public String getOperationName() {
        return operationName;
    }

    public String getFieldName() {
        return fieldName;
    }
}