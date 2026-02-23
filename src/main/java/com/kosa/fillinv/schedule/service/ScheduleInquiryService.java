package com.kosa.fillinv.schedule.service;

import com.kosa.fillinv.global.exception.BusinessException;
import com.kosa.fillinv.global.response.ErrorCode;
import com.kosa.fillinv.schedule.dto.response.ScheduleDetailResponse;
import com.kosa.fillinv.schedule.entity.Schedule;
import com.kosa.fillinv.schedule.entity.ScheduleTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleInquiryService { // 스케줄 조회 서비스

    private final ScheduleValidator validator;

    // 스케쥴 상세 조회
    public ScheduleDetailResponse getScheduleDetail(String memberId, String scheduleId, String scheduleTimeId) {
        Schedule schedule = validator.getSchedule(scheduleId);
        ScheduleTime scheduleTime = validator.getScheduleTime(scheduleTimeId);

        // 스케쥴과 스케쥴 타임이 연결되어 있는지 확인
        if (!scheduleTime.getSchedule().getId().equals(scheduleId)) {
            throw new BusinessException(ErrorCode.SCHEDULE_TIME_MISMATCH);
        }

        String mentorNickname = schedule.getMentorNickname();
        String menteeNickname = validator.getNickname(schedule.getMenteeId());

        // entity -> dto 변환
        // 사용자가 선택한 시간을 보여줘야 하므로 startTime 파라미터 추가
        return ScheduleDetailResponse.from(
                schedule,
                mentorNickname,
                menteeNickname,
                scheduleTime,
                schedule.getRole(memberId)
        );
    }
}
