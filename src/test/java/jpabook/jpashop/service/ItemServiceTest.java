package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.service.ItemService;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;
    @Autowired EntityManager em;

    @Test
    @Rollback(false)
    public void 아이탬_저장() throws Exception {
        //given
        Item book = new Book();
        book.setName("테스트 책 1");
        itemService.saveItem(book);

        //when
        Item findBook = itemRepository.findOne(book.getId());

        //then
        em.flush();
        assertEquals(book.getName(), findBook.getName());
    }

}
