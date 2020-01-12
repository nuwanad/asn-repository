#!/usr/bin/env bash

./wait-for-it.sh asn-mysql:3306
java -jar /application.jar