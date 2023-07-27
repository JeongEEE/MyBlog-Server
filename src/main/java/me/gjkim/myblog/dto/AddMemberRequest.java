package me.gjkim.myblog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {
  private String username;
  private String password;
}
