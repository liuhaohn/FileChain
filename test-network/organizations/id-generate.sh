#!/bin/bash

function one_line_pem {
    echo "`awk 'NF {sub(/\\n/, ""); printf "%s\\\\\\\n",$0;}' $1`"
}

function json_id {
    local CE=$(one_line_pem $2)
    local PR=$(one_line_pem $3)
    sed -e "s/\${MSP}/Org$1MSP/" \
        -e "s#\${CERTIFICATE}#$CE#" \
        -e "s#\${PRIVATE}#$PR#" \
        ../organizations/id-template.json
}


ORG=1
CERTPEM=../organizations/peerOrganizations/org1.example.com/users/User1@org1.example.com/msp/signcerts/*
KEY=../organizations/peerOrganizations/org1.example.com/users/User1@org1.example.com/msp/keystore/*
echo "$(json_id $ORG $CERTPEM $KEY)" > ../organizations/peerOrganizations/org1.example.com/User1@org1.example.com.id

CERTPEM=../organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/*
KEY=../organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/*
echo "$(json_id $ORG $CERTPEM $KEY)" > ../organizations/peerOrganizations/org1.example.com/Admin@org1.example.com.id

CERTPEM=../organizations/peerOrganizations/org1.example.com/users/Visitor@org1.example.com/msp/signcerts/*
KEY=../organizations/peerOrganizations/org1.example.com/users/Visitor@org1.example.com/msp/keystore/*
echo "$(json_id $ORG $CERTPEM $KEY)" > ../organizations/peerOrganizations/org1.example.com/Visitor@org1.example.com.id


ORG=2
CERTPEM=../organizations/peerOrganizations/org2.example.com/users/User1@org2.example.com/msp/signcerts/*
KEY=../organizations/peerOrganizations/org2.example.com/users/User1@org2.example.com/msp/keystore/*
echo "$(json_id $ORG $CERTPEM $KEY)" > ../organizations/peerOrganizations/org2.example.com/User1@org2.example.com.id

CERTPEM=../organizations/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp/signcerts/*
KEY=../organizations/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp/keystore/*
echo "$(json_id $ORG $CERTPEM $KEY)" > ../organizations/peerOrganizations/org2.example.com/Admin@org2.example.com.id

CERTPEM=../organizations/peerOrganizations/org2.example.com/users/Visitor@org2.example.com/msp/signcerts/*
KEY=../organizations/peerOrganizations/org2.example.com/users/Visitor@org2.example.com/msp/keystore/*
echo "$(json_id $ORG $CERTPEM $KEY)" > ../organizations/peerOrganizations/org2.example.com/Visitor@org2.example.com.id
