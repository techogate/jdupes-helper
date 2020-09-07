package matchesandmisses.processors

import matchesandmisses.AbstractProcessor
import matchesandmisses.Matcher
import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options

class SafeDeleteAllMatches extends AbstractProcessor {

    SafeDeleteAllMatches(Set<String> group, Options options, Matcher matcher, Matcher pairMatcher = null) {
        super(group, options, matcher, pairMatcher)
    }

    // Marks all matches in the goupr for deletion, however, if all
    // entries in the group match a safety option
    // resets so that the matches are emptied and the misses is filled.

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
        // make sure we're not going to suggest deleting all in a group
        if (forKeeps.isEmpty()) {
            println "WARNING GROUP IGNORED: Pattern [${matcher.pattern}] match every element in group: \n${group.join('\n')}\n"
            this.printMap()
            this.forDelete.clear();
            this.group.each { forKeeps << it }
        }
        printMap('final')
        this
    }

}
