```plantuml
title

High-level design of "cosmo-page-backend"

end title

cloud "External environment" {
actor "cosmo-page-frontend"
}

component "Control plane" {
node "Facebook API"
node "Image"
node "Mail"
node "Posts"
}

cloud " External APIs" {
entity "Facebook"
}

[cosmo-page-frontend] <--> [Posts]: " Perform posts management"
[cosmo-page-frontend] <--> [Image]: " Upload images"
[cosmo-page-frontend] <--> [Mail]: " Send mails according to mailing list"
[cosmo-page-frontend] <--> [Facebook API]: " Retrieve user info and external posts"
[Facebook API] <--> [Facebook]: " Retrieve data according to designed contracts"
```