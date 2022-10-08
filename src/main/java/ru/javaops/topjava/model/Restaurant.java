package ru.javaops.topjava.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.topjava.HasIdAndEmail;
import ru.javaops.topjava.util.validation.NoHtml;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity implements HasIdAndEmail, Serializable {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 128)
    @NoHtml   // https://stackoverflow.com/questions/17480809
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
//    @Schema(hidden = true)
    private List<Dish> dishes;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "votes")
//    @Schema(hidden = true)
    private List<User> user;

    public Restaurant(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;

    }

    @Override
    public String toString() {
        return "Restaurant:" + id + '[' + email + ']';
    }
}
