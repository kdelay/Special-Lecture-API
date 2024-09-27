package lecture.special.interfaces.common;

public record ErrorResponse (
        String code,
        String message
) {
}