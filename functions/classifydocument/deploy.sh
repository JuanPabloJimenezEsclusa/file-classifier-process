#!/usr/bin/env bash

set -o errexit # Exit on error. Append "|| true" if you expect an error.
set -o errtrace # Exit on error inside any functions or subshells.
set -o nounset # Do not allow use of undefined vars. Use ${VAR:-} to use an undefined VAR
set -o xtrace

cd "$(dirname "$0")"

sam build \
  --template ./template.yml \
  --build-dir ./.aws-sam/build

sam package \
  --template-file ./.aws-sam/build/template.yaml \
  --output-template-file ./.aws-sam/build/packaged-template.yaml \
  --s3-bucket file-classifier-process-template-01

sam deploy \
  --template-file ./.aws-sam/build/packaged-template.yaml \
  --stack-name file-classifier-process-template-01 \
  --s3-bucket file-classifier-process-template-01 \
  --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM \
  --confirm-changeset \
  --on-failure DELETE

# smoke tests
sam list resources --stack-name file-classifier-process-template-01
sam list endpoints --stack-name file-classifier-process-template-01
