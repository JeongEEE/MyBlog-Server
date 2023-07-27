package me.gjkim.myblog.service;

import lombok.RequiredArgsConstructor;
import me.gjkim.myblog.domain.Member;
import me.gjkim.myblog.dto.MemberJoinRequestDto;
import me.gjkim.myblog.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;


  public Member signup(MemberJoinRequestDto dto) {
    String encryptPassword = bCryptPasswordEncoder.encode(dto.getPassword());
    Member member = dto.toEntity(encryptPassword);
    member = memberRepository.save(member);
    return member;
  }

  public Member findById(Long userId) {
    return memberRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
  }
}
