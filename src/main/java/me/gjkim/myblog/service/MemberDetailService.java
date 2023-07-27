package me.gjkim.myblog.service;

import lombok.RequiredArgsConstructor;
import me.gjkim.myblog.domain.Member;
import me.gjkim.myblog.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Override
  public Member loadUserByUsername(String username) {
    return memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException(username));
  }
}
