package ru.javaops.topjava.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity implements HasIdAndEmail, Serializable {
//public class Restaurant extends NamedEntity {

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

//    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
//    @JoinTable(name = "votes",
//            joinColumns = {@JoinColumn(name = "restaurant_id", referencedColumnName = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
//    )
////    @Schema(hidden = true)
////    @JsonIgnore
    @OneToMany(mappedBy = "restaurant")
    private List<User> users;

    public Restaurant(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
//        this.dishes = new ArrayList<>();
    }

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.email, r.dishes);
    }

    public Restaurant(Integer id, String name, String email, List<Dish> dishes) {
        super(id, name);
        this.email = email;
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "Restaurant:" + id + '[' + email + ']';
    }
}
