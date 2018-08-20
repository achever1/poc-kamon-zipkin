nbClients=$1
for i in `seq 1 $nbClients`
do
    curl 'http://localhost:9000/endpoint1' &>/dev/null  &
done
