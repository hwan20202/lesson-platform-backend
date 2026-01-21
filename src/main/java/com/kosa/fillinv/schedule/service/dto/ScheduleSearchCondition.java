package com.kosa.fillinv.schedule.service.dto;

import com.kosa.fillinv.schedule.entity.ScheduleStatus;
import com.kosa.fillinv.schedule.repository.ScheduleParticipantRole;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ScheduleSearchCondition(
        String keyword,
        ScheduleStatus status,
        Instant from,
        Instant to,
        ScheduleSortType sortType,
        String memberId,
        ScheduleParticipantRole participantRole,
        Integer page,
        Integer size
) {

    public ScheduleSearchCondition {
        if (sortType == null) {
            sortType = ScheduleSortType.START_TIME_ASC;
        }
        if (page == null || page < 0) {
            page = 0;
        }
        if (size == null || size <= 0) {
            size = 10;
        }
    }

    public static ScheduleSearchCondition defaultCondition() {
        return new ScheduleSearchCondition(
                null,null, null, null,null,
                null, null, null, null
        );
    }

    public ScheduleSearchCondition toPast(Instant to) {
        return this.to(to).sort(ScheduleSortType.START_TIME_DESC);
    }

    public ScheduleSearchCondition toIntended(Instant from) {
        return this.from(from).sort(ScheduleSortType.START_TIME_ASC);
    }

    public ScheduleSearchCondition participate(String memberId) {
        return new ScheduleSearchCondition(
                this.keyword,
                this.status,
                this.from,
                this.to,
                this.sortType,
                memberId,
                ScheduleParticipantRole.BOTH,
                this.page,
                this.size
        );
    }

    public ScheduleSearchCondition mentee(String memberId) {
        return new ScheduleSearchCondition(
                this.keyword,
                this.status,
                this.from,
                this.to,
                this.sortType,
                memberId,
                ScheduleParticipantRole.MENTEE,
                this.page,
                this.size
        );
    }

    public ScheduleSearchCondition mentor(String memberId) {
        return new ScheduleSearchCondition(
                this.keyword,
                this.status,
                this.from,
                this.to,
                this.sortType,
                memberId,
                ScheduleParticipantRole.MENTOR,
                this.page,
                this.size
        );
    }

    public ScheduleSearchCondition status(ScheduleStatus status) {
        return new ScheduleSearchCondition(
                this.keyword,
                status,
                this.from,
                this.to,
                this.sortType,
                this.memberId,
                this.participantRole,
                this.page,
                this.size
        );
    }

    public ScheduleSearchCondition between(Instant start, Instant end) {
        return this.from(start).to(end);
    }

    public ScheduleSearchCondition from(Instant from) {
        return new ScheduleSearchCondition(
                this.keyword,
                this.status,
                from,
                this.to,
                this.sortType,
                this.memberId,
                this.participantRole,
                this.page,
                this.size
        );
    }

    public ScheduleSearchCondition to(Instant to) {
        return new ScheduleSearchCondition(
                this.keyword,
                this.status,
                this.from,
                to,
                this.sortType,
                this.memberId,
                this.participantRole,
                this.page,
                this.size
        );
    }

    public ScheduleSearchCondition sort(ScheduleSortType scheduleSortType) {
        return new ScheduleSearchCondition(
                this.keyword,
                this.status,
                this.from,
                this.to,
                scheduleSortType,
                this.memberId,
                this.participantRole,
                this.page,
                this.size
        );
    }

    public ScheduleSearchCondition memberId(String memberId) {
        return new ScheduleSearchCondition(
                this.keyword,
                this.status,
                this.from,
                this.to,
                this.sortType,
                memberId,
                this.participantRole,
                this.page,
                this.size
        );
    }
}
