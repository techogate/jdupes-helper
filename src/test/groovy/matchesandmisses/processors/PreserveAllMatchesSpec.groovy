package matchesandmisses.processors

import matchesandmisses.MatcherFactory
import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options
import matchesandmisses.PatternMode
import spock.lang.Specification

class PreserveAllMatchesSpec extends Specification  {
  Options options

  private MatchesAndMisses exec(Set<String> group, String pattern, PatternMode patternMode) {
    PreserveAllMatches underTest = new PreserveAllMatches(group, options, MatcherFactory.createMatcher(patternMode, pattern))
    underTest.processGroup()
  }

  def setup() {
    options = Mock(Options)
  }

  def "none"() {
    given:
    String lookFor = 'aaa'
    Set<String> group = ['bbbb', 'bbb', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains)
    expect:
    results.forDelete == group
    results.forKeeps.isEmpty()
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

  def "all"() {
    given:
    String lookFor = 'aaa'
    Set<String> group = ['aaa', 'aaabb', 'aaa dfgdfg']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.startsWith)
    expect:
    results.forDelete.isEmpty()
    results.forKeeps == group
  }



//
//  def "one"() {
//    given:
//      String lookFor = 'aaa'
//      Set<String> group = ['aaaa', 'bbb', 'ccc']
//
//      MatchesAndMisses results = exec(group, lookFor, PatternMode.contains)
//    expect:
//      results.matches == ['aaaa'] as Set<String>
//      results.misses == ['bbb', 'ccc'] as Set<String>
//      results.matches == exec(group, lookFor, PatternMode.startsWith).matches
//  }
//
//  def "all"() {
//    given:
//    String lookFor = 'a'
//    Set<String> group = ['baaa', 'aaa', 'AAA', 'ccc     a']
//
//    MatchesAndMisses results = exec(group, lookFor, PatternMode.icontains)
//    expect:
//    results.matches.sort() == group.sort()
//    results.misses.isEmpty()
//  }

}

