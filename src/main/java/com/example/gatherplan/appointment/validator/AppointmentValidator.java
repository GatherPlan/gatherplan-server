package com.example.gatherplan.appointment.validator;

import com.example.gatherplan.appointment.repository.entity.Appointment;
import com.example.gatherplan.common.unit.SelectedDateTime;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class AppointmentValidator {

    public Optional<SelectedDateTime> retrieveInvalidSelectedDateTime(
            Appointment appointment, List<SelectedDateTime> selectedDateTimeList) {

        List<LocalDate> candidateDateList = appointment.getCandidateDateList();

        return selectedDateTimeList.stream()
                .filter(selectedDateTime ->
                        candidateDateList.stream()
                                .noneMatch(candidateDate -> candidateDate.isEqual(selectedDateTime.getSelectedDate()))
                )
                .findFirst();
    }

}
