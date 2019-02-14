package hitsandmisses.processors

import hitsandmisses.AbstractProcessor
import hitsandmisses.HitsAndMisses
import hitsandmisses.Matcher
import hitsandmisses.Options

class DeleteAllProcessor extends AbstractProcessor {

  DeleteAllProcessor(Set<String> group, Options options, Matcher matcher) {
    super(group, options, matcher)
  }

  HitsAndMisses processGroup() {
    this.group.each { String fname ->
      if (matcher.match(fname)) {
        hits << fname
        if (options.verbose) println "Hitt:${fname}"
      } else {
        misses << fname
        if (options.verbose) println "Miss:${fname}"
      }
    }
    this
  }

}
