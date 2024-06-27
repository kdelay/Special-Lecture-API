package lecture.special;

public record ErrorResponse (
        String code,
        String message
) {
}