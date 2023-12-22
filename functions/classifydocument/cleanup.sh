#!/usr/bin/env bash

set -o errexit  # Exit on error. Append "|| true" if you expect an error.
set -o errtrace # Exit on error inside any functions or subshells.
set -o nounset  # Do not allow use of undefined vars. Use ${VAR:-} to use an undefined VAR
#set -o xtrace

cd "$(dirname "$0")"

# remove images
aws s3 rm s3://file-classifier-process-s3-02/ --recursive || true

# delete stack
sam delete \
  --stack-name file-classifier-process-template-01 \
  --s3-bucket file-classifier-process-template-01 \
  --debug || true

# smoke tests
sam list resources --stack-namefile-classifier-process-template-01 || true
sam list endpoints --stack-name file-classifier-process-template-01 || true
