package comp3011.assignment1.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

// Assisted in creating the record
// Found response type at https://developers.openai.com/api/reference/resources/audio/subresources/transcriptions/methods/create

public record TranscriptResponse(
    String text,
    Usage usage
) {
    public record Usage(
        @JsonProperty("type") String type,
        @JsonProperty("input_tokens") Integer inputTokens,
        @JsonProperty("input_token_details") InputTokenDetails inputTokenDetails,
        @JsonProperty("output_tokens") Integer outputTokens,
        @JsonProperty("total_tokens") Integer totalTokens
    ) {
        public record InputTokenDetails(
            @JsonProperty("text_tokens") Integer textTokens,
            @JsonProperty("audio_tokens") Integer audioTokens
        ) {}
    }
};