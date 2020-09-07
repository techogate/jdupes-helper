package matchesandmisses.matchers

import matchesandmisses.Matcher

class StartsWith implements Matcher {
  String pattern

  StartsWith(String pattern) {
    this.pattern = pattern
  }

  @Override
  boolean match(String string) {
    return string.startsWith(pattern)
  }
}
