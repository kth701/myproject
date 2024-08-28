package com.spring.myproject.blog.repository;

import com.spring.myproject.blog.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
