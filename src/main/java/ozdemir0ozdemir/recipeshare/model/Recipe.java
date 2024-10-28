package ozdemir0ozdemir.recipeshare.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @SequenceGenerator(name = "recipes_id_gen", sequenceName = "recipes_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "recipes_id_gen", strategy = GenerationType.SEQUENCE)
    @Column(name = "recipe_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "is_vegetarian")
    private boolean vegetarian;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", updatable = false, nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = true, nullable = true)
    private LocalDateTime updatedAt;
}
