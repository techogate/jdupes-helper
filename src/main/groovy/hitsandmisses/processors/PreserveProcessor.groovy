package hitsandmisses.processors

import hitsandmisses.AbstractProcessor
import hitsandmisses.HitsAndMisses
import hitsandmisses.Matcher
import hitsandmisses.Options

class PreserveProcessor extends AbstractProcessor {

  PreserveProcessor(Set<String> group, Options options, Matcher matcher) {
    super(group, options, matcher)
  }

  HitsAndMisses processGroup() {
    this.group.each { String fname ->
      // When preserving, only add one match to the misses
      if (matcher.match(fname) && misses.isEmpty())
        misses << fname
      else
        hits << fname
    }
    // make sure we're not going to suggest deleting all in a group
    if (misses.isEmpty()) {
      if (options.verbose) {
        println "WARNING GROUP IGNORED: No files matching preserve pattern [${this.options.pattern}] in group:\n${group.join('\n')}\n"
      }
      this.printMap()
      this.hits.clear();
      this.group.each { misses << it }
    }
    printMap('final')
    this
  }

}
