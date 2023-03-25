package com.ll.exam.sbb_exam;

import com.ll.exam.sbb_exam.base.RepositoryUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AnswerRepository extends JpaRepository<Answer, Integer>, RepositoryUtil {

  @Transactional
  @Modifying
  @Query(value = "truncate answer", nativeQuery = true)
  void truncate();
}
