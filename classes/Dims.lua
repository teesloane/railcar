Object = require 'lib/classic/classic'
Dims = Object:extend(Object)

function Dims:new()
   self.width = love.graphics.getWidth()
   self.padding = love.graphics.getWidth() / 10
   self.left = self.padding
   self.right = self.width - self.padding
end

function Dims:update(dt)

end

function Dims:draw()

end
