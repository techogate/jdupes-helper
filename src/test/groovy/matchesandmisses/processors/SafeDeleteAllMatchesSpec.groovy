package matchesandmisses.processors


import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options
import matchesandmisses.PatternMode
import matchesandmisses.MatcherFactory
import spock.lang.Specification

class SafeDeleteAllMatchesSpec extends Specification  {
  Options options

  private MatchesAndMisses exec(Set<String> group, String pattern, PatternMode patternMode) {
    SafeDeleteAllMatches underTest = new SafeDeleteAllMatches(group, options, MatcherFactory.createMatcher(patternMode, pattern))
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
    results.forDelete.isEmpty()
    results.forKeeps == ['bbbb', 'bbb', 'ccc'] as Set<String>
    results.forDelete == exec(group, lookFor, PatternMode.startsWith).forDelete
  }

  def "one"() {
    given:
      String lookFor = 'aaa'
      Set<String> group = ['aaaa', 'bbb', 'ccc']

      MatchesAndMisses results = exec(group, lookFor, PatternMode.contains)
    expect:
      results.forDelete == ['aaaa'] as Set<String>
      results.forKeeps == ['bbb', 'ccc'] as Set<String>
      results.forDelete == exec(group, lookFor, PatternMode.startsWith).forDelete
  }

  def "multiple"() {
    given:
    String lookFor = 'aaa'
    Set<String> group = ['baaa', 'aaa', 'AAA', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.contains)
    expect:
    results.forDelete.sort() == ['baaa', 'aaa' ].sort()
    results.forKeeps == ['AAA', 'ccc'] as Set<String>
    results.forDelete - ['baaa'] == exec(group, lookFor, PatternMode.startsWith).forDelete
  }

  def "multipleStartsWith"() {
    given:
    String lookFor = 'aaa'
    Set<String> group = ['baaa', 'aaa', 'AAA', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.startsWith)
    expect:
    results.forDelete == ['aaa'] as Set<String>
    results.forKeeps == ['baaa', 'AAA', 'ccc'] as Set<String>
    results.forDelete == exec(group, lookFor, PatternMode.startsWith).forDelete
  }

  def "all"() {
    given:
    String lookFor = 'aaa'
    Set<String> group = ['baaa', 'aaa', 'AAA', 'cccaAa']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.icontains)
    expect: // Because all are matched, we reset and none are matched
    results.forDelete.isEmpty()
    results.forKeeps.sort() == group.sort()
  }

  def "oneMiss"() {
    given:
    String lookFor = 'aaa'
    Set<String> group = ['baaa', 'aaa', 'AAA', 'ccc']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.icontains)
    expect:
    results.forDelete == group - ['ccc'].sort()
    results.forKeeps == ['ccc'] as Set<String>
  }

//  def "no hits"() {
//    given:
//      String lookFor = 'xaaa'
//      Set<String> group = ['aaaa', 'bbb', 'ccc']
//
//      Matchesandmisses results = exec(group, lookFor, PatternMode.startsWith)
//    expect:
//      results.matches.empty
//      results.misses == group
//      results.matches == exec(group, lookFor, PatternMode.contains).matches
//  }
//
//  def "keep first"() {
//    given:
//      String lookFor = 'aaa'
//      Set<String> group = ['aaaa', 'aaa', 'ccc']
//
//      Matchesandmisses results = exec(group, lookFor, PatternMode.contains)
//    expect:
//      results.matches == ['aaaa'] as Set<String>
//      results.misses == ['aaa', 'ccc'] as Set<String>
//      results.matches == exec(group, lookFor, PatternMode.startsWith).matches
//  }
//
//  def "keep first again"() {
//    given:
//      String lookFor = 'aaa'
//      Set<String> group = ['aaa', 'aaaa', 'ccc']
//
//      Matchesandmisses results = exec(group, lookFor, PatternMode.contains)
//    expect:
//      results.matches == ['aaa'] as Set<String>
//      results.misses == ['aaaa', 'ccc'] as Set<String>
//      results.matches == exec(group, lookFor, PatternMode.startsWith).matches
//  }



}

