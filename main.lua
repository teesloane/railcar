require 'lib.middleclass'
require 'classes.TextInput'
require 'classes.Dims'
local utf8 = require('utf8')

function love.load()
  state = "input"
  dims = Dims()
  print(dims.right, "padding is ", dims.padding)

  textbox = TextInput(
    dims.left,
    love.graphics.getHeight()/2,
    dims.width,
    dims.width,
    function ()
      state = "done"
    end
  )
  love.keyboard.setKeyRepeat(500, 50) -- This is required if you want to hold down keys to spam them
end

function love.update(k)
  if state == "input" then
    textbox:step(k)
  end
end

function love.draw()
  if state == "input" then
    textbox:draw()
  elseif state == "done" then
    love.graphics.print(
      "You typed: " .. textbox.text,
      love.graphics.getWidth()/2,
      love.graphics.getHeight()/2
    )
  end
end

function love.keypressed(key, unicode)
  if state == "input" then
    textbox:keypressed(key, utf8.codepoint(key))
  end
end
