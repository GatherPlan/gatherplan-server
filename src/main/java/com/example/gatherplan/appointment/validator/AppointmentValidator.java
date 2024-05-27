package com.example.gatherplan.appointment.validator;

import com.example.gatherplan.common.unit.SelectedDateTime;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class AppointmentValidator {

    public Optional<SelectedDateTime> retrieveInvalidSelectedDateTime(
            List<LocalDate> candidateDateList, List<SelectedDateTime> selectedDateTimeList) {

        return selectedDateTimeList.stream()
                .filter(selectedDateTime ->
                        candidateDateList.stream()
                                .noneMatch(candidateDate -> candidateDate.isEqual(selectedDateTime.getSelectedDate()))
                )
                .findFirst();
    }

}
