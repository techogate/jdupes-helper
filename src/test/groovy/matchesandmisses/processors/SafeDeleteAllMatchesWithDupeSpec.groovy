package matchesandmisses.processors

import matchesandmisses.MatcherFactory
import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options
import matchesandmisses.PatternMode
import spock.lang.Specification

class SafeDeleteAllMatchesWithDupeSpec extends Specification  {
  Options options

  private MatchesAndMisses exec(Set<String> group,
                                String pattern, PatternMode patternMode,
                                String pairPattern, PatternMode pairPatternMode  ) {
    SafeDeleteAllMatchesWithDupe underTest =
            new SafeDeleteAllMatchesWithDupe(group, options,
                    MatcherFactory.createMatcher(patternMode, pattern),
                    MatcherFactory.createMatcher(pairPatternMode, pairPattern))
    underTest.processGroup()
  }

  def setup() {
    options = Mock(Options)
  }

  def "no matches With a dupe match"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'bbb'
    Set<String> group = ['bbbb', 'bbb', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.contains)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == ['bbbb', 'bbb', 'ccc'] as Set<String>
  }

  def "no matches Without a dupe match"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'eee'
    Set<String> group = ['bbbb', 'bbb', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.contains)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == ['bbbb', 'bbb', 'ccc'] as Set<String>
  }

  def "one match With a dupe match"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'bbb'
    Set<String> group = ['baaa', 'bbb', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.contains)
    expect:
    results.forDelete == ['baaa'] as Set<String>
    results.forKeeps == ['bbb', 'ccc'] as Set<String>
  }

  def "one match Without a dupe match"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'eee'
    Set<String> group = ['bbbb', 'bbb', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.contains)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == ['bbbb', 'bbb', 'ccc'] as Set<String>
  }

  def "multiple matches With a dupe match"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'bbb'
    Set<String> group = ['baaa', 'aaa', 'ccc', 'bbb']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.contains)
    expect:
    results.forDelete == ['baaa', 'aaa'].sort() as Set<String>
    results.forKeeps == ['bbb', 'ccc'].sort() as Set<String>
  }

  def "multiple matches Without a dupe match"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'bbb'
    Set<String> group = ['baaa', 'aaa', 'ccc', 'bb']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.contains)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == group
  }

  def "conflict match"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'bbb'
    Set<String> group = ['baaa', 'aaabbb', 'aaa']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.contains)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == group
  }

  def "all with no dupe match"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'bbb'
    Set<String> group = ['baaa', 'aaa', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.contains)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == group
  }


}

