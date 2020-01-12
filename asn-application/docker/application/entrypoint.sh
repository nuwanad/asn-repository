#!/usr/bin/env bash

./wait-for-it.sh asn-mysql:3306 -t 60
java -jar /application.jar