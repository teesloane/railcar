-- TextInput 1.0 - for love2d 0.7.2
-- Copyright (c) 2011, Francesco Noferi
-- All rights reserved.

TextInput = class('TextInput')
function TextInput:initialize(x, y, size, w, callback)
  self.text = ""
  self.time = 0.0
  self.cursor = "|"
  self.cursor_pos = 0
  self.x = x
  self.y = y
  self.size = size
  self.w = w
  self.callback = callback
  self.shift = false
end

function TextInput:reset()
  self.shift = false
  self.cursor_pos = 0
  self.time = 0.0
  self.text = ""
end

function TextInput:step(k)
  self.time = self.time + k
  if self.time > 1.0 then
    if self.cursor == "|" then
      self.cursor = ""
    else
      self.cursor = "|"
    end
    self.time = 0.0
  end
  self.shift = love.keyboard.isDown("lshift", "rshift", "capslock")
end

function TextInput:keypressed(key, unicode)
  print("the key and unicode are", key, unicode)
  if key == "backspace" and self.cursor_pos > 0 then
    self.text = string.sub(self.text, 1, self.cursor_pos-1) .. string.sub(self.text, self.cursor_pos+1)
    self.cursor_pos = self.cursor_pos-1
  elseif key == "left" then
    self.cursor_pos = math.max(0, self.cursor_pos-1)
  elseif key == "right" then
    self.cursor_pos = math.min(self.text:len(), self.cursor_pos+1)
  elseif key == "delete" then
    self.text = string.sub(self.text, 1, self.cursor_pos) .. string.sub(self.text, self.cursor_pos+2)
  elseif key == "space" then
    self.text = self.text .. " "
  elseif key == "return" then
    self.callback()
  elseif self.text:len() < self.size and unicode < 166 and unicode > 31 then
    local thekey = key
    if self.shift then
      thekey = key:upper()
    end
    self.text = string.sub(self.text, 1, self.cursor_pos) .. thekey .. string.sub(self.text, self.cursor_pos+1)
    self.cursor_pos = self.cursor_pos+1
  end
end

function TextInput:drawBg()
  love.graphics.rectangle("line", self.x, self.y, 60, 120 )
end

function TextInput:draw()
   self:drawBg()

  -- display the actual text.
  love.graphics.printf(self.text, self.x, self.y, self.w)
  -- display the cursor
  love.graphics.printf(
    self.cursor,
    self.x+love.graphics.getFont():getWidth(string.sub(self.text, 1, self.cursor_pos))-love.graphics.getFont():getWidth(self.cursor)/2,
    self.y,
    self.w
  )
end
