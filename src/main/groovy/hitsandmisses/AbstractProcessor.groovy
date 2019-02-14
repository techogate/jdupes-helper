package hitsandmisses

import hitsandmisses.HitsAndMisses
import hitsandmisses.Options


abstract class AbstractProcessor implements HitsAndMisses {
  private Set<String> group
  private Set<String> hits
  private Set<String> misses
  private Options options
  private Matcher matcher

  AbstractProcessor(Set<String> group, Options options, Matcher matcher) {
    this.group = group
    this.hits = new HashSet<String>(20)
    this.misses = new HashSet<String>(20)
    this.options = options
    this.matcher = matcher
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

  Matcher getMatcher() {
    this.matcher
  }

  protected printMap(String msg = '') {
    //print "${this.group.size() > 3 ? this.group.size() : ''}."
    if (!options.verbose) return

    print "==Group ($msg)\n${group.join('\n')}\n"
    print "---Hits\n${hits.join('\n')}\n"
    print "---Miss\n${misses.join('\n')}\n"
  }



}
