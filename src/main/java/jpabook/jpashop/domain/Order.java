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
    private Member member; // 주문 회원

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송 정보

    private LocalDateTime orderDate; // 주문 시간, Date를 사용하면 날짜 관련 어노테이션 매핑이 필요, JDK8에서는 LocalDateTime을 사용하면 하이버네이트가 자동 지원

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 -> ORDER, CANCEL

    // 연관관계 편의 메서드 (양방향일 때 넣어주고, 위치는 핵심적으로 컨트롤하는 쪽에 만든다)
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this); // 다대일(Many-to-One) 관계에서 사용, 컬렉션(Member가 가지고 있는 Orders 목록)에 현재 객체를 추가
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this); // 일대다(One-to-Many)와 일대일(One-to-One) 관계에서 사용, 하나의 객체에만 값을 설정
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메서드 //
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직 //
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // 조회 로직 //
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

//    위의 로직을 람다식으로 변경한 예시
//    public int getTotalPrice() {
//        return orderItems.stream()
//                .mapToInt(OrderItem::getTotalPrice)
//                .sum();
//    }
}
