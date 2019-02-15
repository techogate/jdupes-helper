package hitsandmisses.matchers

import hitsandmisses.Matcher
import java.util.regex.Pattern

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
