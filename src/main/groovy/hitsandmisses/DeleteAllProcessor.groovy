package hitsandmisses

class DeleteAllProcessor extends AbstractProcessor {

  DeleteAllProcessor(Set<String> group, Options options ) {
    super(group, options)
  }

  HitsAndMisses processGroup() {
    this.group.each { String fname ->
      if (fname."${this.options.patternMode}"(this.options.pattern))
        hits << fname
    }
    this
  }

}
