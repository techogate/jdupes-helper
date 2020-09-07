package matchesandmisses.processors

import matchesandmisses.AbstractProcessor
import matchesandmisses.Matcher
import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options

class PreserveAllMatches extends AbstractProcessor {

  PreserveAllMatches(Set<String> group, Options options, Matcher matcher, Matcher pairMatcher = null) {
    super(group, options, matcher, pairMatcher)
  }

  // Marks all matches for preservation and misses for deletion
  // Dangerous as if there are no matches for preservation, they are
  // all marked for deletion

  MatchesAndMisses processGroup() {
    MatchesAndMisses deleteAllMatches = (new DeleteAllMatches(this.group, this.options, this.matcher)).processGroup()

    this.forDelete = deleteAllMatches.forKeeps
    this.forKeeps = deleteAllMatches.forDelete

    printMap('final')
    this
  }

}
