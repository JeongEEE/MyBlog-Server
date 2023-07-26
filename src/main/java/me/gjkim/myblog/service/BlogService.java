package me.gjkim.myblog.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.gjkim.myblog.domain.Article;
import me.gjkim.myblog.dto.AddArticleRequest;
import me.gjkim.myblog.dto.UpdateArticleRequest;
import me.gjkim.myblog.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {
  private final BlogRepository blogRepository;

  public Article save(AddArticleRequest request, String author) {
    return blogRepository.save(request.toEntity(author));
  }

  public List<Article> findAll() {
    return blogRepository.findAll();
  }

  public Article findById(long id) {
    return blogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
  }

  public void delete(long id) {
    blogRepository.deleteById(id);
  }

  @Transactional
  public Article update(long id, UpdateArticleRequest request) {
    Article article = blogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

    article.update(request.getTitle(), request.getContent());

    return article;
  }
}
