package com.yumyumcoach.domain.exercise.service;

import com.yumyumcoach.domain.ai.event.ExerciseReviewRequestedEvent;
import com.yumyumcoach.domain.ai.service.AiBackgroundJobService;
import com.yumyumcoach.domain.exercise.dto.*;
import com.yumyumcoach.domain.exercise.entity.Exercise;
import com.yumyumcoach.domain.exercise.entity.ExerciseRecord;
import com.yumyumcoach.domain.exercise.entity.ExerciseRecordWithExercise;
import com.yumyumcoach.domain.exercise.mapper.ExerciseMapper;
import com.yumyumcoach.domain.exercise.mapper.ExerciseRecordMapper;
import com.yumyumcoach.domain.user.entity.Profile;
import com.yumyumcoach.domain.user.mapper.ProfileMapper;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ExerciseService {
    private final ExerciseMapper exerciseMapper;
    private final ExerciseRecordMapper exerciseRecordMapper;
    private final ProfileMapper profileMapper;
    private final ApplicationEventPublisher eventPublisher;

    private static final int MIN_KEYWORD_LENGTH = 2;
    private static final int SIZE_LIMIT = 10;
    private static final int SIZE_DEFAULT = 5;

    public List<ExerciseResponse> getExercises() {
        return exerciseMapper.findAll().stream()
                .map(this::toExerciseResponse)
                .toList();
    }

    public List<ExerciseRecordResponse> getMyExerciseRecords(String email, LocalDate recordDate) {
        return exerciseRecordMapper.findByEmailAndDate(email, recordDate).stream()
                .map(this::toExerciseRecordResponse)
                .toList();
    }

    public ExerciseRecordResponse getMyExerciseRecordDetail(String email, Long recordId) {
        checkRecordOwnerOrThrow(email, recordId);

        ExerciseRecordWithExercise record = exerciseRecordMapper.findDetailByIdAndEmail(recordId, email);
        if (record == null) {
            throw new BusinessException(ErrorCode.EXERCISE_RECORD_NOT_FOUND);
        }
        return toExerciseRecordResponse(record);
    }

    @Transactional
    public List<ExerciseRecordResponse> createMyExerciseRecords(String email, List<ExerciseRecordRequest> requests) {
        List<ExerciseRecordResponse> result = requests.stream()
                .map(req -> createExerciseRecord(email, req))
                .toList();

        LocalDate anchor = requests.get(requests.size() - 1).getRecordedAt().toLocalDate();
        eventPublisher.publishEvent(new ExerciseReviewRequestedEvent(email, anchor));

        return result;
    }

    @Transactional
    public ExerciseRecordResponse updateMyExerciseRecord(String email, Long recordId, ExerciseRecordRequest request) {
        checkRecordOwnerOrThrow(email, recordId);

        double calories = calculateCalories(email, request.getExerciseId(), request.getDurationMinutes());

        ExerciseRecord exerciseRecord = ExerciseRecord.builder()
                .id(recordId)
                .email(email)
                .exerciseId(request.getExerciseId())
                .recordedAt(request.getRecordedAt())
                .durationMinutes(request.getDurationMinutes())
                .calories(calories)
                .build();

        exerciseRecordMapper.update(exerciseRecord);

        LocalDate anchor = request.getRecordedAt().toLocalDate();
        eventPublisher.publishEvent(new ExerciseReviewRequestedEvent(email, anchor));
        return getMyExerciseRecordDetail(email, recordId);
    }

    @Transactional
    public DeleteExerciseRecordResponse deleteMyExerciseRecord(String email, Long recordId) {
        checkRecordOwnerOrThrow(email, recordId);
        ExerciseRecordWithExercise before = exerciseRecordMapper.findDetailByIdAndEmail(recordId, email);
        if (before == null) throw new BusinessException(ErrorCode.EXERCISE_RECORD_NOT_FOUND);
        exerciseRecordMapper.delete(recordId, email);

        LocalDate anchor = before.getRecordedAt().toLocalDate();
        eventPublisher.publishEvent(new ExerciseReviewRequestedEvent(email, anchor));
        return DeleteExerciseRecordResponse.builder()
                .recordId(recordId)
                .deleted(true)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    // -------------------------
    // 비즈니스 예외(403/404)만
    // -------------------------
    private void checkRecordOwnerOrThrow(String email, Long recordId) {
        String ownerEmail = exerciseRecordMapper.findEmailByRecordId(recordId);

        if (ownerEmail == null) {
            throw new BusinessException(ErrorCode.EXERCISE_RECORD_NOT_FOUND);
        }
        if (!ownerEmail.equalsIgnoreCase(email)) {
            throw new BusinessException(ErrorCode.EXERCISE_RECORD_FORBIDDEN);
        }
    }

    private double calculateCalories(String email, Long exerciseId, Double durationMinutes) {
        Exercise exercise = exerciseMapper.findById(exerciseId);
        if (exercise == null) {
            throw new BusinessException(ErrorCode.EXERCISE_NOT_FOUND);
        }

        Profile profile = profileMapper.findByEmail(email);
        if (profile == null) {
            throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);
        }
        Double currentWeight = profile.getCurrentWeight();
        if (currentWeight == null) {
            // 몸무게가 지정이 안되어있으면 기본값인 60으로 계산
            currentWeight = 60.0;
        }

        double durationHours = durationMinutes / 60.0;
        double rawCalories = exercise.getMet() * currentWeight * durationHours;

        return BigDecimal.valueOf(rawCalories)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private ExerciseResponse toExerciseResponse(Exercise exercise) {
        return ExerciseResponse.builder()
                .exerciseId(exercise.getId())
                .name(exercise.getName())
                .met(exercise.getMet())
                .intensityLevel(exercise.getIntensityLevel())
                .type(exercise.getType())
                .description(exercise.getDescription())
                .build();
    }

    private ExerciseRecordResponse toExerciseRecordResponse(ExerciseRecordWithExercise record) {
        return ExerciseRecordResponse.builder()
                .recordId(record.getId())
                .exerciseId(record.getExerciseId())
                .exerciseName(record.getExerciseName())
                .met(record.getMet())
                .intensityLevel(record.getIntensityLevel())
                .type(record.getType())
                .recordedAt(record.getRecordedAt())
                .durationMinutes(record.getDurationMinutes())
                .calories(record.getCalories())
                .build();
    }

    private ExerciseRecordResponse createExerciseRecord(String email, ExerciseRecordRequest request) {
        double calories = calculateCalories(email, request.getExerciseId(), request.getDurationMinutes());

        ExerciseRecord exerciseRecord = ExerciseRecord.builder()
                .email(email)
                .exerciseId(request.getExerciseId())
                .recordedAt(request.getRecordedAt())
                .durationMinutes(request.getDurationMinutes())
                .calories(calories)
                .build();

        exerciseRecordMapper.insert(exerciseRecord);
        return getMyExerciseRecordDetail(email, exerciseRecord.getId());
    }

    // 운동 검색 기능
    @Transactional(readOnly = true)
    public SearchResponse searchExercise(String keyword, int page, int size) {

        // 파라미터 예외처리 및 스케일링
        keyword = validateKeyword(keyword);
        page = Math.max(0, page);
        size = normalizeSize(size);

        //offset 설정
        int offset = page * size;

        // 현재 페이지 결과 조회
        List<SearchDto> searchResult = exerciseMapper.searchExercise(keyword, size, offset);

        // 전체 개수 조회
        int total = exerciseMapper.countExercises(keyword);

        return SearchResponse.builder()
                .page(page)
                .size(size)
                .total(total)
                .result(searchResult)
                .build();
    }

    private static int normalizeSize(int size) {
        if (size <= 0) {
            size = SIZE_DEFAULT;
        }
        else if (size > SIZE_LIMIT) {
            size = SIZE_LIMIT;
        }

        return size;
    }

    private static String validateKeyword(String keyword) {
        if (keyword == null || keyword.trim().length() < MIN_KEYWORD_LENGTH) {
            throw new BusinessException(ErrorCode.EXERCISE_INVALID_KEYWORD);
        }
        keyword = keyword.trim();

        return keyword;
    }
}

