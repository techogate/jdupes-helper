package hitsandmisses


abstract class AbstractProcessor implements HitsAndMisses {
  private Set<String> group
  private Set<String> hits
  private Set<String> misses
  private Options options

  AbstractProcessor(Set<String> group, Options options) {
    this.group = group
    this.hits = new HashSet<String>(20)
    this.misses = new HashSet<String>(20)
    this.options = options
  }

  Set<String> getHits() {
    this.hits
  }

  Set<String> getMisses() {
    this.misses
  }

  Set<String> getGroup() {
    this.group
  }

  Options getOptions() {
    this.options
  }

  protected printMap(String msg = '') {
    //print "${this.group.size() > 3 ? this.group.size() : ''}."
    if (!options.verbose) return

    print "==Group ($msg)\n${group.join('\n')}\n"
    print "---Hits\n${hits.join('\n')}\n"
    print "---Miss\n${misses.join('\n')}\n"
  }



}
