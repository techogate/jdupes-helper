package matchesandmisses.processors

import matchesandmisses.AbstractProcessor
import matchesandmisses.Matcher
import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options

class DeleteAllMatches extends AbstractProcessor {

  DeleteAllMatches(Set<String> group, Options options, Matcher matcher, Matcher pairMatcher = null) {
    super(group, options, matcher, pairMatcher)
  }

  // Marks all matches for deletion
  // Dangerous, as no checks performed to see that everything in the group is not deleted

  MatchesAndMisses processGroup() {
    this.group.each { String fname ->
      if (matcher.match(fname)) {
        forDelete << fname
        if (options.verbose) println "Hitt:${fname}"
      } else {
        forKeeps << fname
        if (options.verbose) println "Miss:${fname}"
      }
    }
    this
  }

}
