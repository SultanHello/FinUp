package org.example.aiservice.connection;



import lombok.Data;
import java.util.List;

@Data
public class AiResponse {
    private List<Candidate> candidates;
    private UsageMetadata usageMetadata;
    private String modelVersion;

    @Data
    public static class Candidate {
        private Content content;
        private String finishReason;
        private double avgLogprobs;
    }

    @Data
    public static class Content {
        private List<Part> parts;
        private String role;
    }

    @Data
    public static class Part {
        private String text;
    }

    @Data
    public static class UsageMetadata {
        private int promptTokenCount;
        private int candidatesTokenCount;
        private int totalTokenCount;
        private List<TokensDetail> promptTokensDetails;
        private List<TokensDetail> candidatesTokensDetails;
    }

    @Data
    public static class TokensDetail {
        private String modality;
        private int tokenCount;
    }
}
