package ru.javaops.topjava.to;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.javaops.topjava.HasIdAndEmail;
import ru.javaops.topjava.util.validation.NoHtml;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo implements HasIdAndEmail {
    @Email
    @NotBlank
    @Size(max = 128)
    @NoHtml  // https://stackoverflow.com/questions/17480809
    String email;

    public RestaurantTo(Integer id, String name, String email) {
        super(id, name);
        this.email = email;
    }

    @Override
    public String toString() {
        return "RestaurantTo:" + id + '[' + email + ']';
    }
}
