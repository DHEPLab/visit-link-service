package edu.stanford.fsi.reap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stanford.fsi.reap.entity.enumerations.FamilyTies;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = AbstractHistoryEntity.SKIP_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE carer_history SET deleted = true WHERE id = ?")
public class CarerHistory extends AbstractHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 10)
    private String name;

    @NotNull
    @Size(max = 20)
    private String phone;

    @Size(max = 20)
    private String wechat;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FamilyTies familyTies;

    /**
     * A baby can only have one master carer
     */
    @Builder.Default
    @Column(name = "master_carer", nullable = false)
    private boolean master = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Baby baby;

    private Long historyId;

    @Column(name = "project_id")
    private Long projectId;

    @Override
    public String toString() {
        return "Carer{"
                + "id="
                + id
                + ", name='"
                + name
                + '\''
                + ", phone='"
                + phone
                + '\''
                + ", wechat='"
                + wechat
                + '\''
                + ", familyTies="
                + familyTies
                + ", master="
                + master
                + '}';
    }
}
