package edu.stanford.fsi.reap.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = AbstractNormalEntity.SKIP_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE visit_position_record SET deleted = true WHERE id = ?")
public class VisitPositionRecord extends AbstractNormalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long visitId;

    private Long babyId;

    private Double longitude;

    private Double latitude;

    private Double distance;

}
