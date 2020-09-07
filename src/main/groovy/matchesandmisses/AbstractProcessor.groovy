package matchesandmisses

abstract class AbstractProcessor implements MatchesAndMisses {
  private Set<String> group
  protected Set<String> forDelete
  protected Set<String> forKeeps
  private Options options
  private Matcher matcher
  private Matcher pairMatcher

  AbstractProcessor(Set<String> group, Options options, Matcher matcher, Matcher pairMatcher = null) {
    this.group = group
    this.forDelete = new HashSet<String>(20)
    this.forKeeps = new HashSet<String>(20)
    this.options = options
    this.matcher = matcher
    this.pairMatcher = pairMatcher

    // Group must be at least an entry plus >=1 dupe
    assert this.group.size() > 1
  }

  Set<String> getForDelete() {
    this.forDelete
  }

  Set<String> getForKeeps() {
    this.forKeeps
  }

  Set<String> getGroup() {
    this.group
  }

  Options getOptions() {
    this.options
  }

  Matcher getMatcher() {
    this.matcher
  }

  Matcher getPairMatcher() {
    this.pairMatcher
  }

  protected printMap(String msg = '') {
    //print "${this.group.size() > 3 ? this.group.size() : ''}."
    if (!options.verbose) return

    print "==Group ($msg)\n${group.join('\n')}\n"
    print "---Hits\n${forDelete.join('\n')}\n"
    print "---Miss\n${forKeeps.join('\n')}\n"
    if (pairMatcher) {
      print "---Pair\n${pairMatcher.dump()}\n"
    }
  }
}
