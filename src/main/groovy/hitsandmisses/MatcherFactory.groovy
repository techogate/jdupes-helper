package hitsandmisses

import hitsandmisses.matchers.Contains
import hitsandmisses.matchers.Regex
import hitsandmisses.matchers.StartsWith

class MatcherFactory {
  private MatcherFactory() {
  }

  static Matcher createMatcher(PatternMode patternMode, String pattern) {
    switch (patternMode) {
      case PatternMode.contains:
        return new Contains(pattern)
        break
      case PatternMode.startsWith:
        return new StartsWith(pattern)
        break
      case PatternMode.regex:
        return new Regex(pattern)
      default:
        assert ('No such Pattern')
    }

  }
}
