package edu.stanford.fsi.reap.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE project SET deleted = true WHERE id = ?")
public class Project extends AbstractNormalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String code;

    @Column(name = "user_id")
    private Long userId;

    private int status;

}
