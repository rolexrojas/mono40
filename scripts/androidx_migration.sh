#! /bin/bash
# shellcheck disable=SC2086
# shellcheck disable=SC2162

# This script provides a workaround for having dependent modules which 
# have not been migrated to AndroidX
# see: https://developer.android.com/jetpack/androidx/migrate
# inspired from https://gist.github.com/dlew/5db1b780896bbc6f542e7c00a11db6a0

SCRIPTS_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "${SCRIPTS_DIR}"/.. && pwd)"
MAPPING_FILE="$SCRIPTS_DIR/androidx_class_map.csv"

GSED=$(command -v gsed)

if [[ -z "$GSED" ]]; then
  echo "ERROR: you need to install gnu-sed first."
  echo "run: 'brew install gnu-sed'"
  exit 101
fi

replace=""
while IFS=, read -r from to
do
  replace+="s/$from/$to/g;"
done <<< "$(cat $MAPPING_FILE)"

i=0
(find $PROJECT_DIR \( -name "*.kt" -o -name "*.java" -o -name "*.xml" \) -type f ! -path '*/\.git*' ! -path '**/android/app/build/*' ! -path '**/\.idea/*' 2>/dev/null |
while read file
do
  grep -E "android.arch|android.databinding|android.support" $file > /dev/null 2>/dev/null
  ret=$?
  if (( ! ret )); then
    $GSED -i.bak "$replace" $file 
    cmp --silent $file $file.bak
    ret=$?
    if (( ret ));then
      printf "\nDoing file %s\n" $file
     else
      i=$((i+1))
      printf '\r%2d skipped' $i
      rm -f $file.bak 
    fi
  fi
done
echo 
)