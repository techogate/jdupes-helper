package hitsandmisses

class HitsAndMissesSerializer {
  private HitsAndMisses hitsAndMisses
  File hits
  File misses
  int countHits
  int countMisses


  HitsAndMissesSerializer(HitsAndMisses hitsAndMisses, File hits, File misses) {
    this.hitsAndMisses = hitsAndMisses
    this.hits = hits
    this.misses = misses
  }

  def serialize() {

    hitsAndMisses.hits.each { String hit ->
      this.hits.append("$hit\n")
      this.countHits++
    }

    if (hitsAndMisses.misses.size() > 1) {
      this.misses.append("\n")
      hitsAndMisses.misses.each { String miss ->
        this.misses.append("$miss\n")
        this.countMisses++
      }
    }
    new Tuple2(countHits, countMisses)
  }
}

