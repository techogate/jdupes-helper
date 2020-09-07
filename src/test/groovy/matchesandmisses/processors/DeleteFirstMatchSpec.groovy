package matchesandmisses.processors


import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options
import matchesandmisses.PatternMode
import matchesandmisses.MatcherFactory
import spock.lang.Specification

class DeleteFirstMatchSpec extends Specification  {
  Options options

  private MatchesAndMisses exec(Set<String> group, String pattern, PatternMode patternMode) {
    DeleteFirstMatch underTest = new DeleteFirstMatch(group, options, MatcherFactory.createMatcher(patternMode, pattern))
    MatchesAndMisses results = underTest.processGroup()
    results
  }

  def setup() {
    options = Mock(Options)
  }

  def "one hit"() {
    given:
      String lookFor = 'aaa'
      Set<String> group = ['aaaa', 'bbb', 'ccc']

      MatchesAndMisses results = exec(group, lookFor, PatternMode.contains)
    expect:
      results.forDelete == ['aaaa'] as Set<String>
      results.forKeeps == ['bbb', 'ccc'] as Set<String>
      results.forDelete == exec(group, lookFor, PatternMode.startsWith).forDelete
  }

  def "no hits"() {
    given:
    String lookFor = 'xaaa'
    Set<String> group = ['aaaa', 'bbb', 'ccc']
    MatchesAndMisses results = exec(group, lookFor, PatternMode.startsWith)
    expect:
    results.forDelete.empty
    results.forKeeps == group
    results.forDelete == exec(group, lookFor, PatternMode.contains).forDelete
  }



  def "keep first"() {
    given:
      String lookFor = 'aaa'
      Set<String> group = ['aaaa', 'aaa', 'ccc']

      MatchesAndMisses results = exec(group, lookFor, PatternMode.contains)
    expect:
      results.forDelete == ['aaaa'] as Set<String>
      results.forKeeps == ['aaa', 'ccc'] as Set<String>
      results.forDelete == exec(group, lookFor, PatternMode.startsWith).forDelete
  }

  def "keep first again"() {
    given:
      String lookFor = 'aaa'
      Set<String> group = ['AAA', 'aaa', 'ccc']

      MatchesAndMisses results = exec(group, lookFor, PatternMode.icontains)
    expect:
      results.forDelete == ['AAA'] as Set<String>
      results.forKeeps == ['aaa', 'ccc'] as Set<String>
  }
}

