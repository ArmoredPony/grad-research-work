local filters = require 'filters'
local rubrics = require('rubrics').tags
local texts <close> = io.open 'lenta-ru-news-tagged.csv'


---Dismantles text to its content, tag1 and tag2.
---@param text string
---@return string content, string tag1, string tag2
local function dismantletext(text)
  return text:match '(.+),([^,]+),([^,]+)$'
end


---Picks an appropriate tag or returns `nil` in case of failure.
---@param tag1 string
---@param tag2 string
---@return string?
local function choosetag(tag1, tag2)
  return rubrics[tag1] and tag1 or rubrics[tag2] and tag2 or nil
end


---Obtains, filters and returns keywords from a text.
---@param text string
---@return string[]
local function getkeywords(text)
  local mystem <close> = io.popen(
    'echo '..text..' |'
    ..'C:\\Users\\ilya\\Documents\\program\\research_work\\diploma\\util\\mystem.exe'
    ..' -nwl',
    'r'
  )
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


local rubrickeywords = {}
for rubric in pairs(rubrics) do
  rubrickeywords[rubric] = {}
end

local lineidx = 0
for line in texts:lines() do
  local text, tag1, tag2 = dismantletext(line)
  local tag = choosetag(tag1, tag2)
  if tag then
    local keywords = getkeywords(text)
    local dict = rubrickeywords[tag]
    for _, kw in ipairs(keywords) do
      dict[kw] = dict[kw] and dict[kw] + 1 or 1
    end
  end

  lineidx = lineidx + 1
  if lineidx % 1000 == 0 then print('#'..lineidx) end
end

for rubric in pairs(rubrics) do
  local dict = rubrickeywords[rubric]
  local l = {}
  for kw, cnt in pairs(dict) do l[#l+1] = {kw, cnt} end
  table.sort(l, function (a, b) return a[2] > b[2] end)

  local out <close> = io.open('wordbags/'..rubrics[rubric]..'.lua', 'w')
  out:write 'return {\n'
  for _, pair in ipairs(l) do
    out:write("  ['", pair[1], "'] = ", pair[2], ',\n')
  end
  out:write '}\n'
end
