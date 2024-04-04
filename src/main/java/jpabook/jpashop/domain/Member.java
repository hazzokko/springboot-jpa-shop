package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // 내장 타입을 포함 (Embedded나 Embeddable 둘 중 하나만 있으면 되지만 명시적으로 2개 다 해주는 경우가 많다)
    private Address address;

    @OneToMany(mappedBy = "member") // Order 테이블에 있는 member 필드에 의해서 매핑 (여기에 값을 넣는다고 FK값이 변경되지 X)
    private List<Order> orders = new ArrayList<>();
}
