package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
//    @Rollback(false)
    public void 회원가입_테스트() throws Exception {
        //given
        Member member = new Member();
        member.setName("park");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));

    }


    @Test()
    public void 중복_회원_예외처리_테스트() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("PARK");

        Member member2 = new Member();
        member2.setName("PARK");

        //when
        memberService.join(member1);

        IllegalStateException exception = assertThrows(IllegalStateException.class , () -> {
            memberService.join(member2);
        });


        //then
        assertThat(exception.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @Test
    public void 전체_멤버_조회_테스트() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("PARK");

        Member member2 = new Member();
        member2.setName("YONG");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberService.findMembers();

        //then
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    public void 회원_단일_조회() throws Exception {
        //given
        Member member = new Member();
        member.setName("PARK");

        memberRepository.save(member);
        //when
        Member findMember =  memberService.findOne(member.getId());

        //then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getName()).isEqualTo("PARK");

    }

}