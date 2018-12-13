#!/usr/bin/env bash
set -euf -o pipefail
gpg -q --batch --fast-import scripts/codesigning.asc
GPG_TTY=$(tty) mvn deploy -Psign-source-javadoc --settings scripts/ci-settings.xml -DskipTests=true
