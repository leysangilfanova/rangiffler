package com.leisan.rangiffler.gql.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphQlResponse {

    @JsonProperty("data")
    private Object data;
    @JsonProperty("errors")
    private List<GraphQLError> errors;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<GraphQLError> getErrors() {
        return errors;
    }

    public void setErrors(List<GraphQLError> errors) {
        this.errors = errors;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GraphQLError {
        @JsonProperty("message")
        private String message;

        @JsonProperty("path")
        private List<String> path;

        @JsonProperty("locations")
        private List<Location> locations;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<String> getPath() {
            return path;
        }

        public void setPath(List<String> path) {
            this.path = path;
        }

        public List<Location> getLocations() {
            return locations;
        }

        public void setLocations(List<Location> locations) {
            this.locations = locations;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Location {
            @JsonProperty("line")
            private int line;

            @JsonProperty("column")
            private int column;

            public int getLine() {
                return line;
            }

            public void setLine(int line) {
                this.line = line;
            }

            public int getColumn() {
                return column;
            }

            public void setColumn(int column) {
                this.column = column;
            }
        }
    }
}

