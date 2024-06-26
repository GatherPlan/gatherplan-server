package com.example.gatherplan.controller.service;

import com.example.gatherplan.appointment.dto.*;
import com.example.gatherplan.appointment.service.AppointmentService;
import com.example.gatherplan.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentFacadeService {
    public final AppointmentService appointmentService;
    public final RegionService regionService;

    public String registerAppointment(CreateAppointmentReqDto reqDto, Long id, String username) {
        return appointmentService.registerAppointment(reqDto, id, username);
    }

    public AppointmentInfoRespDto retrieveAppointmentInfo(String appointmentCode, Long id) {
        return appointmentService.retrieveAppointmentInfo(appointmentCode, id);
    }

    public void updateAppointment(UpdateAppointmentReqDto reqDto, Long id) {
        appointmentService.updateAppointment(reqDto, id);
    }

    public void deleteAppointment(String appointmentCode, Long id) {
        appointmentService.deleteAppointment(appointmentCode, id);
    }

    public void registerAppointmentJoin(CreateAppointmentJoinReqDto reqDto, Long id) {
        appointmentService.registerAppointmentJoin(reqDto, id);
    }

    public List<AppointmentParticipantsRespDto> retrieveAppointmentParticipants(String appointmentCode, Long id) {
        return appointmentService.retrieveAppointmentParticipants(appointmentCode, id);
    }

    public AppointmentMyParticipantRespDto retrieveAppointmentMyParticipant(String appointmentCode, Long id) {
        return appointmentService.retrieveAppointmentMyParticipant(appointmentCode, id);
    }

    public void updateAppointmentJoin(UpdateAppointmentJoinReqDto reqDto, Long id) {
        appointmentService.updateAppointmentJoin(reqDto, id);
    }

    public void deleteAppointmentJoin(String appointmentCode, Long id) {
        appointmentService.deleteAppointmentJoin(appointmentCode, id);
    }

    public Page<AppointmentCandidateInfoRespDto> retrieveCandidateInfo(AppointmentCandidateInfoReqDto reqDto, Long id) {
        return appointmentService.retrieveCandidateInfo(reqDto, id);
    }

    public void confirmAppointment(ConfirmAppointmentReqDto reqDto, Long id) {
        appointmentService.confirmAppointment(reqDto, id);
    }

    public boolean checkHost(String appointmentCode, Long id) {
        return appointmentService.checkHost(appointmentCode, id);
    }

    public boolean checkJoin(String appointmentCode, Long id) {
        return appointmentService.checkJoin(appointmentCode, id);
    }

    public boolean checkName(String appointmentCode, String username) {
        return appointmentService.checkName(appointmentCode, username);
    }

    public boolean checkNickname(String appointmentCode, String nickname) {
        return appointmentService.checkNickname(appointmentCode, nickname);
    }

    public List<AppointmentSearchListRespDto> retrieveAppointmentSearchList(String keyword, Long id) {
        return appointmentService.retrieveAppointmentSearchList(keyword, id);
    }

    public AppointmentPreviewRespDto retrieveAppointmentPreview(String appointmentCode) {
        return appointmentService.retrieveAppointmentPreview(appointmentCode);
    }
}
