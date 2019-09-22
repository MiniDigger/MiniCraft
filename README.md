# MiniServer

Clean room implementation of the minecraft server using java and netty.

Nothing exciting here, just learning netty.

## Whats done

- [x] Packet handling
  - [x] Sending
  - [x] Receiving
- [x] Server List ping
  - [x] JSON Response
  - [x] Favicon (woooo https://i.imgur.com/v8oVkem.png)
  - [x] Online Status
- [ ] Login
  - [x] offline mode
  - [ ] encryption
  - [ ] compression
- [ ] Joining
  - [x] server brand (wooo https://i.imgur.com/VbbRwo1.png)
  - [x] client brand
  - [ ] server difficulty
  - [ ] player abilities
  - [ ] client settings
  - [ ] spawn position
  - [x] position and look (we can see stuff, yee https://i.imgur.com/JgLatwe.png)
  - [ ] teleport confirm
- [x] chat (woooo https://i.imgur.com/hiyP7TV.png)
- [ ] world stuff
  - [x] sending empty chunks 
  - [x] sending somewhat useable hardcoded stuff (https://i.imgur.com/RTrpYBS.png)
  - [ ] implement palette
  - [ ] reading a vanilla worlds
  - [ ] reading from hypixels format
- [ ] keep alive
  - [x] send it so we don't get disconnected
  - [ ] actually handle it properly
- [ ] Everything else
  
### misc todo

- [ ] support relative position/rotation
- [ ] verify teleport ids
- [ ] figure out why chat format isn't working properly
  
## Building

Maven

## Contributing

I am not sure yet what to do with this, but if you want to help, you are welcome,
 just hit me up, MiniDigger on freenode/esper/spigot irc or MiniDigger#3086 on discord.

## Licence

MIT
