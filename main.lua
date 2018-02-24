function love.load()
   text = "hi"
   myvar = 3
end

function love.update(dt)
end

function love.textinput(t)
   text = text..t
end


function love.draw()
   love.graphics.printf(text, 0, 0, love.graphics.getWidth())
end
