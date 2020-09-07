package matchesandmisses.processors

import matchesandmisses.MatcherFactory
import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options
import matchesandmisses.PatternMode
import spock.lang.Specification

class SafePreserveAllMatchesWithDupeSpec extends Specification  {
  Options options

  private MatchesAndMisses exec(Set<String> group, String pattern, PatternMode patternMode,
                                String pairPattern, PatternMode pairPatternMode) {
    SafePreserveAllMatchesWithDupe underTest = new SafePreserveAllMatchesWithDupe(
            group, options,
            MatcherFactory.createMatcher(patternMode, pattern),
            MatcherFactory.createMatcher(pairPatternMode, pairPattern)
    )
    underTest.processGroup()
  }

  def setup() {
    options = Mock(Options)
  }

  def "preserve none with dupe"() { // no matches for preserving, = > all marked for deletion, but in the Safe impl this is reset
    given:
    String lookFor = 'aaa'
    String dupe = 'bbb'
    Set<String> group = ['bbbb', 'bbb', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.contains)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == group
  }

  def "preserve none with no dupe"() { // no matches for preserving, = > all marked for deletion, but in the Safe impl this is reset
    given:
    String lookFor = 'aaa'
    String dupe = 'xxx'
    Set<String> group = ['bbbb', 'bbb', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.contains)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == group
  }

  def "preserve one with dupe"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'bbb'
    Set<String> group = ['aaa', 'bbb', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.contains)
    expect:
    results.forDelete == ['bbb', 'ccc'] as Set<String>
    results.forKeeps == ['aaa'] as Set<String>
  }

  def "preserve one with no dupe"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'bbbc'
    Set<String> group = ['aaa', 'bbb', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.contains)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == group
  }

  def "preserve multi with dupe"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'bbb'
    Set<String> group = ['aaa', 'bbb', 'aAaccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.icontains, dupe, PatternMode.icontains)
    expect:
    results.forDelete == ['bbb'] as Set<String>
    results.forKeeps == ['aaa', 'aAaccc'] as Set<String>
  }

  def "preserve multi with no dupe"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'bbbx'
    Set<String> group = ['aaa', 'bbb', 'aAaccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains, dupe, PatternMode.icontains)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == group
  }

  def "preserve all with no dupe"() {
    given:
    String lookFor = 'aaa'
    String dupe = 'bbb'
    Set<String> group = ['aaa', 'bbaaa', 'aAaccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.icontains, dupe, PatternMode.icontains)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == group
  }
}

