package com.kosa.fillinv.schedule.service;

import com.kosa.fillinv.global.exception.BusinessException;
import com.kosa.fillinv.global.response.ErrorCode;
import com.kosa.fillinv.schedule.dto.response.ScheduleListResponse;
import com.kosa.fillinv.schedule.entity.Schedule;
import com.kosa.fillinv.schedule.entity.ScheduleStatus;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService { // 스케쥴 수정 및 삭제 (상태 변경 전담)

    private final ScheduleValidator validator;

    // ------- Public API - 외부 호출 핵심 메서드
    // 스케쥴 상태 기본값은 결제 대기
    // 멘티가 결제를 성공했을 경우 (결제 대기 -> 승인 대기)
    public ScheduleListResponse completePayment(String scheduleId) {
        Schedule schedule = validator.getSchedule(scheduleId);

        // 결제 대기 상태인 스케쥴만 승인 대기로 상태 변경 가능
        if (schedule.getStatus() != ScheduleStatus.PAYMENT_PENDING) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_STATUS);
        }

        schedule.updateStatus(ScheduleStatus.APPROVAL_PENDING);
        return scheduleStatusChangeResponse(schedule);
    }

    // 멘토가 멘티의 레슨 수강신청을 승인했을 경우 (승인 대기 -> 승인)
    public ScheduleListResponse approveLessonByMentor(String scheduleId) {
        Schedule schedule = validator.getSchedule(scheduleId);

        // 결제 대기 상태인 스케쥴만 승인 대기로 상태 변경 가능
        if (schedule.getStatus() != ScheduleStatus.APPROVAL_PENDING) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_STATUS);
        }

        schedule.updateStatus(ScheduleStatus.APPROVED);
        return scheduleStatusChangeResponse(schedule);
    }


    // 멘토가 멘티의 레슨 수강신청을 거절했을 경우(승인 대기 -> 취소)
    public ScheduleListResponse rejectLessonByMentor(String scheduleId) {
        Schedule schedule = validator.getSchedule(scheduleId);

        // 결제 대기 상태인 스케쥴만 승인 대기로 상태 변경 가능
        if (schedule.getStatus() != ScheduleStatus.APPROVAL_PENDING) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_STATUS);
        }

        schedule.updateStatus(ScheduleStatus.CANCELED);
        return scheduleStatusChangeResponse(schedule);
    }

    // 해당 레슨 수강이 모두 끝난 경우 (승인 -> 완료)
    public ScheduleListResponse completeLesson(String scheduleId) {
        Schedule schedule = validator.getSchedule(scheduleId);

        // 결제 대기 상태인 스케쥴만 승인 대기로 상태 변경 가능
        if (schedule.getStatus() != ScheduleStatus.APPROVED) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_STATUS);
        }

        schedule.updateStatus(ScheduleStatus.COMPLETED);
        return scheduleStatusChangeResponse(schedule);
    }

    // ------- Private Method - 내부 보조 메서드 (비즈니스 로직)
    private ScheduleListResponse scheduleStatusChangeResponse(Schedule schedule) {
        String mentorNickname = validator.getNickname(schedule.getMentorId());
        String menteeNickname = validator.getNickname(schedule.getMenteeId());
        Instant startTime = schedule.getScheduleTimeList().get(0).getStartTime();

        return ScheduleListResponse.from(schedule, mentorNickname, menteeNickname, startTime);
    }
}
