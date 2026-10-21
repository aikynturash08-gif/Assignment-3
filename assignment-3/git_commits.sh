#!/bin/sh
# Run once in the project folder AFTER setting your identity:
#   git config user.name "Your Name"; git config user.email "you@example.com"
# Creates a feature-branch workflow with meaningful commits.
set -e
git init -b main
git add .gitignore && git commit -m "Initial commit: .gitignore"
feat() { # branch, message, paths...
  b=$1; m=$2; shift 2
  git checkout -q -b "$b"; git add "$@"; git commit -q -m "$m"
  git checkout -q main; git merge -q --no-ff "$b" -m "Merge branch '$b'"
}
feat feature/graph        "Add graph model and seeded graph generators"       src/graph
feat feature/dsu          "Add DSU with path compression and union by rank"   src/dsu
feat feature/mst          "Implement Kruskal and Prim algorithms"             src/mst
feat feature/connectivity "Add topological sort (Kahn) and SCC (Kosaraju)"    src/connectivity
feat feature/dijkstra     "Implement Dijkstra's shortest paths"               src/shortestpath
feat feature/tests        "Add correctness tests"                             src/tests
feat feature/experiments  "Add benchmark, plotting script and run.sh"         src/experiments plot_results.py run.sh
feat feature/results      "Add experiment results, tables and plots"          results
feat feature/report       "Write README report"                               README.md
git log --oneline --graph
