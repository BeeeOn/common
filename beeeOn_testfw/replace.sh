grep -r "email" .
find . -name "*.input"  | xargs sed -i 's###g'
