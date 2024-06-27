package lecture.special.presentation.request;

import java.time.LocalDate;

public record ApplyRequest(Long userId, String speLecName, LocalDate speLecDate) {

}
