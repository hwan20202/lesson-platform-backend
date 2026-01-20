-- 1. 제약 조건 체크 일시 해제
SET FOREIGN_KEY_CHECKS = 0;

-- 2. 카테고리 데이터 삽입
INSERT INTO categories (category_id, name, parent_category_id)
VALUES (2, '서비스 기획·운영', NULL),
       (3, '개발', NULL),
       (4, '디자인', NULL);

INSERT INTO categories (category_id, name, parent_category_id)
VALUES (10, '백엔드 개발자', 3),
       (9, '서버 개발자', 3),
       (16, 'UI/UX 디자이너', 4),
       (5, 'PM·PO', 2);

-- 3. 멤버 데이터 삽입
INSERT INTO members (member_id, nickname, phone_num, email, password, created_at, updated_at)
VALUES ('14', '마스터멘토', '010-1414-1414', 'mentor14@test.com', '1234', NOW(), NOW()),
       ('13', '열공멘티', '010-1313-1313', 'mentee13@test.com', '1234', NOW(), NOW());

-- 4. 레슨(Lessons) 데이터 삽입 (ID: 101~104)
INSERT INTO lessons (lesson_id, title, lesson_type, thumbnail_image, description, location, mentor_id, category_id,
                     price, created_at, updated_at)
VALUES ('101', '현직 서버 개발자의 1:1 백엔드 커피챗', 'MENTORING', 'coffee.png', '백엔드 취업 및 이직 상담', '온라인', '14', 10, 30000, NOW(),
        NOW()),
       ('102', '1:N 소규모 스터디: Spring Boot 실전', 'STUDY', 'spring_study.png', 'Spring 실무 활용법 정복', '오프라인(강남)', '14', 9,
        150000, NOW(), NOW()),
       ('103', 'UI/UX 디자이너를 위한 피그마 기초 클래스', 'ONEDAY', 'figma.png', '포트폴리오 완성반', '온라인', '14', 16, 80000, NOW(), NOW()),
       ('104', '1:N 소규모 클래스: 서비스 기획 입문', 'ONEDAY', 'pm_po.png', '신입 PM을 위한 역량 강화', '온라인', '14', 5, 120000, NOW(),
        NOW());

-- 5. 옵션
INSERT INTO options (option_id, lesson_id, name, minute, price, created_at, updated_at)
VALUES ('1', '101', '기본상담 60분', 60, 30000, NOW(), NOW()),
       ('2', '102', '주말 집중 4주 과정', 240, 150000, NOW(), NOW()),
       ('3', '103', '저녁 클래스 120분', 120, 80000, NOW(), NOW()),
       ('4', '104', '원데이 특강 180분', 180, 120000, NOW(), NOW());

-- 6. 스케줄 및 상세 시간 데이터 삽입 (ID: 10 이하 제약 반영 -> 1~5)

-- [과거 데이터 1]
INSERT INTO schedules (schedule_id, lesson_id, option_id, mentee_id, lesson_mentor_id, mentor_nickname, status,
                       lesson_title, lesson_type, lesson_description, lesson_location, lesson_category_name,
                       option_name, option_minute, price, created_at, updated_at)
VALUES ('1', '101', '1', '13', '14', '마스터멘토', 'APPROVED', '현직 서버 개발자의 1:1 백엔드 커피챗', 'MENTORING', '상담', '온라인', '백엔드 개발자',
        '기본상담', 60, 30000, NOW(), NOW());
INSERT INTO schedule_times(schedule_time_id, schedule_id, start_time, end_time)
VALUES (UUID(), '1', '2026-01-05 10:00:00', '2026-01-05 11:00:00');

-- [과거 데이터 2]
INSERT INTO schedules (schedule_id, lesson_id, option_id, mentee_id, lesson_mentor_id, mentor_nickname, status,
                       lesson_title, lesson_type, lesson_description, lesson_location, lesson_category_name,
                       option_name, option_minute, price, created_at, updated_at)
VALUES ('2', '102', '2', '13', '14', '마스터멘토', 'APPROVED', '1:N 소규모 스터디: Spring Boot 실전', 'STUDY', '스터디', '강남', '서버 개발자',
        '주말 집중', 240, 150000, NOW(), NOW());
INSERT INTO schedule_times(schedule_time_id, schedule_id, start_time, end_time)
VALUES (UUID(), '2', '2026-01-15 14:00:00', '2026-01-15 18:00:00');

-- [오늘/캘린더 데이터]
INSERT INTO schedules (schedule_id, lesson_id, option_id, mentee_id, lesson_mentor_id, mentor_nickname, status,
                       lesson_title, lesson_type, lesson_description, lesson_location, lesson_category_name,
                       option_name, option_minute, price, created_at, updated_at)
VALUES ('3', '103', '3', '13', '14', '마스터멘토', 'APPROVED', 'UI/UX 디자이너를 위한 피그마 클래스', 'ONEDAY', '클래스', '온라인',
        'UI/UX 디자이너', '저녁 클래스', 120, 80000, NOW(), NOW());
INSERT INTO schedule_times(schedule_time_id, schedule_id, start_time, end_time)
VALUES (UUID(), '3', '2026-02-20 19:00:00', '2026-02-20 21:00:00');

-- [예정 데이터 1]
INSERT INTO schedules (schedule_id, lesson_id, option_id, mentee_id, lesson_mentor_id, mentor_nickname, status,
                       lesson_title, lesson_type, lesson_description, lesson_location, lesson_category_name,
                       option_name, option_minute, price, created_at, updated_at)
VALUES ('4', '104', '4', '13', '14', '마스터멘토', 'APPROVED', '1:N 소규모 클래스: 서비스 기획 입문', 'ONEDAY', '클래스', '온라인', 'PM·PO',
        '원데이 특강', 180, 120000, NOW(), NOW());
INSERT INTO schedule_times(schedule_time_id, schedule_id, start_time, end_time)
VALUES (UUID(), '4', '2026-03-01 10:00:00', '2026-03-01 13:00:00');

-- [예정 데이터 2]
INSERT INTO schedules (schedule_id, lesson_id, option_id, mentee_id, lesson_mentor_id, mentor_nickname, status,
                       lesson_title, lesson_type, lesson_description, lesson_location, lesson_category_name,
                       option_name, option_minute, price, created_at, updated_at)
VALUES ('5', '101', '1', '13', '14', '마스터멘토', 'APPROVED', '멘토링 재예약: 커피챗', 'MENTORING', '상담', '온라인', '백엔드 개발자', '기본상담',
        60, 30000, NOW(), NOW());
INSERT INTO schedule_times(schedule_time_id, schedule_id, start_time, end_time)
VALUES (UUID(), '5', '2026-04-10 15:00:00', '2026-04-10 16:00:00');

-- 7. 제약 조건 체크 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;