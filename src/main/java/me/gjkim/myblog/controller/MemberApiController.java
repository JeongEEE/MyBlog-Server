package me.gjkim.myblog.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import me.gjkim.myblog.config.jwt.JwtProperties;
import me.gjkim.myblog.config.jwt.JwtTokenUtil;
import me.gjkim.myblog.domain.Member;
import me.gjkim.myblog.dto.MemberJoinRequestDto;
import me.gjkim.myblog.dto.ResponseDto;
import me.gjkim.myblog.dto.auth.JwtRequestDto;
import me.gjkim.myblog.dto.auth.JwtResponseDto;
import me.gjkim.myblog.service.MemberService;
import me.gjkim.myblog.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

  private final MemberService memberService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;

  private static final Logger log = LoggerFactory.getLogger(Log.class);

  @PostMapping("/member/signup")
  public ResponseEntity<ResponseDto> join(@Valid @RequestBody MemberJoinRequestDto params) {
    log.info("### member join invoked. requestDto. username:" + params.getUsername());
    ResponseDto responseDto = ResponseDto.makeSuccessResponseStatus();
    HttpStatus responseStatus = HttpStatus.OK;
    try {
      Member member = memberService.signup(params);
      log.info("### member join Success, username:" + params.getUsername());
    } catch (DataIntegrityViolationException e) {
      log.error("### member join DataIntegrityViolationException:" + e.getMessage());
      responseDto = ResponseDto.builder()
              .msg(RequestUtil.MEMBER_SIGNUP_INFO_INVALID_MSG)
              .code(RequestUtil.MEMBER_SIGNUP_INFO_INVALID_CODE)
              .build();
      responseStatus = HttpStatus.BAD_REQUEST;
    }
    return new ResponseEntity<>(responseDto, responseStatus);
  }

  @GetMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    new SecurityContextLogoutHandler().logout(request, response,
            SecurityContextHolder.getContext().getAuthentication());
    return "redirect:/login";
  }

  @PostMapping("/member/login")
  public ResponseEntity<ResponseDto> login(@Valid @RequestBody JwtRequestDto params) {
    log.info("### authenticate invoked. username:" + params.getUsername());
    String username = params.getUsername();
    String password = params.getPassword();

    ResponseDto responseDto;
    HttpStatus httpStatus = HttpStatus.OK;
    try {
      // username, password 를 사용해 인증토큰 생성, JwtUserDetailsService 사용
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
      // 인증 및 토큰정보 생성
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();

      log.info("#### username = "+ userDetails.getUsername());
      String token = jwtTokenUtil.generateToken(userDetails);
      log.info("#### tokenData = "+ jwtTokenUtil.getAllClaimsFromToken(token));
      responseDto = new JwtResponseDto(
              jwtTokenUtil.getAllClaimsFromToken(token),
              JwtProperties.TOKEN_PREFIX + jwtTokenUtil.generateToken(userDetails),
              LocalDateTime.now());
    } catch (Exception e) {
      log.error("### authenticate userinfo BadCredentialsException, username:" + username + ", Error:" + e.getMessage());
      responseDto = ResponseDto.builder()
              .msg(RequestUtil.MEMBER_LOGIN_INFO_INVALID_MSG)
              .code(RequestUtil.MEMBER_LOGIN_INFO_INVALID_CODE)
              .build();
      httpStatus = HttpStatus.BAD_REQUEST;
    }
    return new ResponseEntity<>(responseDto, httpStatus);
  }
}
