package comp3011.assignment1.responses;

public record GlobalStatsResponse (
	Integer inputTokens, //  Total input tokens consumed by the speech-to-text Cloud service since UTC server start
	Integer outputTokens // Total output tokens produced by the speech-to-text Cloud service since UTC server start
) {};