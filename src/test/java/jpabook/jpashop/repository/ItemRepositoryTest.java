package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @Test
    void Item레포_저장_테스트() {
        //given
        Item item = new Book();  // Book으로 테스트
        item.setName("Clean Code");
        item.setPrice(10000);
        item.addStock(10);

        //when
        itemRepository.save(item);

        //then
        Item findItem = itemRepository.findOne(item.getId());
        assertThat(findItem).isNotNull();
        assertThat(findItem.getName()).isEqualTo("Clean Code");
        assertThat(findItem.getStockQuantity()).isEqualTo(10);
    }

    @Test
    void Item레포_전체조회_테스트() {
        //given
        Item item1 = new Book();
        item1.setName("Clean Code");
        item1.setPrice(10000);
        item1.addStock(10);

        Item item2 = new Book();
        item2.setName("DDD START");
        item2.setPrice(20000);
        item2.addStock(20);

        itemRepository.save(item1);
        itemRepository.save(item2);

        //when
        List<Item> items = itemRepository.findAll();

        //then
        assertThat(items.size()).isEqualTo(2);
        assertThat(items).extracting("name").containsExactlyInAnyOrder("Clean Code", "DDD START");

    }

    @Transactional
    void 재고_업데이트(Item itemParam) { //itemParam: 파리미터로 넘어온 준영속 상태의 엔티티
        Item findItem = em.find(Item.class, itemParam.getId()); //같은 엔티티를 조회한다.
        findItem.setPrice(itemParam.getPrice()); //데이터를 수정한다.
         }

}