package jpabook.jpashop.domain.item;

import jpabook.jpashop.exception.NotEnoughStockException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void 재고_추가_테스트() {
        //given
        Item item = new Book();
        item.setName("Clean Code");
        item.addStock(10);

        //when
        item.addStock(5);

        //then
        assertThat(item.getStockQuantity()).isEqualTo(15);
    }


    @Test
    void 재고_감소_테스트() {
        Item item = new Book();
        item.setName("Clean Code");
        item.addStock(10);

        //when
        item.removeStock(9);

        //then
        assertThat(item.getStockQuantity()).isEqualTo(1);
    }

    @Test
    public void 재고부족_예외처리_테스트() {
        //given
        Item item = new Book();
        item.setName("Clean Code");
        item.addStock(10); //10개

        //when & then
        assertThatThrownBy(() -> item.removeStock(11)) //11개
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage("재고가 부족합니다.");
    }
}