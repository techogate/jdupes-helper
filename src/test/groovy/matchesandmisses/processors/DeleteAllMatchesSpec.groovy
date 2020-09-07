package matchesandmisses.processors

import matchesandmisses.MatcherFactory
import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options
import matchesandmisses.PatternMode
import spock.lang.Specification

class DeleteAllMatchesSpec extends Specification  {
  Options options

  private MatchesAndMisses exec(Set<String> group, String pattern, PatternMode patternMode) {
    DeleteAllMatches underTest = new DeleteAllMatches(group, options, MatcherFactory.createMatcher(patternMode, pattern))
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

  def "all"() {
    given:
    String lookFor = 'a'
    Set<String> group = ['baaa', 'aaa', 'AAA', 'ccc     a']

    MatchesAndMisses results = exec(group, lookFor, PatternMode.icontains)
    expect:
    results.forDelete.sort() == group.sort()
    results.forKeeps.isEmpty()
  }

}

