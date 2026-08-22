#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <username@host.com> <your_email@example.com>"
    exit 1
fi

ssh_key_file="github-actions"
ssh_server="$1"
email_address="$2"

if [ ! -f ~/.ssh/"$ssh_key_file" ]; then
    ssh-keygen -t rsa -b 4096 -C "$email_address" -f ~/.ssh/"$ssh_key_file" -N ""
fi

ssh_key_path="~/.ssh"

ssh $email_address@"$ssh_server" "mkdir -p $ssh_key_path"
scp ~/.ssh/"$ssh_key_file" "$email_address@$ssh_server:$ssh_key_path"
scp ~/.ssh/"$ssh_key_file.pub" "$email_address@$ssh_server:$ssh_key_path"

ssh $email_address@"$ssh_server" "cat $ssh_key_path/$ssh_key_file.pub >> $ssh_key_path/authorized_keys"