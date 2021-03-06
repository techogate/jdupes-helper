package matchesandmisses.matchers

import matchesandmisses.Matcher

class Contains implements Matcher {
  String pattern

  Contains(String pattern) {
    this.pattern = pattern
  }

  @Override
  boolean match(String string) {
    return string.contains(pattern)
  }
}
