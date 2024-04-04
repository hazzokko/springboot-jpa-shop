package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id") // id로 해도 되지만 DBA가 order_id를 더 선호해서 맞춘다
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id") // 매핑할 FK 이름 지정, 여기에 값을 세팅하면 member_id FK 값이 변경된다.
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문 시간, Date를 사용하면 날짜 관련 어노테이션 매핑이 필요, JDK8에서는 LocalDateTime을 사용하면 하이버네이트가 자동 지원

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 -> ORDER, CANCEL
}
