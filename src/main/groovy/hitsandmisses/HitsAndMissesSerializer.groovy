package hitsandmisses

class HitsAndMissesSerializer {
  private HitsAndMisses hitsAndMisses
  File hits
  File misses
  int countHits
  int countMisses
  boolean dryRun


  HitsAndMissesSerializer(HitsAndMisses hitsAndMisses, File hits, File misses, boolean dryRun) {
    this.hitsAndMisses = hitsAndMisses
    this.hits = hits
    this.misses = misses
    this.dryRun = dryRun
  }

  def serialize() {

    hitsAndMisses.hits.each { String hit ->
      if (!dryRun) this.hits.append("$hit\n")
      this.countHits++
    }

    if (hitsAndMisses.misses.size() > 1) {
      if (!dryRun) this.misses.append("\n")
      hitsAndMisses.misses.each { String miss ->
        if (!dryRun) this.misses.append("$miss\n")
        this.countMisses++
      }
    }
    new Tuple2(countHits, countMisses)
  }
}

