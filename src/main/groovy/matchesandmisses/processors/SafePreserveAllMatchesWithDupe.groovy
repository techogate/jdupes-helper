package matchesandmisses.processors

import matchesandmisses.AbstractProcessor
import matchesandmisses.Matcher
import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options

class SafePreserveAllMatchesWithDupe extends AbstractProcessor {

    SafePreserveAllMatchesWithDupe(Set<String> group, Options options,
                                   Matcher matcher, Matcher pairMatcher) {
        super(group, options, matcher, pairMatcher)
    }

    MatchesAndMisses processGroup() {
        MatchesAndMisses deleteAllMatchesWithDupe = (new SafeDeleteAllMatchesWithDupe(this.group, this.options,
                this.matcher,this.pairMatcher)).processGroup()

        this.forDelete = deleteAllMatchesWithDupe.forKeeps
        this.forKeeps = deleteAllMatchesWithDupe.forDelete

        if (this.forKeeps.isEmpty()) {
            this.forDelete = []
            this.forKeeps = group
        }

        printMap('final')
        this
    }

}
