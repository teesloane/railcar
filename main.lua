require 'Prompt'
Object = require 'libraries/classic'

function love.load()
   prompt = Prompt()
end

function love.update(dt)

end

function love.keypressed(key)
   if key == 'backspace' then
      prompt:backspace()
   end
end

function love.textinput(t)
   prompt:textinput(t)
end


function love.draw()
   love.graphics.printf(prompt.text, 0, 0, love.graphics.getWidth())
end
