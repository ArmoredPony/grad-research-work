local mystempath <const> =
'C:\\Users\\ilya\\Documents\\program\\research_work\\diploma\\util\\mystem.exe'
local filters = require 'filters'

return function (text)
  local cmd = 'echo '..text..'|'..mystempath..' -nwl'
  local mystem <close> = io.popen(cmd, 'r')
  local keywords = {}
  for kw in mystem:lines() do ---@cast kw string
    if kw:find '?' then goto continue end -- unrecognized token
    kw = kw:match('^[^|]+')
    -- token is too short, in filterlist or is a noun
    if utf8.len(kw) < 4 or filters[kw] or kw:find 'ть$' or kw:find 'ться$' then
      goto continue
    end
    keywords[#keywords+1] = kw
    ::continue::
  end
  return keywords
end
