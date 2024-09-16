package jpabook.jpashop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    

    @Test
    @Transactional
//    @Rollback(false)
    public void memberTest() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("A");

        //when
        long savedId = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedId);

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
        
    }
}