package hitsandmisses.processors

import hitsandmisses.AbstractProcessor
import hitsandmisses.HitsAndMisses
import hitsandmisses.Matcher
import hitsandmisses.Options

class DeleteProcessor extends AbstractProcessor {

  DeleteProcessor(Set<String> group, Options options, Matcher matcher) {
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
    // make sure we're not going to suggest deleting all in a group
    if (misses.isEmpty()) {
      println "WARNING GROUP IGNORED: Pattern [${this.options.pattern}] match every element in group: \n${group.join('\n')}\n"
      this.printMap()
      this.hits.clear();
      this.group.each { misses << it }
    }
    printMap('final')
    this
  }

}
