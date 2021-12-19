package com.jhr.algoNote.repository;

import com.jhr.algoNote.domain.Member;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member  m ", Member.class)
            .getResultList();
    }

    /**
     * 이메일로 조회
     *
     * @return nullable Optional
     */
    public Optional<Member> findByEmail(String email) {

        List<Member> results = em.createQuery("select  m from Member  m where  m.email = :email",
                Member.class)
            .setParameter("email", email)
            .getResultList();

        return Optional.ofNullable(results.isEmpty() ? null : results.get(0));
    }


}
