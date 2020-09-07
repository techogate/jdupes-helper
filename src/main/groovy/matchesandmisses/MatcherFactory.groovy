package matchesandmisses

import matchesandmisses.matchers.Contains
import matchesandmisses.matchers.ContainsIgnoreCase
import matchesandmisses.matchers.Regex
import matchesandmisses.matchers.StartsWith

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
        break
      case PatternMode.icontains:
        return new ContainsIgnoreCase(pattern)
        break
      default:
        assert ('No such Pattern')
    }
  }
}
