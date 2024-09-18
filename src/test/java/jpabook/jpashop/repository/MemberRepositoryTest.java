package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    

    @Test
    public void 멤버레포_저장_테스트() {
        //given
        Member member = new Member();
        member.setName("PARK");

        //when
        memberRepository.save(member);

        Member findMember = memberRepository.findOne(member.getId());

        //then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getName()).isEqualTo("PARK");
    }

    @Test
    public void 멤버레포_전체조회_테스트() {
        //given
        Member member1 = new Member();
        member1.setName("PARK");

        Member member2 = new Member();
        member2.setName("YONG");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findAll();

        //then
        assertThat(members.size()).isEqualTo(2);
        assertThat(members).extracting("name").containsExactlyInAnyOrder("PARK", "YONG");
    }

    @Test
    public void 멤버레포_이름으로_조회_테스트() {
        //given
        Member member1 = new Member();
        member1.setName("PARK");

        Member member2 = new Member();
        member2.setName("YONG");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findByName("PARK");

        //then
        assertThat(members).hasSize(1);
        assertThat(members.get(0).getName()).isEqualTo("PARK");

    }
}