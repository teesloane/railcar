local utf8 = require('utf8')
Object = require 'libraries/classic'

Prompt = Object:extend()

function Prompt:new()
  self.text = 'hi'

  -- enable key repeat so backspace can be held down to trigger love.keypressed multiple times.
  love.keyboard.setKeyRepeat(true)
end

function Prompt:update(dt)

end

function Prompt:backspace()
  -- get the byte offset to the last UTF-8 character in the string.
  local byteoffset = utf8.offset(self.text, -1)

  if byteoffset then
    -- remove the last UTF-8 character.
    -- string.sub operates on bytes rather than UTF-8 characters, so we couldn't do string.sub(text, 1, -2).
    self.text = string.sub(self.text, 1, byteoffset - 1)
  end
end

function Prompt:textinput(t)
  self.text = self.text..t
end

function Prompt:draw()

end
