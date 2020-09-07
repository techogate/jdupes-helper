package matchesandmisses

interface MatchesAndMisses {
  Set<String> getForDelete()
  Set<String> getForKeeps()
  MatchesAndMisses processGroup()
}

