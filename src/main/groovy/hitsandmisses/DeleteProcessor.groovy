package hitsandmisses

class DeleteProcessor extends AbstractProcessor {

  DeleteProcessor(Set<String> group, Options options) {
    super(group, options)
  }

  HitsAndMisses processGroup() {
    this.group.each { String fname ->
      if (fname."${this.options.patternMode}"(this.options.pattern))
        hits << fname
      else
        misses << fname
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
