package matchesandmisses.processors

import matchesandmisses.AbstractProcessor
import matchesandmisses.Matcher
import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options

class DeleteFirstMatch extends AbstractProcessor {

  DeleteFirstMatch(Set<String> group, Options options, Matcher matcher, Matcher pairMatcher = null) {
    super(group, options, matcher, pairMatcher)
  }

  MatchesAndMisses processGroup() {

    this.group.each { String fname ->
      if (matcher.match(fname) && forDelete.isEmpty()) {
        forDelete << fname
        if (options.verbose) println "Hit:${fname}"
      } else {
        forKeeps << fname
        if (options.verbose) println "Miss:${fname}"
      }
    }
    assert forDelete.size() <= 1
    assert forKeeps.size() >= (group.size() - 1)

    printMap('final')
    this
  }

}
