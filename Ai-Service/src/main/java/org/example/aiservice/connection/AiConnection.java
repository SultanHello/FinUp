package org.example.aiservice.connection;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AiConnection {

    @JsonProperty("contents")
    private List<Content> contents;

    // Геттеры и сеттеры
    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public static class Content {

        private String role;
        private List<Part> parts;

        // Геттеры и сеттеры
        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public List<Part> getParts() {
            return parts;
        }

        public void setParts(List<Part> parts) {
            this.parts = parts;
        }
    }

    public static class Part {

        private String text;
        public Part(String text){
            this.text= text;
        }


        // Геттеры и сеттеры
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}