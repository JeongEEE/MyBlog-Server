package me.gjkim.myblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.gjkim.myblog.config.jwt.JwtFactory;
import me.gjkim.myblog.config.jwt.JwtProperties;
import me.gjkim.myblog.domain.Member;
import me.gjkim.myblog.domain.RefreshToken;
import me.gjkim.myblog.dto.CreateAccessTokenRequest;
import me.gjkim.myblog.repository.RefreshTokenRepository;
import me.gjkim.myblog.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenApiControllerTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  JwtProperties jwtProperties;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  RefreshTokenRepository refreshTokenRepository;

  @BeforeEach
  public void mockMvcSetUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .build();
    memberRepository.deleteAll();
  }

  @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
  @Test
  public void createNewAccessToken() throws Exception {
    // given
    final String url = "/api/token";

    Member testMember = memberRepository.save(Member.builder()
            .username("user@gmail.com")
            .password("test")
            .build());

    String refreshToekn = JwtFactory.builder()
            .claims(Map.of("id", testMember.getId()))
            .build()
            .createToken(jwtProperties);

    refreshTokenRepository.save(new RefreshToken(testMember.getId(), refreshToekn));

    CreateAccessTokenRequest request = new CreateAccessTokenRequest();
    request.setRefreshToken(refreshToekn);
    final String requestBody = objectMapper.writeValueAsString(request);

    // when
    ResultActions resultActions = mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody));

    // then
    resultActions
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.accessToken").isNotEmpty());
  }
}
