#!/usr/bin/env bash

set -o errexit  # Exit on error. Append "|| true" if you expect an error.
set -o errtrace # Exit on error inside any functions or subshells.
set -o nounset  # Do not allow use of undefined vars. Use ${VAR:-} to use an undefined VAR
#set -o xtrace

cd "$(dirname "$0")"

folder_path="/tmp/data"

find "${folder_path}" -type f -regex ".*.TESTING.*" | while read -r file; do
  delay="$((RANDOM % 3 + 3))"
  echo "Next file: (${file}) Waiting for (${delay}) seconds..."
  sleep "${delay}"

  aws s3 cp "${file}" s3://file-classifier-process-s3-02/process/ \
    --region "us-west-2" \
    --include "*.pdf|*.jpg|*.jpeg|*.png"
done
