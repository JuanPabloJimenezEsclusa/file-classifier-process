#!/usr/bin/env bash

set -o errexit  # Exit on error. Append "|| true" if you expect an error.
set -o errtrace # Exit on error inside any functions or subshells.
set -o nounset  # Do not allow use of undefined vars. Use ${VAR:-} to use an undefined VAR
set -o xtrace

cd "$(dirname "$0")"

# copy folders into s3
aws s3 cp ./files/process/trainingData s3://file-classifier-process-s3-01/process/trainingData/ \
  --recursive \
  --region "eu-west-1" \
  --exclude "*.sh" \
  --include "*.pdf|*.jpg|*.jpeg|*.png"

# consult images in s3
aws s3 ls s3://file-classifier-process-s3-01/ \
  --recursive \
  --summarize \
  --human-readable
