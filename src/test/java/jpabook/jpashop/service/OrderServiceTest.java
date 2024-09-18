package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;

    @Autowired OrderService orderService;

    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문_테스트() throws Exception {
        //given
        Member member = createMember();


        Book book = createBook("Clean Code", 10000, 10);

        int orderCount = 2;
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야한다.");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(8, book.getStockQuantity(),"주문 수량만큼 재고가 줄어야 한다.");
    }


    @Test()
    public void 상품주문_재고수량초과_테스트() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("Clean Code", 10000, 10);

        int orderCount = 11;
        
        //when & then
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), orderCount);
        });
    }

    @Test
    public void 주문취소_테스트() throws Exception {
        //given
        // 멤버, 아이템 생성
        Member member = createMember();
        Book item = createBook("Clean Code", 10000, 10);
        int orderCount = 2;

        // 주문
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);


        //when
        // 주문취소
        orderService.cancelOrder(orderId);

        //then
        // 주문상태가 주문취소로 변경됐는지?
        // 아이템 재고가 처음에 생성한 재고수 만큼인지?
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL 이다.");
        assertEquals(10, item.getStockQuantity(), "상품 개수는 주문이 취소된 상품 개수만큼 다시 증가해야한다.");
    }


    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "봉천동", "123-123"));
        em.persist(member);
        return member;
    }
}