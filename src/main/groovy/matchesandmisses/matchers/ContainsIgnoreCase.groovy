package matchesandmisses.matchers

import matchesandmisses.Matcher

class ContainsIgnoreCase implements Matcher {
  String pattern

  ContainsIgnoreCase(String pattern) {
    this.pattern = pattern
  }

  @Override
  boolean match(String string) {
    return string.toUpperCase().contains(pattern.toUpperCase())
  }
}
