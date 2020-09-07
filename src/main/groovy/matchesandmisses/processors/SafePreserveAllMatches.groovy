package matchesandmisses.processors

import matchesandmisses.AbstractProcessor
import matchesandmisses.Matcher
import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options

class SafePreserveAllMatches extends AbstractProcessor {

  SafePreserveAllMatches(Set<String> group, Options options, Matcher matcher, Matcher pairMatcher = null) {
    super(group, options, matcher, pairMatcher)
  }

  // Marks all matches for preservation and misses for deletion
  // if there are no matches for preservation, they results are reset:
  // ie all entries marked for preservation, none for deletion

  MatchesAndMisses processGroup() {
    MatchesAndMisses deleteAllMatches = (new DeleteAllMatches(this.group, this.options, this.matcher)).processGroup()

    this.forDelete = deleteAllMatches.forKeeps
    this.forKeeps = deleteAllMatches.forDelete

    if (this.forKeeps.isEmpty()) {
      this.forDelete = []
      this.forKeeps = group
    }

    printMap('final')
    this
  }

}
