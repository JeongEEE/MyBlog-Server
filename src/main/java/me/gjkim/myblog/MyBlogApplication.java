package me.gjkim.myblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 참조 github
// https://github.com/shinsunyoung/springboot-developer

@EnableJpaAuditing
@SpringBootApplication
public class MyBlogApplication {
  public static void main(String[] args) {
    SpringApplication.run(MyBlogApplication.class, args);
  }
}
