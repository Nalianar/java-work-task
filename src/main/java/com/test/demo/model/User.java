package com.test.demo.model;

import com.test.demo.dto.UserDto;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;

    private String password;

    @Column(unique = true)
    private String token;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy="user", fetch=FetchType.EAGER)
    private Set<Card> cards;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<UserGroup> groups;

    public static UserDto toDto(User user){
        return UserDto.builder()
                .id(user.id)
                .numOfCards(user.cards.size())
                .login(user.login)
                .build();
    }

}
