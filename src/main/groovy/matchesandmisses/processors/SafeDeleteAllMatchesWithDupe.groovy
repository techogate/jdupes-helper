package matchesandmisses.processors

import matchesandmisses.AbstractProcessor
import matchesandmisses.Matcher
import matchesandmisses.MatchesAndMisses
import matchesandmisses.Options

class SafeDeleteAllMatchesWithDupe extends AbstractProcessor {

    SafeDeleteAllMatchesWithDupe(Set<String> group, Options options,
                                 Matcher matcher, Matcher pairMatcher) {
        super(group, options, matcher, pairMatcher)
    }

    MatchesAndMisses processGroup() {
        String pair = null
        this.group.each { String fname ->
            if (pairMatcher.match(fname)) {
                pair = fname
                if (options.verbose) println "Pair:${pair}"
            }
            if (matcher.match(fname)) {
                forDelete << fname
                if (options.verbose) println "Hit:${fname}"
            } else {
                forKeeps << fname
                if (options.verbose) println "Miss:${fname}"
            }
        }

        if(!pair && options.verbose) {
            println "INFO: Group does not contain pair ${pairMatcher}\n"
        }

        if (pair && forDelete.contains(pair)) {
            println "WARNING: Search Pattern [${matcher.dump()}] and pair search pattern match [${pairMatcher.dump()}] match the same elements. They should be mutually exclusive.\n"
            pair = null
        }

        // make sure we're not going to suggest deleting all in a group and that the pair dupe has been detected
        if (forKeeps.isEmpty() || !pair) {
            println "WARNING GROUP IGNORED: Pattern [${matcher.pattern}] match every element in group: \n${group.join('\n')}\n"
            this.printMap()
            this.forDelete.clear();
            this.group.each { forKeeps << it }
        }
        printMap('final')
        this
    }

}
