#!/bin/bash
echo "=========== executing $(pwd)/id-generate.sh, relative path is test-network ==========="


function one_line_pem {
    echo "`awk 'NF {sub(/\\n/, ""); printf "%s\\\\\\\n",$0;}' $1`"
}

function json_id {
    local CE=$(one_line_pem $2)
    local PR=$(one_line_pem $3)
    sed -e "s/\${MSP}/Org$1MSP/" \
        -e "s#\${CERTIFICATE}#$CE#" \
        -e "s#\${PRIVATE}#$PR#" \
        ./organizations/id-template.json
}


ORG=3
CERTPEM=./organizations/peerOrganizations/org3.example.com/users/User1@org3.example.com/msp/signcerts/*
KEY=./organizations/peerOrganizations/org3.example.com/users/User1@org3.example.com/msp/keystore/*
echo "$(json_id $ORG $CERTPEM $KEY)" > ./organizations/peerOrganizations/org3.example.com/User1@org3.example.com.id

CERTPEM=./organizations/peerOrganizations/org3.example.com/users/Admin@org3.example.com/msp/signcerts/*
KEY=./organizations/peerOrganizations/org3.example.com/users/Admin@org3.example.com/msp/keystore/*
echo "$(json_id $ORG $CERTPEM $KEY)" > ./organizations/peerOrganizations/org3.example.com/Admin@org3.example.com.id

CERTPEM=./organizations/peerOrganizations/org3.example.com/users/Visitor@org3.example.com/msp/signcerts/*
KEY=./organizations/peerOrganizations/org3.example.com/users/Visitor@org3.example.com/msp/keystore/*
echo "$(json_id $ORG $CERTPEM $KEY)" > ./organizations/peerOrganizations/org3.example.com/Visitor@org3.example.com.id
