#!/bin/sh

git filter-branch --commit-filter '
        if [ "$GIT_COMMITTER_NAME" = "Erik Dubois" ];
        then
                GIT_COMMITTER_NAME="DaMatrix";
                GIT_AUTHOR_NAME="DaMatrix";
                GIT_COMMITTER_EMAIL="joey.rabil@gmail.com";
                GIT_AUTHOR_EMAIL="joey.rabil@gmail.com";
                git commit-tree "$@";
        else
                git commit-tree "$@";
        fi' HEAD
