#!/usr/bin/env bash

echo "Kicking off!"

java -cp target/meliora-1.0-alpha-jar-with-dependencies.jar Trainer  -Xms1024m  -Xmx85g

