package edu.stanford.fsi.reap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stanford.fsi.reap.converter.DomainListConverter;
import edu.stanford.fsi.reap.entity.enumerations.BabyStage;
import edu.stanford.fsi.reap.pojo.Domain;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author hookszhang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = AbstractNormalEntity.SKIP_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE lesson_schedule_history SET deleted = true WHERE id = ?")
@Slf4j
public class LessonScheduleHistory extends AbstractNormalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 20)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private BabyStage stage;

    @NotNull
    private Integer startOfApplicableDays;

    @NotNull
    private Integer endOfApplicableDays;

    /**
     * Mapper value -> Lesson.id; label -> Lesson.number
     */
    @NotEmpty
    @Column(columnDefinition = "json", nullable = false)
    @Convert(converter = DomainListConverter.class)
    private List<Domain> lessons;

    @ManyToOne
    @JsonIgnore
    private Curriculum curriculum;

    @ManyToOne
    @JsonIgnore
    private LessonScheduleHistory source;

    private Long historyId;
    public boolean includes(int days) {
        boolean result = startOfApplicableDays <= days && endOfApplicableDays >= days;
        log.debug(
                "Match schedule id: {}, start: {}, end: {}, days: {}, result: {}",
                id,
                startOfApplicableDays,
                endOfApplicableDays,
                days,
                result);
        return result;
    }

    public LessonScheduleHistory copyFrom(LessonScheduleHistory other) {
        this.name = other.name;
        this.stage = other.stage;
        this.startOfApplicableDays = other.startOfApplicableDays;
        this.endOfApplicableDays = other.endOfApplicableDays;
        this.lessons = other.lessons;
        return this;
    }

    public boolean equalsId(LessonScheduleHistory other) {
        return id.equals(other.getId());
    }
}
