package com.example.gatherplan.controller.service;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.service.TempAppointmentService;
import com.example.gatherplan.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TempAppointmentFacadeService {
    public final TempAppointmentService tempAppointmentService;
    public final RegionService regionService;


    public String registerAppointment(CreateTempAppointmentReqDto reqDto) {
        return tempAppointmentService.registerAppointment(reqDto);
    }

    public TempAppointmentInfoRespDto retrieveAppointmentInfo(TempAppointmentInfoReqDto reqDto) {
        return tempAppointmentService.retrieveAppointmentInfo(reqDto);
    }

    public void updateAppointment(UpdateTempAppointmentReqDto reqDto) {
        tempAppointmentService.updateAppointment(reqDto);
    }

    public void deleteAppointment(DeleteTempAppointmentReqDto reqDto) {
        tempAppointmentService.deleteAppointment(reqDto);
    }

    public void registerAppointmentJoin(CreateTempAppointmentJoinReqDto reqDto) {
        tempAppointmentService.registerAppointmentJoin(reqDto);
    }

    public List<TempAppointmentParticipantsRespDto> retrieveAppointmentParticipants(TempAppointmentParticipantsReqDto reqDto) {
        return tempAppointmentService.retrieveAppointmentParticipants(reqDto);
    }

    public TempAppointmentMyParticipantRespDto retrieveAppointmentMyParticipant(TempAppointmentMyParticipantReqDto reqDto) {
        return tempAppointmentService.retrieveAppointmentMyParticipant(reqDto);
    }

    public void updateAppointmentJoin(UpdateTempAppointmentJoinReqDto reqDto) {
        tempAppointmentService.updateAppointmentJoin(reqDto);
    }

    public void deleteAppointmentJoin(DeleteTempAppointmentJoinReqDto reqDto) {
        tempAppointmentService.deleteAppointmentJoin(reqDto);
    }

    public List<TempAppointmentCandidateInfoRespDto> retrieveCandidateInfo(TempAppointmentCandidateInfoReqDto reqDto) {
        return tempAppointmentService.retrieveCandidateInfo(reqDto);
    }

    public void confirmAppointment(TempConfirmAppointmentReqDto reqDto) {
        tempAppointmentService.confirmAppointment(reqDto);
    }

    public boolean checkHost(TempCheckHostReqDto reqDto) {
        return tempAppointmentService.checkHost(reqDto);
    }

    public boolean checkJoin(TempCheckJoinReqDto reqDto) {
        return tempAppointmentService.checkJoin(reqDto);
    }

    public boolean checkUser(TempCheckJoinReqDto reqDto) {
        return tempAppointmentService.checkUser(reqDto);
    }

    public boolean validJoin(CreateTempUserReqDto reqDto) {
        return tempAppointmentService.validJoin(reqDto);
    }
}
