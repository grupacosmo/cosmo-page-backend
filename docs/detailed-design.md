```plantuml
!pragma teoz true

title 
    Detailed design of "cosmo-page-backend" 
end title

box "External environment" #MOTIVATION
actor "Client" as client
end box

box "Control plain" #MOTIVATION
participant "Facebook API" as facebookapi
participant "Image" as image
participant "Mail" as mail
participant "Posts" as posts

box "External APIs" #MOTIVATION
entity "Facebook" as facebook
end box

activate facebookapi

group /api/facebook/token POST

client -> facebookapi: create api token

activate facebook
facebookapi <-> facebook: request api token
deactivate facebook

end group

group /api/facebook/accounts GET

client <-> facebookapi: retrieve user accounts for the token

activate facebook
facebookapi <-> facebook: request user account details for the provided token
deactivate facebook

end group

group /api/facebook/posts GET

client <-> facebookapi: retrieve all the posts for an account

activate facebook
facebookapi <-> facebook: request all the posts for the provided account
deactivate facebook

end group

group /api/facebook/posts/{id} GET

client <-> facebookapi: retrieve post details

activate facebook
facebookapi <-> facebook: request provided post details
deactivate facebook

end group

activate image

group /api/images POST

client -> image: upload post image

end group

group /api/images/{id} GET

client <-> image: retrieve uploaded image

end group

deactivate image

activate mail

group /api/mail POST

client -> mail: add to the mailing list

end group

group /api/mail GET

client <-> mail: retrieve mailing list

end group

deactivate mail

activate posts

group /api/posts/sync POST

client -> posts: perform posts synchronization with external APIs

end group

group /api/posts POST

client <-> posts: create new post

end group

group /api/posts/{id} GET

client <-> posts: retrieve post by the provided id

end group

group /api/posts GET

client <-> posts: retrieve all the created posts

end group

group /api/posts/{id} DELETE

client <-> posts: delete post with the provided id

end group

deactivate posts
```