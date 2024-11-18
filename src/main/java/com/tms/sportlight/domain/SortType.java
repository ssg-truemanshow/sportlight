package com.tms.sportlight.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {

  POPULARITY("인기순"),
  NEWEST("최신순"),
  RATING("별점순"),
  REVIEW_COUNT("리뷰순"),
  DISTANCE("거리순");

  private final String value;
}
