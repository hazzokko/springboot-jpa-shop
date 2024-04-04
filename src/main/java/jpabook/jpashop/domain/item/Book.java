package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B") // 값을 지정하지 않으면 클래스명으로 지정
@Getter @Setter
public class Book extends Item {

    private String author;
    private String isbn;
}
