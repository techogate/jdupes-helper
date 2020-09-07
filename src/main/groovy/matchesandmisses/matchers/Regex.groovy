package matchesandmisses.matchers

import matchesandmisses.Matcher

class Regex implements Matcher {
  String pattern

  Regex(String pattern) {
    this.pattern = pattern
  }

  @Override
  boolean match(String string) {
    return (string =~ /$pattern/ )
  }
}
