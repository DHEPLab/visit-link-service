package edu.stanford.fsi.reap.entity;

import static edu.stanford.fsi.reap.entity.enumerations.CurriculumBranch.MASTER;

import edu.stanford.fsi.reap.entity.enumerations.CurriculumBranch;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * @author hookszhang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = AbstractHistoryEntity.SKIP_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE curriculum_history SET deleted = true WHERE id = ?")
public class CurriculumHistory extends AbstractHistoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull @Size(max = 20)
  private String name;

  @NotNull @Size(max = 200)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(length = 10, nullable = false)
  private CurriculumBranch branch;

  @Builder.Default private boolean published = false;

  private Long sourceId;

  private Long historyId;

  @Column(name = "project_id")
  private Long projectId;

  public boolean draftBranch() {
    return CurriculumBranch.DRAFT.equals(branch);
  }

  public boolean publishedMasterBranch() {
    return MASTER.equals(branch) && published;
  }
}
