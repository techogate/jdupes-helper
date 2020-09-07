package matchesandmisses

class MatchesAndMissesSerializer {
  private MatchesAndMisses matchesAndMisses
  File matches
  File misses
  int countMatches
  int countMisses
  boolean dryRun


  MatchesAndMissesSerializer(MatchesAndMisses matchesAndMisses, File matches, File misses, boolean dryRun) {
    this.matchesAndMisses = matchesAndMisses
    this.matches = matches
    this.misses = misses
    this.dryRun = dryRun
  }

  def serialize() {

    matchesAndMisses.forDelete.each { String match ->
      if (!dryRun) this.matches.append("$match\n")
      this.countMatches++
    }

    if (matchesAndMisses.forKeeps.size() > 1) {
      if (!dryRun) this.misses.append("\n")
      matchesAndMisses.forKeeps.each { String miss ->
        if (!dryRun) this.misses.append("$miss\n")
        this.countMisses++
      }
    }
    new Tuple2(countMatches, countMisses)
  }
}

