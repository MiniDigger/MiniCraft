# MiniServer

Clean room implementation of the minecraft server using java and netty.

Nothing exciting here, just learning netty.

## Whats done

- [x] Packet handling
  - [x] Sending
  - [x] Receiving
- [ ] Server List ping
  - [x] JSON Response
  - [ ] Favicon
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
  - [ ] chunk data
  - [ ] spawn position
  - [x] position and look (we can see stuff, yee https://i.imgur.com/JgLatwe.png)
  - [ ] teleport confirm
- [x] chat (woooo https://i.imgur.com/hiyP7TV.png)
- [ ] Everything else
- [ ] keep alive
  - [x] send it so we don't get disconnected
  - [ ] actually handle it properly
  
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
