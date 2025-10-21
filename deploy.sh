#!/bin/bash

echo "Deploying to Helios"

## Remove existing deployment
ssh -p 2222 s468013@se.ifmo.ru "rm -rf wildfly-preview-26.1.3.Final/standalone/deployments/interactive-graph-ui-1.0-SNAPSHOT.war"
# add new deployment
scp -P 2222 ./build/libs/interactive-graph-ui.war s468013@se.ifmo.ru:wildfly-preview-26.1.3.Final/standalone/deployments