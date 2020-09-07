package matchesandmisses.processors

import matchesandmisses.MatcherFactory
import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options
import matchesandmisses.PatternMode
import spock.lang.Specification

class SafePreserveAllMatchesSpec extends Specification  {
  Options options

  private MatchesAndMisses exec(Set<String> group, String pattern, PatternMode patternMode) {
    SafePreserveAllMatches underTest = new SafePreserveAllMatches(group, options, MatcherFactory.createMatcher(patternMode, pattern))
    underTest.processGroup()
  }

  def setup() {
    options = Mock(Options)
  }

  def "none"() { // no matches for preserving, = > all marked for deletion, but in the Safe impl this is reset
    given:
    String lookFor = 'aaa'
    Set<String> group = ['bbbb', 'bbb', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == group
  }

  def "one"() {
    given:
    String lookFor = 'aaa'
    Set<String> group = ['bbbb', 'bbb', 'AAA']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.icontains)
    expect:
    results.forKeeps == ['AAA'] as Set<String>
    results.forDelete == group - ['AAA']
  }

  def "multiple"() {
    given:
    String lookFor = 'aaa'
    Set<String> group = ['bbbb', 'baAabb', 'AAA']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.icontains)
    expect:
    results.forDelete == ['bbbb'] as Set<String>
    results.forKeeps == ['baAabb', 'AAA'] as Set<String>
  }

  def "all"() { // ==> none for deletion
    given:
    String lookFor = 'aaa'
    Set<String> group = ['aaa', 'aaabb', 'aaa dfgdfg']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.startsWith)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == group
  }
}

