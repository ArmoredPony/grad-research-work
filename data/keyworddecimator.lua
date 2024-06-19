local rubrics = {
  ['science'] = true,
  ['sport'] = true,
  ['culture'] = true,
  ['economics'] = true,
  ['media'] = true,
  ['politics'] = true,
  ['society'] = true,
}

local dicts = {}
for rubric in pairs(rubrics) do
  dicts[rubric] = require('wordbags/'..rubric)
end

local union = {}
for _, dict in pairs(dicts) do
  for word in pairs(dict) do
    union[word] = true
  end
end

local wordlist = {}
for word in pairs(union) do
  wordlist[#wordlist+1] = word
end
table.sort(wordlist)

local rubricsordered = {}
for rubric in pairs(rubrics) do
  rubricsordered[#rubricsordered+1] = rubric
end
table.sort(rubricsordered)

local INDENT <const>, GAP <const> = '%-25s', '%-12s'
io.write(INDENT:format '')
for _, rubric in ipairs(rubricsordered) do
  io.write(GAP:format(rubric))
end
print()

for _, word in ipairs(wordlist) do
  -- io.write(INDENT:format(word))
  local met = 0
  for _, rubric in ipairs(rubricsordered) do
    if dicts[rubric][word] then met = met + 1 end
  end
  if met > 1 then
    io.write(INDENT:format(word), ' ', GAP:format(met))
    print()
  end
end
