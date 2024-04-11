package com.example.gatherplan.appointment.validator;

import com.example.gatherplan.appointment.enums.TimeType;
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
        List<TimeType> candidateTimeTypeList = appointment.getCandidateTimeTypeList();

        return selectedDateTimeList.stream()
                .filter(selectedDateTime ->
                        candidateDateList.stream()
                                .noneMatch(candidateDate -> candidateDate.isEqual(selectedDateTime.getSelectedDate()))
                                || candidateTimeTypeList.stream()
                                .noneMatch(timeType ->
                                        !timeType.getStartTime().isAfter(selectedDateTime.getSelectedStartTime()) &&
                                                !timeType.getEndTime().isBefore(selectedDateTime.getSelectedEndTime())
                                ))
                .findFirst();
    }
}
