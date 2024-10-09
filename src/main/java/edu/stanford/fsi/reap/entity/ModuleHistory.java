package edu.stanford.fsi.reap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.stanford.fsi.reap.converter.ComponentListConverter;
import edu.stanford.fsi.reap.entity.enumerations.CurriculumBranch;
import edu.stanford.fsi.reap.entity.enumerations.ModuleTopic;
import edu.stanford.fsi.reap.pojo.Component;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Curriculum Module
 *
 * @author hookszhang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = AbstractHistoryEntity.SKIP_DELETED_CLAUSE)
@SQLDelete(sql = "UPDATE module_history SET deleted = true WHERE id = ?")
public class ModuleHistory extends AbstractHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Uniquely identifies a module, different versions of the same module are allowed in the table.
     * The same module display only the most recent published module
     */
    @JsonIgnore
    @Column(length = 32, nullable = false)
    private String versionKey;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private CurriculumBranch branch;

    @Builder.Default
    private boolean published = false;

    @NotNull
    @Size(max = 40)
    private String name;

    @NotNull
    @Size(max = 20)
    private String number;

    @NotNull
    @Size(max = 200)
    private String description;

    @NotNull
    @Column(length = 30)
    private ModuleTopic topic;

    @Valid
    @NotEmpty
    @Column(columnDefinition = "json")
    @Convert(converter = ComponentListConverter.class)
    private List<Component> components;

    public boolean version(ModuleHistory other) {
        return versionKey.equals(other.getVersionKey());
    }

    private Long historyId;

    @Column(name = "project_id")
    private Long projectId;
}
