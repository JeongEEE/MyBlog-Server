package me.gjkim.myblog.repository;

import me.gjkim.myblog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByUsername(String username);
}
