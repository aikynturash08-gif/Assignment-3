#!/bin/sh
set -e
mkdir -p out results/tables results/plots
javac -d out $(find src -name '*.java')
java -cp out tests.Tests
java -cp out experiments.Benchmark results/tables/results.csv
python3 plot_results.py
